/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository;

import com.apu.auctionserver.entity.User;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class UserRepository {

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
        if(!users.contains(user))
            users.add(user);
    }
    
    public List<User> getAuctionUsers() {
        return users;
    }
    
    public void removeUser(User user) {
        if(users.contains(user))
            users.remove(user);
    }
    
    public User getUserById(int userId) {
        User ret = null;
        for(User u:users) {
            if(u.getUserId() == userId) return u;
        }
        return ret;
    }
    
}
