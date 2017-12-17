/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.jdbc;

import com.apu.auctionserver.DB.entity.AuctionLot;
import com.apu.auctionserver.repository.LotRepository;
import java.util.List;

/**
 *
 * @author apu
 */
public class LotRepositoryJDBC implements LotRepository {

    @Override
    public List<AuctionLot> getAuctionLots() {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public AuctionLot getAuctionLotById(int lotId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void removeAuctionLotById(int lotId) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public void saveAuctionLot(AuctionLot lot) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
