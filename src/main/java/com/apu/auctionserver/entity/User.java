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
    private BufferedReader in;
    private BufferedWriter out;
    private final List<AuctionLot> observedLots = new ArrayList<>();

    public User(int userId) {
        this(userId, null, null, null);
    }    
    
    public User(int userId, Socket socket, BufferedReader in, BufferedWriter out) {
        this.userId = userId;
        this.socket = socket;
        this.in = in;
        this.out = out;
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

    public BufferedReader getIn() {
        return in;
    }

    public void setIn(BufferedReader in) {
        this.in = in;
    }

    public BufferedWriter getOut() {
        return out;
    }

    public void setOut(BufferedWriter out) {
        this.out = out;
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
