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
public class AuctionAPIMessageReaderFactory implements IMessageReaderFactory {
    
    public AuctionAPIMessageReaderFactory() {
    }

    @Override
    public IMessageReader createMessageReader() {
        return new AuctionAPIMessageReader();
    }
    
}
