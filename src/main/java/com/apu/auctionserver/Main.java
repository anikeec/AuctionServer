/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver;

import com.apu.auctionserver.entity.Auction;
import com.apu.auctionserver.entity.AuctionLot;
import com.apu.auctionserver.entity.User;
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
        auctionInit();
        
        try {
            server = new Server(CONNECTION_PORT, CONNECTIONS_MAX);
            server.accept();
        } catch (IOException ex) {
            Logger.getLogger(Main.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    private static void auctionInit() {
        AuctionLot lot;
        User user;
        Auction auction = Auction.getInstance();
        
        user = new User(1);
        auction.addUserToAuction(user);
        lot = new AuctionLot(1, 10, "Book");
        auction.addLotToAuction(lot);
        user.addLotToObserved(lot); 
        lot = new AuctionLot(2, 25, "TVset");
        auction.addLotToAuction(lot);
        user.addLotToObserved(lot);
        
    }
    
}
