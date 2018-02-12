/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

import com.apu.auctionserver.server.nw.controller.NwInputController;
import com.apu.auctionserver.server.nw.exception.ErrorQueryException;
import com.apu.auctionserver.utils.Log;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class MessageProcessor {
    
    private final Log log = Log.getInstance();
    private final Class classname = MessageProcessor.class;
    
    private static MessageProcessor instance;
    
    private NwInputController nwInputController;

    public MessageProcessor() {
        instance = this;        
    }

    public void process(Message message) {
        String query = message.getMessageStr();
        log.debug(classname, "Socket: " + message.socketId + ". Message: " + query);
        try {
            nwInputController = NwInputController.getInstance();
            nwInputController.handle(query, message.socketId);
        } catch (ErrorQueryException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }

    }
    
}
