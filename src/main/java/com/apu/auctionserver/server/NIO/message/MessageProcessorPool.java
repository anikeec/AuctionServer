/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

import com.apu.auctionserver.nw.NetworkController;
import com.apu.auctionserver.server.NIO.WriteProxy;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.BlockingQueue;

/**
 *
 * @author apu
 */
public class MessageProcessorPool {
    
    List<Thread> mpThreadList = new ArrayList<>();
    
    private final static int MP_POOL_SIZE = 3;
    private final BlockingQueue<Message> inputMessageQueue;
    private final BlockingQueue<Message> outputMessageQueue;
    private final NetworkController networkController;
    private final WriteProxy writeProxy;

    public MessageProcessorPool(NetworkController networkController,
                                    BlockingQueue<Message> inputMessageQueue, 
                                    BlockingQueue<Message> outputMessageQueue,
                                    WriteProxy writeProxy) {
        this.networkController = networkController;
        this.inputMessageQueue = inputMessageQueue;
        this.outputMessageQueue = outputMessageQueue;
        this.writeProxy = writeProxy;
    }
    
    public void init() {
        for(int i=0; i<MP_POOL_SIZE; i++) {
            Thread mpThread = 
                new Thread(new MessageProcessorThread(networkController,
                                                        inputMessageQueue,
                                                        outputMessageQueue,
                                                        writeProxy));
            mpThreadList.add(mpThread);
            mpThread.setDaemon(true);
            mpThread.start();
        }
    }
    
    public void close() {
        for(Thread thread : mpThreadList) {
            thread.interrupt();
        }
    }
    
}
