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
    private final List<User> users = new ArrayList<>();
    private static UserRepository instance;
    
    private UserRepository() {
    }
    
    public static UserRepository getInstance() {
        if(instance == null)
            instance = new UserRepository();
        return instance;
    }
    
    public void addUser(User user) {
        try (Session session = sessionFactory.openSession()) {
            session.beginTransaction();
            session.save(user);
            session.getTransaction().commit();
            session.close();
        }      
//        if(!users.contains(user))
//            users.add(user);
    }
    
    public List<User> getAuctionUsers() {
        return users;
    }
    
    public void removeUser(User user) {
        if(users.contains(user))
            users.remove(user);
    }
    
    public User getUserById(int userId) {
        User ret;
        try (Session session = sessionFactory.openSession()) {
//            session.beginTransaction();
            Query query = session.getNamedQuery("User.findByUserId");
            query.setInteger("userId", userId);
            ret = (User) query.uniqueResult();
//            ret = null;
//            for(User u:users) {
//                if(u.getUserId() == userId) return u;
//            }   
//          session.save(lot);
//            session.getTransaction().commit();
            session.close();
        }
        return ret;
    }
    
    public void updateUser(User user) {
        
    }
    
}
