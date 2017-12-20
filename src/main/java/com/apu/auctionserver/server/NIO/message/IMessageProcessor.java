/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

import com.apu.auctionserver.server.NIO.WriteProxy;

/**
 *
 * @author apu
 */
public interface IMessageProcessor {
    
    public void process(Message message, WriteProxy writeProxy);
    
}
