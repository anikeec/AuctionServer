/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver;

import com.apu.auctionserver.DB.HibernateSessionFactory;
import com.apu.auctionserver.entities.AuctionLotStatus;
import com.apu.auctionserver.entity.Auction;
import com.apu.auctionserver.entity.AuctionLot;
import com.apu.auctionserver.entity.User;
import com.apu.auctionserver.server.Server;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.hibernate.Session;

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
        auctionInit();
        dbInit();
        
        try {
            server = new Server(CONNECTION_PORT, CONNECTIONS_MAX);
            server.accept();
        } catch (IOException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
    }
    
    private static void auctionInit() {
        AuctionLot lot1, lot2;
        User user;
        
        lot1 = new AuctionLot(1, 10, "Book");
        Auction.getInstance().addLotToAuction(lot1);
        lot2 = new AuctionLot(2, 25, "TVset");
        Auction.getInstance().addLotToAuction(lot2);       
    }
    
    private static void dbInit() {
        try (Session session = HibernateSessionFactory.getSessionFactory().openSession()) {
            session.beginTransaction();
            
            AuctionLotStatus lotStatus = new AuctionLotStatus(1);
            lotStatus.setName("temp");
            session.save(lotStatus);
            session.getTransaction().commit();
        }
    }
    
}
