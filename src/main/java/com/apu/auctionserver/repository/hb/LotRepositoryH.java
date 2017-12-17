/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.hb;

import com.apu.auctionserver.DB.HibernateSessionFactory;
import com.apu.auctionserver.DB.entity.AuctionLot;
import com.apu.auctionserver.repository.LotRepository;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author apu
 */
public class LotRepositoryH implements LotRepository {
    private final SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
    private static LotRepositoryH instance;
    
    private LotRepositoryH() {
    }
    
    public static LotRepositoryH getInstance() {
        if(instance == null)
            instance = new LotRepositoryH();
        return instance;
    }
    
    @Override
    public List<AuctionLot> getAuctionLots() {
        List<AuctionLot> list;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.getNamedQuery("AuctionLot.findAll");
            list = query.list();
        }
        return list;
    }
    
    @Override
    public void saveAuctionLot(AuctionLot lot) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(lot);
            session.getTransaction().commit();
        }
    }
    
    @Override
    public void removeAuctionLotById(int lotId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            AuctionLot lot = session.load(AuctionLot.class, lotId);
            session.delete(lot);
            session.getTransaction().commit();
        }
    }
    
    @Override
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
