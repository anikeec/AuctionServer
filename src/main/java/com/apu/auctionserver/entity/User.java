/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entity;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.net.Socket;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class User {
    private final int userId;
    private Socket socket;
    private final List<AuctionLot> observedLots = new ArrayList<>();

    public User(int userId) {
        this(userId, null);
    }    
    
    public User(int userId, Socket socket) {
        this.userId = userId;
        this.socket = socket;
    }

    public int getUserId() {
        return userId;
    }

    public Socket getSocket() {
        return socket;
    }

    public void setSocket(Socket socket) {
        this.socket = socket;
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
