/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.message;

import com.apu.auctionserver.server.NIO.ServerSocketNIOProcessor;
import java.nio.ByteBuffer;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class Message {
    
//    private StringBuffer stringBuffer = new StringBuffer();
    private byte[] byteArray;
    
    public long socketId = 0; // the id of source socket or destination socket, depending on whether is going in or out.
    
    public Message() {
    }
    
    public byte[] getMessageAsArray() {        
//        byte[] array = stringBuffer.toString().getBytes();
//        return array;
        if(this.byteArray == null)
            return new byte[0];
        else
            return this.byteArray;
    }
    
    public String getMessageStr() {       
//        return stringBuffer.toString();
        if(this.byteArray == null)
            return "";
        else
            return new String(this.byteArray); 
    }

    public void writeToMessage(byte[] byteArray){
//        String str = new String(byteArray);
//        this.stringBuffer = new StringBuffer(str);
        this.byteArray = byteArray;
    }

    public void writePartialMessageToMessage(ByteBuffer srcBuffer, int endIndex) {        
//        int size = ServerSocketNIOProcessor.BYTE_BUFFER_SIZE;
        int length = endIndex + 1;                                              //question ????????
        this.byteArray = new byte[length];
        System.arraycopy(srcBuffer.array(), 0, this.byteArray, 0, length);
////        int addSymbolsLength = 2;
//        byte[] destBuffer = new byte[length];
//        System.arraycopy(srcBuffer.array(), 0, destBuffer, 0, length);
//        String str = new String(destBuffer);
////        destBuffer = new byte[size];
//        this.stringBuffer.append(str);
//        
////        System.arraycopy(srcBuffer.array(), 
////                            length, 
////                            destBuffer, 
////                            0, 
////                            size - length);
        srcBuffer.clear();
//        srcBuffer.put(destBuffer, 0, size);
    }
    
}
