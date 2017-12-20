/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

import com.apu.auctionserver.server.NIO.SocketNIO;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.List;

/**
 *
 * @author apu
 */
public interface IMessageReader {
    
    public void init(MessageBuffer readMessageBuffer);

    public void read(SocketNIO socket, ByteBuffer byteBuffer) throws IOException;

    public List<Message> getMessages();
    
}
