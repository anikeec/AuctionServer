/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository;

import com.apu.auctionserver.entity.AuctionLot;
import java.util.ArrayList;
import java.util.List;

/**
 *
 * @author apu
 */
public class LotRepository {
    private final List<AuctionLot> auctionLots = new ArrayList<>();
    private static LotRepository instance;
    
    private LotRepository() {
    }
    
    public static LotRepository getInstance() {
        if(instance == null)
            instance = new LotRepository();
        return instance;
    }
    
    public void addAuctionLot(AuctionLot lot) {
        if(!auctionLots.contains(lot))
            auctionLots.add(lot);
    }
    
    public AuctionLot getAuctionLotById(int lotId) {
        AuctionLot ret = null;
        
        return ret;
    }
}
