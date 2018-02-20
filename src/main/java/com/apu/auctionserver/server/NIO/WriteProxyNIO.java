/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO;

import com.apu.auctionserver.server.WriteProxy;
import com.apu.auctionserver.server.NIO.message.Message;
import java.util.Queue;

/**
 *
 * @author apu
 */
public class WriteProxyNIO implements WriteProxy {
    
    private Queue        writeQueue     = null;

    public WriteProxyNIO(Queue writeQueue) {
        this.writeQueue = writeQueue;
    }

    @Override
    public synchronized boolean enqueue(Message message){
        return this.writeQueue.offer(message);
    }

}
