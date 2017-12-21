/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

import com.apu.auctionserver.server.NIO.ServerSocketNIOProcessor;
import com.apu.auctionserver.server.NIO.SocketNIO;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class MessageWriter {
    
    private List<Message> writeQueue   = new ArrayList<>();
    private Message  messageInProgress = null;
    private int      bytesWritten      =    0;
    
    public MessageWriter() {
    }
    
    public void enqueue(Message message) {
        if(this.messageInProgress == null){
            this.messageInProgress = message;
        } else {
            this.writeQueue.add(message);
        }
    }
    
    /*
        1 - we have to check if bytes are available in the byteBuffer,
        1a - if available(messageInProgress != null) then send them,
        1b - if no data in bytebuffer then move next message to messageInProgress and then to byteBuffer, previously clear buffer    
    */
    public void write(SocketNIO socket, ByteBuffer byteBuffer) throws IOException {
//        ByteBuffer byteBuffer = ByteBuffer.allocate(ServerSocketNIOProcessor.BYTE_BUFFER_SIZE);
        if(this.messageInProgress != null) {
            // we have some data in the byteBuffer, full size of data = messageInProgress.size
            byteBuffer.clear();
            byteBuffer.put(this.messageInProgress.getMessageAsArray(), 
                    this.bytesWritten, 
                    this.messageInProgress.getMessageAsArray().length - this.bytesWritten);
            byteBuffer.flip();
            
            this.bytesWritten += socket.write(byteBuffer);
            byteBuffer.clear();
            
            if(bytesWritten >= this.messageInProgress.getMessageAsArray().length){
                this.messageInProgress = null;
            } 
        }
        
        if(this.messageInProgress == null) {
            if(this.writeQueue.size() > 0){
                this.messageInProgress = this.writeQueue.remove(0);
            } else {
                this.messageInProgress = null;
                //todo unregister from selector
            }
            bytesWritten = 0;
        }  
               
    }

    public boolean isEmpty() {
        return this.writeQueue.isEmpty();// && this.messageInProgress == null
    }
}
