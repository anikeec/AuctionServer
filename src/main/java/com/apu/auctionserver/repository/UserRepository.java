/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository;

import com.apu.auctionserver.DB.entity.User;
import java.util.List;

/**
 *
 * @author apu
 */
public interface UserRepository {

    List<User> getAuctionUsers();

    User getUserById(int userId);

    void removeUserById(int userId);

    void saveUser(User user);
    
    void updateUserByIdSetOnline(int userId);
    
    void updateUserByIdSetOffline(int userId);
    
    void updateUserAllSetStatus(String status);
    
}
