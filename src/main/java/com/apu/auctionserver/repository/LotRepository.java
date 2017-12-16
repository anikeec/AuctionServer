/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository;

import com.apu.auctionserver.DB.HibernateSessionFactory;
import com.apu.auctionserver.DB.entity.AuctionLot;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author apu
 */
public class LotRepository {
    private final SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
    private final List<AuctionLot> auctionLots = new ArrayList<>();
    private static LotRepository instance;
    
    private LotRepository() {
    }
    
    public static LotRepository getInstance() {
        if(instance == null)
            instance = new LotRepository();
        return instance;
    }
    
    public List<AuctionLot> getAuctionLots() {
        return auctionLots;
    }
    
    public void addAuctionLot(AuctionLot lot) {
        Session session = sessionFactory.openSession();
        session.beginTransaction();
//        session.g
//        if(!auctionLots.contains(lot))
//            auctionLots.add(lot);       
        session.save(lot);
        session.getTransaction().commit();
        session.close();
    }
    
    public void removeAuctionLot(AuctionLot lot) {
        if(auctionLots.contains(lot)) {
            auctionLots.remove(lot);
        }
    }
    
    public AuctionLot getAuctionLotById(int lotId) {
        AuctionLot ret = null;
        for(AuctionLot lot:auctionLots) {
            if(lot.getLotId() == lotId) return lot;
        }
        return ret;
    }
    
    public void updateAuctionLot(AuctionLot lot) {
        AuctionLot lotSrc = getAuctionLotById(lot.getLotId());
        if(lotSrc == null) return;
        lotSrc.setLastRate(lot.getLastRate());
        lotSrc.setLastRateUser(lot.getLastRateUser());
    }
    
    
}
