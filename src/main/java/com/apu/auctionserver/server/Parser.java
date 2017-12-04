/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server;

import com.apu.auctionapi.AuctionQuery;
import com.apu.auctionapi.NewRateQuery;
import com.apu.auctionapi.PingQuery;
import com.apu.auctionapi.PollQuery;
import com.apu.auctionapi.QueryType;
import com.apu.auctionapi.RegistrationQuery;
import org.json.JSONObject;

/**
 *
 * @author apu
 */
public class Parser {
    
    public static RegistrationQuery parseRegistration(String query) throws Exception {
        throw new Exception("Method has not ready yet");
    }
    
    public static PingQuery parsePing(String query)  throws Exception {
        throw new Exception("Method has not ready yet");        
    }
    
    public static NewRateQuery parseNewRate(String query)  throws Exception {
        throw new Exception("Method has not ready yet");        
    }
    
    public static PollQuery parsePoll(String query)  throws Exception {
        throw new Exception("Method has not ready yet");        
    }
    
    public static AuctionQuery parse(String query) throws Exception {
        AuctionQuery result = null;
        
        JSONObject obj = new JSONObject(result);
        String queryType = obj.getString("queryType");
        if(queryType.equals(QueryType.REGISTRATION.toString())) {
            return parseRegistration(queryType);
        }
        
        return result;
    }
}
