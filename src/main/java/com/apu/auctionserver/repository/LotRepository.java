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

    AuctionLot getAuctionLotById(int lotId);

    List<AuctionLot> getAuctionLots();

    void removeAuctionLot(AuctionLot lot);

    void saveAuctionLot(AuctionLot lot);
    
}
