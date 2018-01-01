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
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class MessageProcessorThread implements Runnable {
    
    private final Log log = Log.getInstance();
    private final Class classname = MessageProcessorThread.class;
    
    private final NetworkController networkController;
    private final BlockingQueue<Message> inputMessageQueue;
    private final BlockingQueue<Message> outputMessageQueue;
    private final WriteProxy writeProxy;

    public MessageProcessorThread(NetworkController networkController,
                                    BlockingQueue<Message> inputMessageQueue, 
                                    BlockingQueue<Message> outputMessageQueue,
                                    WriteProxy writeProxy) {
        this.networkController = networkController;
        this.inputMessageQueue = inputMessageQueue;
        this.outputMessageQueue = outputMessageQueue;
        this.writeProxy = writeProxy;
    } 

    @Override
    public void run() {
        
        Message message;
        Message answerMessage;
        String query;
        String answer;
        
        while(!Thread.currentThread().isInterrupted()) {
            message = null;
            try {
                message = inputMessageQueue.take();
            } catch (InterruptedException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
            if(message == null) continue;
            query = message.getMessageStr();
            try {
                answer = networkController.handle(query);
                answerMessage = new Message();
                answerMessage.socketId = message.socketId;
                answerMessage.writeToMessage(answer.getBytes());
                writeProxy.enqueue(answerMessage);
//                outputMessageQueue.put(message);
            } catch (ErrorQueryException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
            
        }        
    }
    
}
