/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.hb;

import com.apu.auctionserver.DB.HibernateSessionFactory;
import com.apu.auctionserver.DB.entity.User;
import com.apu.auctionserver.repository.UserRepository;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author apu
 */
public class UserRepositoryH implements UserRepository {
    private final SessionFactory sessionFactory = 
            HibernateSessionFactory.getSessionFactory();
    private static UserRepositoryH instance;
    
    private UserRepositoryH() {
    }
    
    public static UserRepositoryH getInstance() {
        if(instance == null)
            instance = new UserRepositoryH();
        return instance;
    }
    
    @Override
    public void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
        }      
    }
    
    @Override
    public List<User> getAuctionUsers() {
        List<User> list;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.getNamedQuery("User.findAll");
            list = query.list();
        }
        return list;
    }
    
    @Override
    public void removeUserById(int userId) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            User user = session.load(User.class, userId);
            session.delete(user);
            session.getTransaction().commit();
        }
    }
    
    @Override
    public User getUserById(int userId) {
        User ret;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.getNamedQuery("User.findByUserId");
            query.setInteger("userId", userId);
            ret = (User) query.uniqueResult();
//            ret = session.load(User.class, userId);
        }
        return ret;
    } 

    @Override
    public void updateUserByIdSetOnline(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateUserByIdSetOffline(int userId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void updateUserAllSetStatus(String status) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
