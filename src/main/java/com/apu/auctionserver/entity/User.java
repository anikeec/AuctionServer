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
public class User {
    public static enum STATUS{ONLINE, OFFLINE};
    private final int userId;
    private final List<AuctionLot> observedLots = new ArrayList<>();
    private STATUS status;

    public User(int userId) {
        this.userId = userId;
    }    

    public int getUserId() {
        return userId;
    }

    public STATUS getStatus() {
        return status;
    }

    public void setStatus(STATUS status) {
        this.status = status;
    }
    
    public List<AuctionLot> getObservedLots() {
        return observedLots;
    }
    
    public void addLotToObserved(AuctionLot lot) {
        if(!observedLots.contains(lot)) {
            observedLots.add(lot);
            lot.addUserToObservers(this);
        }
    }
    
    public void removeLotFromObserved(AuctionLot lot) {
        if(observedLots.contains(lot)) {
            observedLots.remove(lot);
            lot.removeUserFromObservers(this);
        }
    }
    
    public void eraseObservableList() {
        for(AuctionLot lot : observedLots) {
            lot.removeUserFromObservers(this);
        }
        observedLots.clear();
    }
    
}
