/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO;

import com.apu.auctionserver.server.NIO.message.MessageProcessor;
import com.apu.auctionserver.server.NIO.message.MessageReader;
import com.apu.auctionserver.server.Server;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import java.nio.channels.ServerSocketChannel;
import java.util.Queue;
import java.util.concurrent.ArrayBlockingQueue;

/**
 *
 * @author apu
 */
public class ServerNIO implements Server {
    
    public static final int CONNECTION_QUEUE_SIZE = 1024;
    private final Log log = Log.getInstance();
    private final Class classname = ServerNIO.class;
    
    private ServerSocketChannel serverSocketChannel;
    private final int serverPort;
    private final int backlog;
    
    private ServerSocketNIOAccepter  socketAccepter  = null;
    private ServerSocketNIOProcessor socketProcessor = null;

    public ServerNIO(int port, int backlog) {
        this.serverPort = port;
        this.backlog = backlog;
    }    

    @Override
    public void accept() throws IOException {
        
        Queue socketQueue = new ArrayBlockingQueue(CONNECTION_QUEUE_SIZE);
        
        this.socketAccepter  = 
                new ServerSocketNIOAccepter(serverPort, socketQueue); 
        
        this.socketProcessor = 
                new ServerSocketNIOProcessor(socketQueue);
        
        Thread accepterThread  = new Thread(this.socketAccepter);
        Thread processorThread = new Thread(this.socketProcessor);
        
        accepterThread.start();
        processorThread.start();
    }
    
}
