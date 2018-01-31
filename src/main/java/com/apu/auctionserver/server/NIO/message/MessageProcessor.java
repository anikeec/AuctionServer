/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

import com.apu.auctionserver.nw.NetworkController;
import com.apu.auctionserver.server.NIO.WriteProxy;
import com.apu.auctionserver.utils.Log;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class MessageProcessor {
    
    private final Log log = Log.getInstance();
    private final Class classname = MessageProcessor.class;
    
    NetworkController networkController = new NetworkController();
    
    private static final int MESSAGE_QUEUE_SIZE = 100;
    private final BlockingQueue<Message> inputMessageQueue;
    private final BlockingQueue<Message> outputMessageQueue;
    
    private final MessageProcessorPool messageProcessorPool;
    
    private static MessageProcessor instance;
    
    public static MessageProcessor getInstance() {
//        if(instance == null)
//            instance = new MessageProcessor();
        return instance;
    }

    public MessageProcessor(WriteProxy writeProxy) {
        inputMessageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE, true);
        outputMessageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);
        messageProcessorPool = 
                new MessageProcessorPool(networkController,
                                            inputMessageQueue, 
                                            outputMessageQueue,
                                            writeProxy);
        messageProcessorPool.init();
        instance = this;
    }

    public void process(Message message) {
        String query = message.getMessageStr();
        log.debug(classname, "Message Received from socket: " + message.socketId);
        log.debug(classname, "Message: " + query);
        
        try {
            inputMessageQueue.put(message);
        } catch (InterruptedException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }

    }
    
}
