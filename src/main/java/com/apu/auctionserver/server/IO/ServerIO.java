/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.IO;

import com.apu.auctionserver.server.Server;
import com.apu.auctionserver.server.UserControlThread;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

/**
 *
 * @author apu
 */
public class ServerIO implements Server {
    
    private static final Log log = Log.getInstance();
    private final Class classname = ServerIO.class;
    
    private ServerSocket serverSocket;
    private ConnectionHandlerPool handlerPool;
    private int backlog;  

    public ServerIO(int port, int backlog) throws IOException {            
            this.backlog = backlog;
            serverSocket = new ServerSocket(port, backlog);
    }

    @Override
    public void accept() throws IOException {
            handlerPool = new ConnectionHandlerPool(backlog);
            log.debug(classname, "Server started");
            Thread userControlThread = new Thread(new UserControlThread());
            userControlThread.setDaemon(true);
            userControlThread.start();
            while (true) {
                    Socket socket = serverSocket.accept();
                    handlerPool.addConnection(socket);
            }
    }
}
