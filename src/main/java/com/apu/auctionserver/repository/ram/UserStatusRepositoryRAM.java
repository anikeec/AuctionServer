/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.ram;

import com.apu.auctionserver.repository.UserStatusRepository;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;

/**
 *
 * @author apu
 */
public class UserStatusRepositoryRAM implements UserStatusRepository {
    
    private static final Map<Integer, String> userStatus = new HashMap<>();
    private static UserStatusRepositoryRAM instance;
    
    public static UserStatusRepositoryRAM getInstance() {
        if(instance == null)
            instance = new UserStatusRepositoryRAM();
        return instance;
    }

    @Override
    public synchronized String getUserStatusByUserId(int id) {
        return userStatus.get(id);
    }

    @Override
    public synchronized void setUserStatus(int userId, String status) {
        userStatus.put(userId, status);
    }

    @Override
    public synchronized void updateUserByIdSetOnline(int id) {
        setUserStatus(id, USER_ONLINE);
    }

    @Override
    public synchronized void updateUserByIdSetOffline(int id) {
        setUserStatus(id, USER_OFFLINE);
    }

    @Override
    public synchronized void updateUserAllSetOffline() {
        Iterator it = userStatus.entrySet().iterator();
        Map.Entry<Integer, String> entry;
        while(it.hasNext()) {
            entry = (Map.Entry<Integer, String>)it.next();
            entry.setValue(USER_OFFLINE);
        }
    }
}
