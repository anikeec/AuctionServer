/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entity;

import com.apu.auctionserver.DB.entity.AuctionLot;
import com.apu.auctionserver.DB.entity.User;
import java.util.List;

/**
 *
 * @author apu
 */
public interface AuctionI {

    void addAuctionLotIdListToObservableByUser(User user, List<Integer> list);

    void addAuctionLotToObservableByUser(User user, AuctionLot lot);

    void addLotToAuction(AuctionLot lot);

    void addUserToAuction(User user);

    void clearObservableAuctionLotsByUser(User user);

    AuctionLot getAuctionLotById(int lotId);

    List<AuctionLot> getAuctionLots();

    User getAuctionUserById(int userId);

    List<User> getAuctionUsers();

    List<AuctionLot> getObservableAuctionLotsByUser(User user);

    List<Integer> getObserverIdListByAuctionLot(AuctionLot lot);

    void init();

    void removeLotFromAuctionById(int lotId);

    void removeUserFromAuctionById(int userId);

    void updateAuctionLot(AuctionLot lot);

    void updateUser(User user);
    
}
