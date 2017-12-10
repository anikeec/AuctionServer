/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server;

import com.apu.auctionserver.controller.Controller;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.util.concurrent.BlockingQueue;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apu
 */
public class ConnectionHandler implements Runnable{
    private final int SOCKET_RECEIVE_TIMEOUT = 1000;
    private BlockingQueue<Socket> queue;
    private Socket socketTemp;
	
    public ConnectionHandler(BlockingQueue<Socket> queue) {
            this.queue = queue;
    }

    public void run() {
        while(true) {
            socketTemp = null;
            try {
                socketTemp = queue.take();
            } catch (InterruptedException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
            if(socketTemp != null) {
                handleSocket(socketTemp);
            }
        }
    }

    private void handleSocket(Socket socket) {
        try {
            try {
                // do anything you need
                InputStream is = socket.getInputStream();
                OutputStream os = socket.getOutputStream();
                BufferedReader in = new BufferedReader(new InputStreamReader(is));
                BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
                String line;
                Controller controller = Controller.getInstance();
                String str;
                int amount = 0;
                StringBuilder sb = new StringBuilder();
                byte[] bytes = new byte[1024];
                socket.setSoTimeout(SOCKET_RECEIVE_TIMEOUT);
                while(!socket.isClosed()) {
                    if(Thread.currentThread().isInterrupted()) {
                        System.out.println("Server thread. Interrupted.");
                        break;
                    }
//                    if(is.available() == 0) continue;
                    try {
                        amount = is.read(bytes, 0, 1024);
                    } catch (SocketTimeoutException ex) {
                        continue;
                    }
                    if(amount == -1) {
                        System.out.println("Receive end of socket.");
                        break;
                    }
                    if(amount == 0) continue;
                    str = new String(bytes, 0, amount);
                    sb.append(str);
                    if(sb.toString().contains("\r\n")) {
                        line = sb.toString();
                        sb.delete(0, sb.capacity());
                        if(line != null) {
                            System.out.println(line);
                            controller.handle(line, socket, in, out);
                        }
                    }
                } 
                System.out.println("Server stopped");
                os.close();
                is.close();            
            } catch (Exception ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
                System.out.println("Client closed connection");
            } finally {
                System.out.println("Closing socket");
                socket.close();
            }
        } catch (IOException ex) {
                Logger.getLogger(ConnectionHandler.class.getName()).log(Level.SEVERE, null, ex);
            }
    }
}
