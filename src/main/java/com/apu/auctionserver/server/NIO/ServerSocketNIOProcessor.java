/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO;

import com.apu.auctionserver.server.NIO.message.MessageWriter;
import com.apu.auctionserver.server.NIO.message.Message;
import com.apu.auctionserver.server.NIO.message.MessageReader;
import com.apu.auctionserver.server.NIO.message.MessageProcessor;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.ClosedChannelException;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.Queue;
import java.util.Set;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class ServerSocketNIOProcessor implements Runnable {
    
    private final Log log = Log.getInstance();
    private final Class classname = ServerSocketNIOProcessor.class;
    
    public static int BYTE_BUFFER_SIZE = 1024;
    private long nextSocketId = 16 * 1024; //start incoming socket ids from 16K - reserve bottom ids for pre-defined sockets (servers).
    
    private final Map<Long,ByteBuffer> readBuffersMap = new HashMap<>();
    private final Map<Long,ByteBuffer> writeBuffersMap = new HashMap<>();
    private final Map<Long, SocketNIO> socketMap = new HashMap<>();
    private final Set<SocketNIO> emptyToNonEmptySockets = new HashSet<>();
    private final Set<SocketNIO> nonEmptyToEmptySockets = new HashSet<>();
    
    private Queue<SocketNIO>  inboundSocketQueue = null;
    private final Queue<Message> outboundMessageQueue = new LinkedList<>(); 
    //todo use a better / faster queue.

    private final MessageReader messageReader;    
    private final MessageProcessor messageProcessor;
    private Selector   readSelector    = null;
    private Selector   writeSelector   = null;    
    private WriteProxy writeProxy      = null;

    public ServerSocketNIOProcessor(Queue<SocketNIO> inboundSocketQueue) throws IOException {
        this.inboundSocketQueue   = inboundSocketQueue;        
        this.writeProxy           = new WriteProxy(this.outboundMessageQueue);        
        this.messageReader        = new MessageReader();        
        this.messageProcessor     = new MessageProcessor(writeProxy);
        this.readSelector         = Selector.open();
        this.writeSelector        = Selector.open();
    }    

    @Override
    public void run() {
        while(true){
            try{
                executeCycle();
//                Thread.sleep(1);
            } catch(IOException  ex){
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
    public void executeCycle() throws IOException {
        takeNewSockets();
        readFromSockets();
        writeToSockets();
    }
    
    public void takeNewSockets() throws IOException {
        SocketNIO newSocket = this.inboundSocketQueue.poll();

        while(newSocket != null){
            newSocket.socketId = this.nextSocketId++;
            newSocket.socketChannel.configureBlocking(false);
            newSocket.messageReader = this.messageReader;
            newSocket.messageWriter = new MessageWriter();

            this.socketMap.put(newSocket.socketId, newSocket);
            this.readBuffersMap.put(newSocket.socketId, 
                                    ByteBuffer.allocate(BYTE_BUFFER_SIZE));
            this.writeBuffersMap.put(newSocket.socketId, 
                                    ByteBuffer.allocate(BYTE_BUFFER_SIZE));

            SelectionKey key = newSocket.socketChannel
                        .register(this.readSelector, SelectionKey.OP_READ);
            key.attach(newSocket);

            newSocket = this.inboundSocketQueue.poll();
        }
    }
    
    public void readFromSockets() throws IOException {
        int readReady = this.readSelector.selectNow();

        if(readReady > 0){
            log.debug(classname, "Have some keys. Amount: " + readReady);
            Set<SelectionKey> selectedKeys = this.readSelector.selectedKeys();
            Iterator<SelectionKey> keyIterator = selectedKeys.iterator();

            while(keyIterator.hasNext()) {
                SelectionKey key = keyIterator.next();
                try {
                    readFromSocket(key);
                } catch (IOException ex) {
                    log.debug(classname,ExceptionUtils.getStackTrace(ex));
                    SocketNIO socket = (SocketNIO) key.attachment();
                    log.debug(classname, "Socket closed: " + socket.socketId);
                    this.socketMap.remove(socket.socketId);
                    this.readBuffersMap.remove(socket.socketId);
                    this.writeBuffersMap.remove(socket.socketId);
                    key.attach(null);
                    key.cancel();
                    key.channel().close();
                }

                keyIterator.remove();
            }
            selectedKeys.clear();
        }
    }
    
    private void readFromSocket(SelectionKey key) throws IOException {
        SocketNIO socket = (SocketNIO) key.attachment();
        ByteBuffer bBuffer = this.readBuffersMap.get(socket.socketId);
        socket.messageReader.read(socket, bBuffer);

        List<Message> fullMessages = socket.messageReader.getMessages();
        if(fullMessages.size() > 0){
            for(Message message : fullMessages){
                message.socketId = socket.socketId;
                this.messageProcessor.process(message);  
            }
            fullMessages.clear();
        }

        if(socket.endOfStreamReached){
            log.debug(classname, "Socket closed: " + socket.socketId);
            this.socketMap.remove(socket.socketId);
            this.readBuffersMap.remove(socket.socketId);
            this.writeBuffersMap.remove(socket.socketId);
            key.attach(null);
            key.cancel();
            key.channel().close();
        }
    }
    
    public void writeToSockets() throws IOException {

        // Take all new messages from outboundMessageQueue
        takeNewOutboundMessages();

        // Cancel all sockets which have no more data to write.
        cancelEmptySockets();

        // Register all sockets that *have* data and which are not yet registered.
        registerNonEmptySockets();

        // Select from the Selector.
        int writeReady = this.writeSelector.selectNow();

        if(writeReady > 0){
            Set<SelectionKey>      selectionKeys = this.writeSelector.selectedKeys();
            Iterator<SelectionKey> keyIterator   = selectionKeys.iterator();

            while(keyIterator.hasNext()){
                SelectionKey key = keyIterator.next();

                SocketNIO socket = (SocketNIO) key.attachment();

                ByteBuffer bBuffer = this.writeBuffersMap.get(socket.socketId);
                socket.messageWriter.write(socket, bBuffer);

                if(socket.messageWriter.isEmpty()){
                    this.nonEmptyToEmptySockets.add(socket);
                }

                keyIterator.remove();
            }

            selectionKeys.clear();

        }
    }

    private void registerNonEmptySockets() throws ClosedChannelException {
        for(SocketNIO socket : emptyToNonEmptySockets){
            socket.socketChannel.register(this.writeSelector, SelectionKey.OP_WRITE, socket);
        }
        emptyToNonEmptySockets.clear();
    }

    private void cancelEmptySockets() {
        for(SocketNIO socket : nonEmptyToEmptySockets){
            SelectionKey key = socket.socketChannel.keyFor(this.writeSelector);
            key.cancel();
        }
        nonEmptyToEmptySockets.clear();
    }

    private void takeNewOutboundMessages() {
        Message outMessage = this.outboundMessageQueue.poll();
        while(outMessage != null){
            SocketNIO socket = this.socketMap.get(outMessage.socketId);

            if(socket != null){
                MessageWriter messageWriter = socket.messageWriter;
                if(messageWriter.isEmpty()){
                    messageWriter.enqueue(outMessage);
                    nonEmptyToEmptySockets.remove(socket);
                    emptyToNonEmptySockets.add(socket);    
                } else{
                   messageWriter.enqueue(outMessage);
                }
            }

            outMessage = this.outboundMessageQueue.poll();
        }
    }
    
}
