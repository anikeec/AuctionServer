/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.nw.utils;

import com.apu.auctionapi.AuctionQuery;
import com.apu.auctionapi.query.NewRateQuery;
import com.apu.auctionapi.query.PingQuery;
import com.apu.auctionapi.query.PollQuery;
import com.apu.auctionapi.QueryType;
import com.apu.auctionapi.query.DisconnectQuery;
import com.apu.auctionapi.query.RegistrationQuery;
import com.apu.auctionapi.query.SubscribeQuery;
import com.apu.auctionserver.nw.exception.ErrorQueryException;
import com.apu.auctionserver.utils.Log;
import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class Decoder {
    
    private static final Log log = Log.getInstance();
    private final Class classname = Decoder.class;
    
    private final JsonParser parser = new JsonParser();
    private static Decoder instance;
    
    private JsonObject rootObject;
    
    private Decoder() {
    }
    
    public synchronized static Decoder getInstance() {
        if(instance == null)
            instance = new Decoder();
        return instance;
    }
    
    private synchronized void decode(RegistrationQuery result) throws Exception {
        log.debug(classname, "Registration packet");
        JsonArray array = rootObject.get("observableLotIdList").getAsJsonArray();
        Integer lotId;
        for(JsonElement element:array) {
            lotId = element.getAsInt();
            result.addLotIdToObservableList(lotId);
        }
    }
    
    private synchronized void decode(DisconnectQuery result)  throws Exception {
        log.debug(classname, "DisconnectQuery packet");        
    }
    
    private synchronized void decode(PingQuery result)  throws Exception {
        throw new Exception("Method has not ready yet");        
    }
    
    private synchronized void decode(NewRateQuery result)  throws Exception {
        log.debug(classname, "NewRateQuery packet"); 
        int lotId = rootObject.get("lotId").getAsInt();
        result.setLotId(lotId);
        int price = rootObject.get("price").getAsInt();
        result.setPrice(price);        
    }
    
//    private void decode(NotifyQuery result)  throws Exception {
//        throw new Exception("Method has not ready yet");        
//    }
    
    private synchronized void decode(PollQuery result)  throws Exception {
        log.debug(classname, "Poll packet");        
    }
    
    private synchronized void decode(SubscribeQuery result)  throws Exception {
        log.debug(classname, "Subscribe packet decode");
        int lotId = rootObject.get("lotId").getAsInt();
        result.setLotId(lotId);
    }
    
    public synchronized AuctionQuery decode(String query) throws ErrorQueryException {
        AuctionQuery result = null;
        try {
            JsonElement jsonElement = parser.parse(query);
            rootObject = jsonElement.getAsJsonObject();
            String queryType = rootObject.get("queryType").getAsString();
            String time = rootObject.get("time").getAsString();
            Long packetId = rootObject.get("packetId").getAsLong();
            Integer userId = rootObject.get("userId").getAsInt();       

            if(queryType.equals(QueryType.NEW_RATE.toString())) {            
                result = new NewRateQuery(packetId, userId, time);
                decode((NewRateQuery)result);            
            } else if(queryType.equals(QueryType.PING.toString())) {
                result = new PingQuery(packetId, userId, time);
                decode((PingQuery)result);
            } else if(queryType.equals(QueryType.POLL.toString())) {
                result = new PollQuery(packetId, userId, time);
                decode((PollQuery)result);
            } else if(queryType.equals(QueryType.DISCONNECT.toString())) {
                result = new DisconnectQuery(packetId, userId, time);
                decode((DisconnectQuery)result);
            } else if(queryType.equals(QueryType.REGISTRATION.toString())) {
                result = new RegistrationQuery(packetId, userId, time);
                decode((RegistrationQuery)result);
            } else if(queryType.equals(QueryType.SUBSCRIBE.toString())) {
                result = new SubscribeQuery(packetId, userId, time);
                decode((SubscribeQuery)result);
            }
        } catch (Exception ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
            throw new ErrorQueryException("Error decoding message", ex);
        }        
        return result;
    }
}
