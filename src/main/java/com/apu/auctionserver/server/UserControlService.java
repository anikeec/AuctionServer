/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server;

import com.apu.auctionserver.auction.Auction;
import com.apu.auctionserver.repository.UserStatusRepository;
import com.apu.auctionserver.repository.ram.UserStatusRepositoryRAM;
import com.apu.auctionserver.utils.Time;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 *
 * @author apu
 */
public class UserControlService {
    
    private static final Map<Integer,Date> userLastQueryTime = new HashMap<>();
    private static final Auction auction = Auction.getInstance();
    private static final UserStatusRepository usr = 
                                UserStatusRepositoryRAM.getInstance();
    
    public static synchronized void notifyService(int userId) {
        if(!userLastQueryTime.containsKey(userId)) {
            usr.updateUserByIdSetOnline(userId);
        }
        userLastQueryTime.put(userId, new Date());            
    }
    
    public static synchronized void runChecking() {
        long curTime = new Date().getTime();
        int userId;
        long userLastQueryTimeValue;
        List<Integer> idsToRemove = new ArrayList<>();
        for(Map.Entry<Integer,Date> entry:userLastQueryTime.entrySet()) {
            userId = entry.getKey();
            userLastQueryTimeValue = entry.getValue().getTime();
            if((curTime - userLastQueryTimeValue) > Time.USER_TIMEOUT) {
                idsToRemove.add(userId);                
            }            
        }
        for(int id:idsToRemove) {
            usr.updateUserByIdSetOffline(id);
            userLastQueryTime.remove(id);
        }
    }
    
}
