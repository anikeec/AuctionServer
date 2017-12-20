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
import java.util.Arrays;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class AuctionAPIMessageProcessor implements IMessageProcessor {
    
    private final Log log = Log.getInstance();
    private final Class classname = AuctionAPIMessageProcessor.class;
    
    private static int counter = 0;

    @Override
    public void process(Message message, WriteProxy writeProxy) {
        String query = message.getMessageStr();
        log.debug(classname, "Message Received from socket: " + message.socketId);
        log.debug(classname, "Message: " + query);

        String answer = "";
        try {
            answer = new NetworkController().handle(query);        
            counter++;
            if(counter > 1) {
                log.debug(classname, "pause");
            }
            Message response = writeProxy.getMessage();
            response.socketId = message.socketId;
            
            response.writeToMessage(answer.getBytes());
            log.debug(classname, "Answer: " + answer);

            writeProxy.enqueue(response);
        } catch (ErrorQueryException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        } 
    }
    
}
