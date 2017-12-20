/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO;

import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.channels.ServerSocketChannel;
import java.nio.channels.SocketChannel;
import java.util.Queue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class ServerSocketNIOAccepter implements Runnable {
    
    private final Log log = Log.getInstance();
    private final Class classname = ServerSocketNIOAccepter.class;
    
    private int tcpPort = 0;
    private ServerSocketChannel serverSocket = null;

    private Queue socketQueue = null;

    public ServerSocketNIOAccepter(int tcpPort, Queue socketQueue)  {
        this.tcpPort     = tcpPort;
        this.socketQueue = socketQueue;
    }

    @Override
    public void run() {
        try{
            this.serverSocket = ServerSocketChannel.open();
            this.serverSocket.bind(new InetSocketAddress(tcpPort));
        } catch(IOException ex){
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
            return;
        }


        while(true){
            try{
                SocketChannel socketChannel = this.serverSocket.accept();

                log.debug(classname, "Socket accepted: " + socketChannel);

                //todo check if the queue can even accept more sockets.
                this.socketQueue.add(new SocketNIO(socketChannel));

            } catch(IOException ex){
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }

        }
    }
    
}
