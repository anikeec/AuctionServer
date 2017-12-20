/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

/**
 *
 * @author apu
 */
public class MessageBuffer {
    
    public MessageBuffer() {

    }

    public Message getMessage() {
        return new Message();
    }

    public boolean expandMessage(Message message){
        return false;
    }
}
