/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.hb;

import com.apu.auctionserver.repository.entity.AuctionLot;
import com.apu.auctionserver.repository.entity.Observe;
import com.apu.auctionserver.repository.entity.User;
import com.apu.auctionserver.repository.ObserveRepository;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author apu
 */
public class ObserveRepositoryH implements ObserveRepository {
    private final SessionFactory sessionFactory = 
            HibernateService.getSessionFactory();
    private final LotRepositoryH lotRepository = 
                        LotRepositoryH.getInstance();
    private static ObserveRepositoryH instance;
    
    private ObserveRepositoryH() {
    }
    
    public static ObserveRepositoryH getInstance() {
        if(instance == null)
            instance = new ObserveRepositoryH();
        return instance;
    }
    
    @Override
    public List<AuctionLot> getObservableAuctionLotsByUser(User user) {
        List<AuctionLot> list = new ArrayList<>();
        List<Observe> observes;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            Query query = session.getNamedQuery("Observe.findByUserId");
            query.setInteger("userId", user.getUserId());
            observes = query.list();
            for(Observe o:observes) {
                Query query1 = session.getNamedQuery("AuctionLot.findByLotId");
                query1.setInteger("lotId", o.getLot().getLotId());                
                list.add((AuctionLot) query1.uniqueResult());
            }
            session.getTransaction().commit();
        }        
        return list;
    }
    
    @Override
    public void clearObservableAuctionLotsByUser(User user) {
        List<Observe> observes;
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();

            Query query = session.getNamedQuery("Observe.findByUserId");
            query.setInteger("userId", user.getUserId());
            observes = query.list();
            for(Observe o:observes) {
                session.delete(o);
            }
            session.getTransaction().commit();
        }
        
    }
    
    @Override
    public void addAuctionLotToObservableByUser(User user, AuctionLot lot) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
//            lot = lotRepository.getAuctionLotById(lotId);
            Observe observe = new Observe();
            observe.setUser(user);
            observe.setLot(lot);
            session.saveOrUpdate(observe);
//            session.saveOrUpdate(user);            
            session.getTransaction().commit();
        }
    }
    
    @Override
    public List<Integer> getObserverIdListByAuctionLot(AuctionLot lot) {
        List<Integer> list = new ArrayList<>();
        List<Observe> observes;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.getNamedQuery("Observe.findByLotId");
            query.setInteger("lotId", lot.getLotId());
            observes = query.list();
            for(Observe o:observes) {
                list.add(o.getId());
            }
        }
        return list;
    }
    
    @Override
    public void addAuctionLotIdListToObservableByUser(User user, List<Integer> lotIds) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
  
            AuctionLot lot; 
            Observe observe;
            for(Integer lotId : lotIds) {
                lot = lotRepository.getAuctionLotById(lotId);
                observe = new Observe();
                observe.setUser(user);
                observe.setLot(lot);
                session.saveOrUpdate(observe);
            }
            session.saveOrUpdate(user);
            
            session.getTransaction().commit();
        }
    }
    
}
