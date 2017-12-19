/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.auction;

import com.apu.auctionserver.repository.entity.AuctionLot;
import com.apu.auctionserver.repository.entity.User;
import java.util.List;
import com.apu.auctionserver.repository.LotRepository;
import com.apu.auctionserver.repository.ObserveRepository;
import com.apu.auctionserver.repository.UserRepository;
import com.apu.auctionserver.repository.jdbc.LotRepositoryJDBC;
import com.apu.auctionserver.repository.jdbc.ObserveRepositoryJDBC;
import com.apu.auctionserver.repository.jdbc.UserRepositoryJDBC;

/**
 *
 * @author apu
 */
public class Auction implements AuctionI {
    private final LotRepository lotRepository = 
                        LotRepositoryJDBC.getInstance();
    private final UserRepository userRepository = 
                        UserRepositoryJDBC.getInstance();
    private final ObserveRepository observeRepository = 
                        ObserveRepositoryJDBC.getInstance();
    
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
    public List<AuctionLot> getAuctionLots() {
        return lotRepository.getAuctionLots();
    }
    
    @Override
    public AuctionLot getAuctionLotById(int lotId) {
        return lotRepository.getAuctionLotById(lotId);
    }
    
    @Override
    public void addLotToAuction(AuctionLot lot) {
        lotRepository.saveAuctionLot(lot);
    }
    
    @Override
    public void removeLotFromAuctionById(int lotId) {
        lotRepository.removeAuctionLotById(lotId);
    }
    
    @Override
    public void updateAuctionLot(AuctionLot lot) {
        lotRepository.saveAuctionLot(lot);
    }
    
    @Override
    public List<User> getAuctionUsers() {
        return userRepository.getAuctionUsers();
    }
    
    @Override
    public User getAuctionUserById(int userId) {
        return userRepository.getUserById(userId);
    }
    
    @Override
    public void addUserToAuction(User user) {
        userRepository.saveUser(user);
    }
    
    @Override
    public void removeUserFromAuctionById(int userId) {
        userRepository.removeUserById(userId);
    }
    
    @Override
    public void updateUser(User user) {
        userRepository.saveUser(user);
    }
    
    @Override
    public void addAuctionLotIdListToObservableByUser(User user, List<Integer> list) {
        observeRepository.addAuctionLotIdListToObservableByUser(user, list);
    }
    
    @Override
    public List<AuctionLot> getObservableAuctionLotsByUser(User user) {
        return observeRepository.getObservableAuctionLotsByUser(user);
    }
    
    @Override
    public void addAuctionLotToObservableByUser(User user, AuctionLot lot) {
        observeRepository.addAuctionLotToObservableByUser(user, lot);
    }
    
    @Override
    public void clearObservableAuctionLotsByUser(User user) {
        observeRepository.clearObservableAuctionLotsByUser(user);
    }
    
    @Override
    public List<Integer> getObserverIdListByAuctionLot(AuctionLot lot) {
        return observeRepository.getObserverIdListByAuctionLot(lot);
    }
    
    @Override
    public void init() {
//        initAuction();
        initUsers();
    }
    
    private void initAuction() {
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
    
    private void initUsers() {
        updateUserAllSetStatus(USER_OFFLINE);
    }

    @Override
    public void updateUserByIdSetOnline(int userId) {
        userRepository.updateUserByIdSetOnline(userId);
    }

    @Override
    public void updateUserByIdSetOffline(int userId) {
        userRepository.updateUserByIdSetOffline(userId);
    }

    @Override
    public void updateUserAllSetStatus(String status) {
        userRepository.updateUserAllSetStatus(status);
    }
    
}
