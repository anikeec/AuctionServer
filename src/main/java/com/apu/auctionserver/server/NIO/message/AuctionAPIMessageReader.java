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
public class AuctionAPIMessageReader implements IMessageReader {
    
    private MessageBuffer messageBuffer    = null;

    private final List<Message> completeMessages = new ArrayList<>();
    private Message       nextMessage      = null;
    
    public AuctionAPIMessageReader() {        
    }

    @Override
    public void init(MessageBuffer readMessageBuffer) {
        this.messageBuffer        = readMessageBuffer;
        this.nextMessage          = messageBuffer.getMessage();
    }

    @Override
    public void read(SocketNIO socket, ByteBuffer byteBuffer) throws IOException {
        int bytesRead = socket.read(byteBuffer);
        byteBuffer.flip();

        if(byteBuffer.remaining() == 0){
            byteBuffer.clear();
            return;
        }

        this.nextMessage.writeToMessage(byteBuffer);

        int endIndex = AuctionAPIUtil
                .parseRequest(this.nextMessage.sharedArray, 
                            this.nextMessage.offset, 
                            this.nextMessage.offset + this.nextMessage.length);
        if(endIndex != -1){
            Message message = this.messageBuffer.getMessage();

            message.writePartialMessageToMessage(nextMessage, endIndex);

            completeMessages.add(nextMessage);
            nextMessage = message;
        }
        byteBuffer.clear();
    }

    @Override
    public List<Message> getMessages() {
        return this.completeMessages;
    }
    
}
