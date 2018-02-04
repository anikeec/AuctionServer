/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.controller;

import com.apu.auctionserver.server.NIO.msg.Msg;
import com.apu.auctionserver.utils.Log;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author apu
 */
public class AuctionControllerPool {
    private final Log log = Log.getInstance();
    private final Class classname = AuctionControllerPool.class;
    
    private final int POOL_SIZE = 5;
    private final List<Thread> acThreadList = new ArrayList<>();
    
    private final int MESSAGE_QUEUE_SIZE = 10;
    private final BlockingQueue<Msg> inputMessageQueue = 
                    new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE, true);
    private final BlockingQueue<Msg> outputMessageQueue = 
                    new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE); 

    public void init() {
        for(int i=0; i<POOL_SIZE; i++) {
            Thread auThread = new Thread(
                    new AuctionController(inputMessageQueue,outputMessageQueue));
            acThreadList.add(auThread);
            auThread.setDaemon(true);
            auThread.start();
        }
    }
    
    public void close() {
        for(Thread thread : acThreadList) {
            thread.interrupt();
        }
    }

    public BlockingQueue<Msg> getInputMsgQueue() {
        return this.inputMessageQueue;
    }
    
    public BlockingQueue<Msg> getOutputMsgQueue() {
        return this.outputMessageQueue;
    }
    
}
