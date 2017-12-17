/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository;

import com.apu.auctionserver.DB.entity.AuctionLot;
import com.apu.auctionserver.DB.entity.User;
import java.util.List;

/**
 *
 * @author apu
 */
public interface ObserveRepository {
    
    List<AuctionLot> getObservableAuctionLotsByUser(User user);

    List<Integer> getObserverIdListByAuctionLot(AuctionLot lot);

    void addAuctionLotIdListToObservableByUser(User user, List<Integer> lotIds);

    void addAuctionLotToObservableByUser(User user, AuctionLot lot);

    void clearObservableAuctionLotsByUser(User user);    
    
}
