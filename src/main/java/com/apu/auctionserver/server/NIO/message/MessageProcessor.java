/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

import com.apu.auctionserver.nw.NetworkController;
import com.apu.auctionserver.nw.exception.ErrorQueryException;
import com.apu.auctionserver.server.NIO.WriteProxy;
import com.apu.auctionserver.utils.Log;
import java.util.concurrent.ArrayBlockingQueue;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class MessageProcessor {
    
    private final Log log = Log.getInstance();
    private final Class classname = MessageProcessor.class;
    
    NetworkController networkController = new NetworkController();
    
    private static final int MESSAGE_QUEUE_SIZE = 10;
    private final BlockingQueue<Message> inputMessageQueue;
    private final BlockingQueue<Message> outputMessageQueue;
    
    private MessageProcessorPool messageProcessorPool;
    private WriteProxy writeProxy;

    public MessageProcessor() {
        inputMessageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);
        outputMessageQueue = new ArrayBlockingQueue<>(MESSAGE_QUEUE_SIZE);        
    }
    
    public void init() {
        messageProcessorPool = 
                new MessageProcessorPool(networkController,
                                            inputMessageQueue, 
                                            outputMessageQueue,
                                            writeProxy);
        messageProcessorPool.init();
    }
    
    public void setWriteProxy(WriteProxy writeProxy) {
        this.writeProxy = writeProxy;
    }

    public void process(Message message, WriteProxy writeProxy) {
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
