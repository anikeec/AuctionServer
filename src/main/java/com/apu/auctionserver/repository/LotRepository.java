/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository;

import com.apu.auctionserver.DB.entity.AuctionLot;
import java.util.List;

/**
 *
 * @author apu
 */
public interface LotRepository {
    
    List<AuctionLot> getAuctionLots();

    AuctionLot getAuctionLotById(int lotId);    

    void removeAuctionLotById(int lotId);

    void saveAuctionLot(AuctionLot lot);
    
}
