/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.Jetty;

import com.apu.auctionserver.server.NIO.message.Message;
import com.apu.auctionserver.server.WriteProxy;
import java.util.Queue;

/**
 *
 * @author apu
 */
public class WriteProxyJetty implements WriteProxy {
    
    private Queue   writeQueue;

    public WriteProxyJetty(Queue writeQueue) {
        this.writeQueue = writeQueue;
    }

    @Override
    public synchronized boolean enqueue(Message message) {
        return this.writeQueue.offer(message);
    }
    
}
