/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.ram;

import com.apu.auctionserver.repository.SocketRepository;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

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

    @Override
    public Integer getUserIdBySocketId(long socketId) {
        if(sockets.containsValue(socketId) == false)
            return null;
        Iterator<Entry<Integer, Long>> it = sockets.entrySet().iterator();
        Entry<Integer, Long> entry;
        while(it.hasNext()) {
            entry = it.next();
            if(entry.getValue() == socketId)
                return entry.getKey();
        }
        return null;
    }
    
}
