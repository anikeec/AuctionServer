/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO;

import com.apu.auctionserver.server.NIO.message.MessageBuffer;
import com.apu.auctionserver.server.NIO.message.Message;
import java.util.Queue;

/**
 *
 * @author apu
 */
public class WriteProxy {
    
    private MessageBuffer messageBuffer = null;
    private Queue        writeQueue     = null;

    public WriteProxy(MessageBuffer messageBuffer, Queue writeQueue) {
        this.messageBuffer = messageBuffer;
        this.writeQueue = writeQueue;
    }

    public Message getMessage(){
        return this.messageBuffer.getMessage();
    }

    public boolean enqueue(Message message){
        return this.writeQueue.offer(message);
    }

}
