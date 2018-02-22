/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.ram;

import com.apu.auctionserver.repository.SocketRepository;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author apu
 */
public class SocketRepositoryRAM implements SocketRepository {
    
    private static final Map<Long, Integer> sockets = new HashMap<>();
    private static SocketRepositoryRAM instance;
    
    public static SocketRepositoryRAM getInstance() {
        if(instance == null)
            instance = new SocketRepositoryRAM();
        return instance;
    }

    @Override
    public Long getSocketIdByUserId(int id) {
        if(sockets.containsValue(id) == false)
            return null;
        Iterator<Entry<Long, Integer>> it = sockets.entrySet().iterator();
        Entry<Long, Integer> entry;
        while(it.hasNext()) {
            entry = it.next();
            if(entry.getValue() == id)
                return entry.getKey();
        }
        return null;
    }

    @Override
    public void setSocketId(int userId, long socketId) {
        sockets.put(socketId, userId);
    }

    @Override
    public Integer getUserIdBySocketId(long socketId) {
        return sockets.get(socketId);
    }

    @Override
    public List<Long> getSocketIdListByUserId(int id) {
        List<Long> list = new ArrayList<>();
        
        if(sockets.containsValue(id) == false) 
            return list;
        
        Iterator<Entry<Long, Integer>> it = sockets.entrySet().iterator();
        Entry<Long, Integer> entry;
        while(it.hasNext()) {
            entry = it.next();
            if(entry.getValue() == id)
                list.add(entry.getKey());
        }    
        
        return list;
    }
    
}
