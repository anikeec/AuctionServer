/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entity;

import com.apu.auctionserver.DB.entity.AuctionLot;
import com.apu.auctionserver.DB.entity.User;
import com.apu.auctionserver.repository.LotRepository;
import com.apu.auctionserver.repository.ObserveRepository;
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
    private final ObserveRepository observeRepository = 
                        ObserveRepository.getInstance();
    
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
    
    public synchronized List<AuctionLot> getAuctionLots() {
        return lotRepository.getAuctionLots();
    }
    
    public synchronized AuctionLot getAuctionLotById(int lotId) {
        return lotRepository.getAuctionLotById(lotId);
    }
    
    public synchronized void addLotToAuction(AuctionLot lot) {
        lotRepository.saveAuctionLot(lot);
    }
    
    public synchronized void removeLotFromAuction(AuctionLot lot) {
        lotRepository.removeAuctionLot(lot);
    }
    
    public synchronized void updateAuctionLot(AuctionLot lot) {
        lotRepository.saveAuctionLot(lot);
    }
    
    public synchronized List<User> getAuctionUsers() {
        return userRepository.getAuctionUsers();
    }
    
    public synchronized User getAuctionUserById(int userId) {
        return userRepository.getUserById(userId);
    }
    
    public synchronized void addUserToAuction(User user) {
        userRepository.saveUser(user);
    }
    
    public synchronized void removeUserFromAuction(User user) {
        userRepository.removeUser(user);
    }
    
    public synchronized void updateUser(User user) {
        userRepository.saveUser(user);
    }
    
    public synchronized void addAuctionLotIdListToObservableByUser(User user, List<Integer> list) {
        observeRepository.addAuctionLotIdListToObservableByUser(user, list);
    }
    
    public synchronized List<AuctionLot> getObservableAuctionLotsByUser(User user) {
        return observeRepository.getObservableAuctionLotsByUser(user);
    }
    
    public synchronized void addAuctionLotToObservableByUser(User user, AuctionLot lot) {
        observeRepository.addAuctionLotToObservableByUser(user, lot);
    }
    
    public synchronized void clearObservableAuctionLotsByUser(User user) {
        observeRepository.clearObservableAuctionLotsByUser(user);
    }
    
    public synchronized List<Integer> getObserverIdListByAuctionLot(AuctionLot lot) {
        return observeRepository.getObserverIdListByAuctionLot(lot);
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
