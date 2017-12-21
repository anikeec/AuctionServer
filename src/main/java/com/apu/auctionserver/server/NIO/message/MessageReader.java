/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

import com.apu.auctionserver.server.NIO.SocketNIO;
import java.io.IOException;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class MessageReader {

    private final List<Message> completeMessages = new ArrayList<>();
    
    public MessageReader() {        
    }

    public void read(SocketNIO socket, ByteBuffer byteBuffer) throws IOException {
        int bytesRead = socket.read(byteBuffer);
        byteBuffer.flip();

        if(byteBuffer.remaining() == 0){
            byteBuffer.clear();
            return;
        }

        int endIndex = AuctionAPIUtil.parseRequest(byteBuffer.array(), bytesRead);
        if(endIndex != -1){
            Message message = new Message();
            message.socketId = socket.socketId;
            message.writePartialMessageToMessage(byteBuffer, endIndex);

            completeMessages.add(message);
        }
//        byteBuffer.clear();
    }

    public List<Message> getMessages() {
        return this.completeMessages;
    }
    
}
