/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server;

import com.apu.auctionserver.utils.Log;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class ConnectionThread implements Runnable{
    
    private static final Log log = Log.getInstance();
    private final Class classname = ConnectionThread.class;
    
    private Socket socketTemp;
	
    public ConnectionThread(Socket socket) {
        this.socketTemp = socket;
    }

    public void run() {            
        handleSocket(socketTemp);
    }

    private void handleSocket(Socket socket) {
        try {
            InputStream is = socket.getInputStream();
            OutputStream os = socket.getOutputStream();
            BufferedReader in = new BufferedReader(new InputStreamReader(is));
            BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
            String line = null;
            while(true) {
                line = in.readLine(); // ожидаем пока клиент пришлет строку текста.
                if(line.contains("Bue")) break;
                log.debug(classname, line);
                out.write(line + "\n"); // отсылаем клиенту обратно ту самую строку текста.
                out.flush(); // заставляем поток закончить передачу данных.
//                System.out.println();
            }  
            socketTemp.close();
        } catch (IOException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
    }
}

