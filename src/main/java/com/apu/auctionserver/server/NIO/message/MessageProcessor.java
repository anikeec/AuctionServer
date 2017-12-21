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
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class MessageProcessor {
    
    private final Log log = Log.getInstance();
    private final Class classname = MessageProcessor.class;
    
    NetworkController networkController = new NetworkController();

    public void process(Message message, WriteProxy writeProxy) {
        String query = message.getMessageStr();
        log.debug(classname, "Message Received from socket: " + message.socketId);
        log.debug(classname, "Message: " + query);

        String answer = "";
        try {
            answer = networkController.handle(query);        
//            answer += "\r\n";
            Message response = new Message();
            response.socketId = message.socketId;            
            response.writeToMessage(answer.getBytes());
            log.debug(classname, "Answer: " + answer);
            writeProxy.enqueue(response);
        } catch (ErrorQueryException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        } 
    }
    
}
