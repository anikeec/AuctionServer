/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository;

import com.apu.auctionserver.DB.HibernateSessionFactory;
import com.apu.auctionserver.DB.entity.User;
import java.util.ArrayList;
import java.util.List;
import org.hibernate.Query;
import org.hibernate.Session;
import org.hibernate.SessionFactory;

/**
 *
 * @author apu
 */
public class UserRepository {
    private SessionFactory sessionFactory = HibernateSessionFactory.getSessionFactory();
    private static UserRepository instance;
    
    private UserRepository() {
    }
    
    public static UserRepository getInstance() {
        if(instance == null)
            instance = new UserRepository();
        return instance;
    }
    
    public void saveUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.saveOrUpdate(user);
            session.getTransaction().commit();
        }      
    }
    
    public List<User> getAuctionUsers() {
        List<User> list;
        try (Session session = sessionFactory.openSession()) {
            Query query = session.getNamedQuery("User.findAll");
            list = query.list();
        }
        return list;
    }
    
    public void removeUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.delete(user);
            session.getTransaction().commit();
        }
    }
    
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
    
}
