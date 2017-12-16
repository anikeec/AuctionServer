/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entity;

import com.apu.auctionserver.DB.entity.AuctionLot;
import com.apu.auctionserver.DB.entity.User;
import com.apu.auctionserver.repository.LotRepository;
import com.apu.auctionserver.repository.UserRepository;
import java.util.List;

/**
 *
 * @author apu
 */
public class Auction {
    private final LotRepository lotRepository = 
                        LotRepository.getInstance();
    private final UserRepository userRepository = 
                        UserRepository.getInstance();
    
    public static String USER_ONLINE = "online";
    public static String USER_OFFLINE = "offline";
    
    private static Auction instance;
    
    private Auction() {
    }
    
    public static Auction getInstance() {
        if(instance == null)
            instance = new Auction();
        return instance;
    }
    
    public List<AuctionLot> getAuctionLots() {
        return lotRepository.getAuctionLots();
    }
    
    public AuctionLot getAuctionLotById(int lotId) {
        return lotRepository.getAuctionLotById(lotId);
    }
    
    public void addLotToAuction(AuctionLot lot) {
        lotRepository.addAuctionLot(lot);
    }
    
    public void removeLotFromAuction(AuctionLot lot) {
        lotRepository.removeAuctionLot(lot);
    }
    
    public void updateAuctionLot(AuctionLot lot) {
        lotRepository.updateAuctionLot(lot);
    }
    
    public List<User> getAuctionUsers() {
        return userRepository.getAuctionUsers();
    }
    
    public User getAuctionUserById(int userId) {
        return userRepository.getUserById(userId);
    }
    
    public void addUserToAuction(User user) {
        userRepository.addUser(user);
    }
    
    public void removeUserFromAuction(User user) {
        userRepository.removeUser(user);
    }
    
    public void updateUser(User user) {
        userRepository.updateUser(user);
    }
    
    public void init() {
        AuctionLot lot1, lot2;
        User user;
        
        lot1 = new AuctionLot(1);
        lot1.setStartPrice(10);
        lot1.setLotName("Book");
        addLotToAuction(lot1);
        lot2 = new AuctionLot(2);
        lot2.setStartPrice(25);
        lot2.setLotName("TVset");
        addLotToAuction(lot2);         
    }
    
}
