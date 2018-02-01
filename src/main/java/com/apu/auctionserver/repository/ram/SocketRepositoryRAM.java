/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.ram;

import com.apu.auctionserver.repository.SocketRepository;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author apu
 */
public class SocketRepositoryRAM implements SocketRepository {
    
    private static final Map<Integer, Long> sockets = new HashMap<>();
    private static SocketRepositoryRAM instance;
    
    public static SocketRepositoryRAM getInstance() {
        if(instance == null)
            instance = new SocketRepositoryRAM();
        return instance;
    }

    @Override
    public Long getSocketIdByUserId(int id) {
        return sockets.get(id);
    }

    @Override
    public void setSocketId(int userId, long socketId) {
        sockets.put(userId, socketId);
    }
    
}
