/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.Jetty;

import com.apu.auctionserver.server.NIO.message.Message;
import com.apu.auctionserver.utils.Log;
import java.util.List;
import java.util.Queue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class JettyOutputThread implements Runnable {
    
    private final Log log = Log.getInstance();
    private final Class classname = JettyOutputThread.class;
    
    private final Queue<Message> outboundMessageQueue;
    private final WebSocketCollection socketCollection = 
                            WebSocketCollection.getInstance();

    public JettyOutputThread(Queue<Message> outboundMessageQueue) {
        this.outboundMessageQueue = outboundMessageQueue;
    }

    @Override
    public void run() {
        while(true) {
            Message mess = outboundMessageQueue.poll();
            if(mess == null) {
                try {
                    Thread.sleep(10);
                } catch (InterruptedException ex) {
                    log.debug(classname,ExceptionUtils.getStackTrace(ex));
                }
                continue;
            }
            long socketId = mess.socketId;
            List<WebSocketChannel> socketChannelList = 
                        socketCollection.getChannelListBySocketId(socketId);
            for(WebSocketChannel socketChannel:socketChannelList) {
                if(socketChannel != null) {
                    log.debug(classname, "Send to socketId: " + socketId + 
                                    ", socketChannel: " + socketChannel);
                    socketChannel.sendTextMessage(mess.getMessageStr());
                }
            }
        }
    }
    
}
