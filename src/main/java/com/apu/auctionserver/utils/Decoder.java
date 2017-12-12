/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.utils;

import com.apu.auctionapi.AuctionQuery;
import com.apu.auctionapi.query.NewRateQuery;
import com.apu.auctionapi.query.NotifyQuery;
import com.apu.auctionapi.query.PingQuery;
import com.apu.auctionapi.query.PollQuery;
import com.apu.auctionapi.QueryType;
import com.apu.auctionapi.query.RegistrationQuery;
import com.apu.auctionapi.query.SubscribeQuery;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

/**
 *
 * @author apu
 */
public class Decoder {
    
    private String query;
    private final JsonParser parser = new JsonParser();
    private static Decoder instance;
    
    private JsonElement jsonElement;
    private JsonObject rootObject;
    
    private Decoder() {
    }
    
    public static Decoder getInstance() {
        if(instance == null)
            instance = new Decoder();
        return instance;
    }
    
    private void decode(RegistrationQuery result) throws Exception {
        System.out.println("Registration packet");
        JsonArray array = rootObject.get("observableLotIdList").getAsJsonArray();
        Integer lotId;
        for(JsonElement element:array) {
            lotId = element.getAsInt();
            result.addLotIdToObservableList(lotId);
        }
    }
    
    private void decode(PingQuery result)  throws Exception {
        throw new Exception("Method has not ready yet");        
    }
    
    private void decode(NewRateQuery result)  throws Exception {
        System.out.println("NewRateQuery packet"); 
        int lotId = rootObject.get("lotId").getAsInt();
        result.setLotId(lotId);
        int price = rootObject.get("price").getAsInt();
        result.setPrice(price);        
    }
    
//    private void decode(NotifyQuery result)  throws Exception {
//        throw new Exception("Method has not ready yet");        
//    }
    
    private void decode(PollQuery result)  throws Exception {
        System.out.println("Poll packet");        
    }
    
    private void decode(SubscribeQuery result)  throws Exception {
        System.out.println("Subscribe packet decode");
        int lotId = rootObject.get("lotId").getAsInt();
        result.setLotId(lotId);
    }
    
    public AuctionQuery decode(String query) throws Exception {
        this.query = query;    

        jsonElement = parser.parse(query);
        rootObject = jsonElement.getAsJsonObject();
        String queryType = rootObject.get("queryType").getAsString();
        String time = rootObject.get("time").getAsString();
        Long packetId = rootObject.get("packetId").getAsLong();
        Integer userId = rootObject.get("userId").getAsInt();        
        
        AuctionQuery result = null;
        if(queryType.equals(QueryType.NEW_RATE.toString())) {
            result = new NewRateQuery(packetId, userId, time);
            Decoder.this.decode((NewRateQuery)result);
//        } else if(queryType.equals(QueryType.NOTIFY.toString())) {
//            Decoder.this.decode((NotifyQuery)result);
        } else if(queryType.equals(QueryType.PING.toString())) {
            result = new PingQuery(packetId, userId, time);
            Decoder.this.decode((PingQuery)result);
        } else if(queryType.equals(QueryType.POLL.toString())) {
            result = new PollQuery(packetId, userId, time);
            Decoder.this.decode((PollQuery)result);
        } else if(queryType.equals(QueryType.REGISTRATION.toString())) {
            result = new RegistrationQuery(packetId, userId, time);
            Decoder.this.decode((RegistrationQuery)result);
        } else if(queryType.equals(QueryType.SUBSCRIBE.toString())) {
            result = new SubscribeQuery(packetId, userId, time);
            Decoder.this.decode((SubscribeQuery)result);
        }
        
        return result;
    }
}
