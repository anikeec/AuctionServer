/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entity;

import com.apu.auctionserver.DB.entity.AuctionLot;
import com.apu.auctionserver.DB.entity.User;
import com.apu.auctionserver.repository.hb.LotRepositoryH;
import com.apu.auctionserver.repository.hb.ObserveRepositoryH;
import com.apu.auctionserver.repository.hb.UserRepositoryH;
import java.util.List;
import com.apu.auctionserver.repository.LotRepository;
import com.apu.auctionserver.repository.ObserveRepository;
import com.apu.auctionserver.repository.UserRepository;

/**
 *
 * @author apu
 */
public class Auction implements AuctionI {
    private final LotRepository lotRepository = 
                        LotRepositoryH.getInstance();
    private final UserRepository userRepository = 
                        UserRepositoryH.getInstance();
    private final ObserveRepository observeRepository = 
                        ObserveRepositoryH.getInstance();
    
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
    
    @Override
    public synchronized List<AuctionLot> getAuctionLots() {
        return lotRepository.getAuctionLots();
    }
    
    @Override
    public synchronized AuctionLot getAuctionLotById(int lotId) {
        return lotRepository.getAuctionLotById(lotId);
    }
    
    @Override
    public synchronized void addLotToAuction(AuctionLot lot) {
        lotRepository.saveAuctionLot(lot);
    }
    
    @Override
    public synchronized void removeLotFromAuctionById(int lotId) {
        lotRepository.removeAuctionLotById(lotId);
    }
    
    @Override
    public synchronized void updateAuctionLot(AuctionLot lot) {
        lotRepository.saveAuctionLot(lot);
    }
    
    @Override
    public synchronized List<User> getAuctionUsers() {
        return userRepository.getAuctionUsers();
    }
    
    @Override
    public synchronized User getAuctionUserById(int userId) {
        return userRepository.getUserById(userId);
    }
    
    @Override
    public synchronized void addUserToAuction(User user) {
        userRepository.saveUser(user);
    }
    
    @Override
    public synchronized void removeUserFromAuctionById(int userId) {
        userRepository.removeUserById(userId);
    }
    
    @Override
    public synchronized void updateUser(User user) {
        userRepository.saveUser(user);
    }
    
    @Override
    public synchronized void addAuctionLotIdListToObservableByUser(User user, List<Integer> list) {
        observeRepository.addAuctionLotIdListToObservableByUser(user, list);
    }
    
    @Override
    public synchronized List<AuctionLot> getObservableAuctionLotsByUser(User user) {
        return observeRepository.getObservableAuctionLotsByUser(user);
    }
    
    @Override
    public synchronized void addAuctionLotToObservableByUser(User user, AuctionLot lot) {
        observeRepository.addAuctionLotToObservableByUser(user, lot);
    }
    
    @Override
    public synchronized void clearObservableAuctionLotsByUser(User user) {
        observeRepository.clearObservableAuctionLotsByUser(user);
    }
    
    @Override
    public synchronized List<Integer> getObserverIdListByAuctionLot(AuctionLot lot) {
        return observeRepository.getObserverIdListByAuctionLot(lot);
    }
    
    @Override
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
