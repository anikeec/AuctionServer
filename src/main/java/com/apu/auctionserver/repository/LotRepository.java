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
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author apu
 */
public class LotRepository {
    private final SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
    private static LotRepository instance;
    
    private LotRepository() {
    }
    
    public static LotRepository getInstance() {
        if(instance == null)
            instance = new LotRepository();
        return instance;
    }
    
    public List<AuctionLot> getAuctionLots() {
        List<AuctionLot> list;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.getNamedQuery("AuctionLot.findAll");
            list = query.list();
        }
        return list;
    }
    
    public void saveAuctionLot(AuctionLot lot) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(lot);
            session.getTransaction().commit();
        }
    }
    
    public void removeAuctionLot(AuctionLot lot) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(lot);
            session.getTransaction().commit();
        }
    }
    
    public AuctionLot getAuctionLotById(int lotId) {
        AuctionLot ret;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.getNamedQuery("AuctionLot.findByLotId");
            query.setInteger("lotId", lotId);
//            ret = session.load(AuctionLot.class, lotId);
            ret = (AuctionLot) query.uniqueResult();
        }        
        return ret;
    }    
    
}
