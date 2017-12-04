/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author apu
 */
public class Server {
    private ServerSocket serverSocket;
    private ConnectionHandlerPool handlerPool;
    private int backlog;  

    public Server(int port, int backlog) throws IOException {            
            this.backlog = backlog;
            serverSocket = new ServerSocket(port, backlog);
    }

    public void accept() throws IOException {
            handlerPool = new ConnectionHandlerPool(backlog);
            System.out.println("Server started");
            while (true) {
                    Socket socket = serverSocket.accept();
                    handlerPool.addConnection(socket);
//                    new Thread(new ConnectionThread(socket)).start();
            }
    }
}
