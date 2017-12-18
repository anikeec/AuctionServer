/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server;

import com.apu.auctionserver.auction.Auction;
import com.apu.auctionserver.utils.Time;
import java.util.Date;
import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author apu
 */
public class UserControlService {
    
    private static final Map<Integer,Date> userLastQueryTime = new HashMap<>();
    private static final Auction auction = Auction.getInstance();
    
    public static synchronized void notifyService(int userId) {
        if(!userLastQueryTime.containsKey(userId)) {
            auction.updateUserByIdSetOnline(userId);
        }
        userLastQueryTime.put(userId, new Date());            
    }
    
    public static synchronized void runChecking() {
        long curTime = new Date().getTime();
        int userId;
        long userLastQueryTimeValue;
        for(Map.Entry<Integer,Date> entry:userLastQueryTime.entrySet()) {
            userId = entry.getKey();
            userLastQueryTimeValue = entry.getValue().getTime();
            if((curTime - userLastQueryTimeValue) > Time.USER_TIMEOUT) {
                auction.updateUserByIdSetOffline(userId);
                userLastQueryTime.remove(userId);
            }            
        }
    }
    
}
