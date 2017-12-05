/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entity;

import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class Auction {
    private final List<AuctionLot> auctionLots = new ArrayList<>();
    private final List<User> auctionUsers = new ArrayList<>();
    
    private static Auction instance;
    
    private Auction() {
    }
    
    public static Auction getInstance() {
        if(instance == null)
            instance = new Auction();
        return instance;
    }
    
    public List<AuctionLot> getAuctionLots() {
        return auctionLots;
    }
    
    public void addLotToAuction(AuctionLot lot) {
        if(!auctionLots.contains(lot)) {
            auctionLots.add(lot);
        }
    }
    
    public void removeLotFromAuction(AuctionLot lot) {
        if(auctionLots.contains(lot)) {
            auctionLots.remove(lot);
        }
    }
    
    public List<User> getAuctionUsers() {
        return auctionUsers;
    }
    
    public void addUserToAuction(User user) {
        if(!auctionUsers.contains(user)) {
            auctionUsers.add(user);
        }
    }
    
    public void removeUserFromAuction(User user) {
        if(auctionUsers.contains(user)) {
            auctionUsers.remove(user);
        }
    }
    
}
