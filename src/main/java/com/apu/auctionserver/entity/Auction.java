/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entity;

import com.apu.auctionserver.repository.LotRepository;
import com.apu.auctionserver.repository.UserRepository;
import java.util.ArrayList;
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
    
    public void addLotToAuction(AuctionLot lot) {
        lotRepository.addAuctionLot(lot);
    }
    
    public void removeLotFromAuction(AuctionLot lot) {
        lotRepository.removeAuctionLot(lot);
    }
    
    public List<User> getAuctionUsers() {
        return userRepository.getAuctionUsers();
    }
    
    public void addUserToAuction(User user) {
        userRepository.addUser(user);
    }
    
    public void removeUserFromAuction(User user) {
        userRepository.removeUser(user);
    }
    
}
