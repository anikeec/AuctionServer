/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entity;

/**
 *
 * @author apu
 */
public class AuctionLot {
    private int lotId;
    private int startPrice;
    private String lotName;
    private int lastRate;
    private int lastRateUserId;

    public AuctionLot(int lotId, int startPrice, String lotName) {
        this.lotId = lotId;
        this.startPrice = startPrice;
        this.lotName = lotName;
    }

    public int getLotId() {
        return lotId;
    }

    public void setLotId(int lotId) {
        this.lotId = lotId;
    }

    public int getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(int startPrice) {
        this.startPrice = startPrice;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public int getLastRate() {
        return lastRate;
    }

    public void setLastRate(int lastRate) {
        this.lastRate = lastRate;
    }

    public int getLastRateUserId() {
        return lastRateUserId;
    }

    public void setLastRateUserId(int lastRateUserId) {
        this.lastRateUserId = lastRateUserId;
    }
    
    
}
