/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver;

import com.apu.auctionserver.auction.Auction;
import com.apu.auctionserver.server.Server;
import com.apu.auctionserver.server.IO.ServerIO;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class Main {
    private static final Log log = Log.getInstance();
    private static final Class classname = Main.class;
    private static final int CONNECTIONS_MAX = 10;
    private static final int CONNECTION_PORT = 5050;    
    static Server server;
    
    public static void main(String[] args) {
        Auction.getInstance().init();
        
        try {
            server = new ServerIO(CONNECTION_PORT, CONNECTIONS_MAX);
            server.accept();
        } catch (IOException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
    }
    
}
