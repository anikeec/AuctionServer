/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.nw;

import com.apu.auctionapi.answer.AnswerQuery;
import com.apu.auctionapi.AuctionLotEntity;
import com.apu.auctionapi.AuctionQuery;
import com.apu.auctionapi.query.DisconnectQuery;
import com.apu.auctionapi.query.NewRateQuery;
import com.apu.auctionapi.query.PingQuery;
import com.apu.auctionapi.answer.PollAnswerQuery;
import com.apu.auctionapi.query.PollQuery;
import com.apu.auctionapi.query.RegistrationQuery;
import com.apu.auctionapi.query.SubscribeQuery;
import com.apu.auctionserver.entity.Auction;
import com.apu.auctionserver.entity.AuctionLot;
import com.apu.auctionserver.entity.User;
import com.apu.auctionserver.nw.exception.ErrorQueryException;
import com.apu.auctionserver.nw.utils.Coder;
import com.apu.auctionserver.nw.utils.Decoder;
import com.apu.auctionserver.utils.Log;
import java.util.List;

/**
 *
 * @author apu
 */
public class NetworkController {
    
    private static final Log log = Log.getInstance();
    private final Class classname = NetworkController.class;
    
    private final Auction auction = Auction.getInstance();
    private final Decoder decoder = Decoder.getInstance();
    private final Coder coder = Coder.getInstance();    
    
    public String handle(String queryStr) throws ErrorQueryException {
        AuctionQuery query = decoder.decode(queryStr);
        AuctionQuery answer;
        
        if(query instanceof DisconnectQuery) {
            answer = handle((DisconnectQuery)query);
        } else if(query instanceof NewRateQuery) {
            answer = handle((NewRateQuery)query);
        } else if(query instanceof PingQuery) { 
            answer = handle((PingQuery)query);
        } else if(query instanceof PollQuery) { 
            answer = handle((PollQuery)query);
        } else if(query instanceof RegistrationQuery) {
            answer = handle((RegistrationQuery)query);
        } else {
            answer = null;
        }
        return coder.code(answer);    
    }
    
    public AnswerQuery handle(DisconnectQuery query) {
        log.debug(classname, "Disconnect query to controller");
        User user = auction.getAuctionUserById(query.getUserId());        
        AnswerQuery answer;
        if(user != null) {
            user.eraseObservableList();
            user.setStatus(User.STATUS.OFFLINE);
            auction.removeUserFromAuction(user);
            answer = new AnswerQuery(query.getPacketId(), 
                                        user.getUserId(), 
                                        "DisconnectQuery - OK. Disconnected");   
        } else {
            log.debug(classname, "User unknown");
            answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "DisconnectQuery - Error. User unknown.");
        }
        return answer;
    }
    
    public AuctionQuery handle(NewRateQuery query) {
        log.debug(classname, "NewRateQuery query to controller");
        AuctionLot lot = auction.getAuctionLotById(query.getLotId());
        User user = auction.getAuctionUserById(query.getUserId());
        AnswerQuery answer;
        if((lot != null)&& (user != null)) {
            int price = query.getPrice();        
            if(price > lot.getLastRate()) {
                lot.setLastRate(price);                
                lot.setLastRateUser(user);
                auction.updateAuctionLot(lot);
                answer = new AnswerQuery(query.getPacketId(), 
                                        user.getUserId(), 
                                        "NewRateQuery - OK. Your rate is last.");
            } else {
                answer = new AnswerQuery(query.getPacketId(), 
                                        user.getUserId(), 
                                        "NewRateQuery - Error. Your rate is low.");
            }            
        } else {
            log.debug(classname, "User unknown");
            answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "NewRateQuery - Error. User unknown.");
        }
        return answer;
    }
    
    public AuctionQuery handle(PingQuery query) {
        log.debug(classname, "Ping query to controller");
        User user = auction.getAuctionUserById(query.getUserId());
        AnswerQuery answer;
        if(user != null) {
            answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "Ping answer");
        } else {
            log.debug(classname, "User unknown");
            answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "NewRateQuery - Error. User unknown.");
        }        
        return answer;
    }
    
    public AuctionQuery handle(PollQuery query) {
        log.debug(classname, "Poll query to controller");
        User user = auction.getAuctionUserById(query.getUserId());
        if(user == null) {
            log.debug(classname, "User unknown");
            AnswerQuery answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "PollQuery - Error. User unknown.");
            return answer;
        } else {     

        PollAnswerQuery answer = 
            new PollAnswerQuery(query.getPacketId(), 
                                user.getUserId());
        
        //get list or Lots for current user
        List<AuctionLot> lots = user.getObservedLots();
        User lastRateUser;
        int lastRateUserId;
        for(AuctionLot lot: lots) {
            lastRateUser = lot.getLastRateUser();
            if(lastRateUser != null) 
                lastRateUserId = lastRateUser.getUserId();
            else
                lastRateUserId = 0;
            AuctionLotEntity entity = 
                    new AuctionLotEntity(lot.getLotId(),
                                        lot.getStartPrice(),
                                        lot.getLotName(),
                                        lot.getLastRate(),
                                        lastRateUserId,
                                        lot.getAmountObservers());
            answer.addLotToCollection(entity);
        }
        return answer;
        }         
    } 
    
    public AuctionQuery handle(RegistrationQuery query) {
        log.debug(classname, "Registration query to controller");
        User user = auction.getAuctionUserById(query.getUserId());
        if(user == null) {
            user = new User(query.getUserId());            
        }
//        socketRepository.addSocket(user, socket);
        user.setStatus(User.STATUS.ONLINE);
        List<Integer> lotIdList = query.getObservableLotIdList();
        AuctionLot lot;
        user.eraseObservableList();
        for(Integer lotId : lotIdList) {
            lot = auction.getAuctionLotById(lotId);
            user.addLotToObserved(lot);
        }
        auction.addUserToAuction(user);     
        AnswerQuery answer = 
                new AnswerQuery(query.getPacketId(), 
                                user.getUserId(),  
                                "Registration answer");
        return answer;
    } 
    
    public AuctionQuery handle(SubscribeQuery query) {
        log.debug(classname, "Subscribe query to controller");
        User user = auction.getAuctionUserById(query.getUserId());
        AnswerQuery answer;
        if(user != null) {
            int lotId = query.getLotId();
            user.addLotToObserved(auction.getAuctionLotById(lotId));
            
            answer = 
                new AnswerQuery(query.getPacketId(), 
                                user.getUserId(),  
                                "Subscribe answer");            
        } else {
            answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "SubscribeQuery - Error. User unknown.");
        }
        return answer;
    }
    
    
    
}
