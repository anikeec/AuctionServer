/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server;

import com.apu.auctionserver.server.NIO.message.Message;

/**
 *
 * @author apu
 */
public interface WriteProxy {

    boolean enqueue(Message message);
    
}
