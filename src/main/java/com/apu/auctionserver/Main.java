/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver;

import com.apu.auctionserver.server.Server;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author apu
 */
public class Main {
    private static int CONNECTIONS_MAX = 10;
    private static int CONNECTION_PORT = 5050;    
    static Server server;
    
    public static void main(String[] args) {
        try {
            server = new Server(CONNECTION_PORT, CONNECTIONS_MAX);
            server.accept();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
}
