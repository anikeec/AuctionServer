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
import com.apu.auctionapi.query.InternalQuery;
import com.apu.auctionapi.query.NotifyQuery;
import com.apu.auctionapi.query.PollQuery;
import com.apu.auctionapi.query.RegistrationQuery;
import com.apu.auctionapi.query.SubscribeQuery;
import com.apu.auctionserver.auction.Auction;
import com.apu.auctionserver.repository.entity.AuctionLot;
import com.apu.auctionserver.repository.entity.User;
import com.apu.auctionserver.nw.exception.ErrorQueryException;
import com.apu.auctionserver.nw.utils.Coder;
import com.apu.auctionserver.nw.utils.Decoder;
import com.apu.auctionserver.repository.SocketRepository;
import com.apu.auctionserver.repository.ram.SocketRepositoryRAM;
import com.apu.auctionserver.server.UserControlService;
import com.apu.auctionserver.utils.Log;
import com.apu.auctionserver.utils.Time;
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
    
    public String handle(String queryStr, long socketId) throws ErrorQueryException {
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
            answer = handle((RegistrationQuery)query, socketId);
        } else if(query instanceof InternalQuery) {
            answer = handle((InternalQuery)query);
        } else {
            answer = null;
        }
        return coder.code(answer);    
    }
    
    public AuctionQuery handle(InternalQuery query) { 
        log.debug(classname, "Internal query to controller");
        NotifyQuery answer = 
            new NotifyQuery(query.getPacketId(), query.getUserId());            
        answer.setLot(query.getLot());
        return answer;
    }
    
    public AnswerQuery handle(DisconnectQuery query) {
        log.debug(classname, "Disconnect query to controller");
        User user = auction.getAuctionUserById(query.getUserId());        
        AnswerQuery answer;
        if(user != null) {
            auction.clearObservableAuctionLotsByUser(user);
            user.setStatus(Auction.USER_OFFLINE);
//            auction.removeUserFromAuction(user);
            answer = new AnswerQuery(query.getPacketId(), 
                                        user.getUserId(), 
                                        "DisconnectQuery - OK. Disconnected");
            auction.updateUser(user);
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
            UserControlService.notifyService(user.getUserId());
            boolean auctionFinished = false;
            if(lot.getFinishDate() != null) {
                long finishTime = lot.getFinishDate().getTime();
                long currentTime = Time.getTimeMs();
                if(currentTime >= finishTime)
                        auctionFinished = true;
            } else {
                auctionFinished = true;
            }
            if(auctionFinished) {
                answer = new AnswerQuery(query.getPacketId(), 
                                        user.getUserId(), 
                        "NewRateQuery - Error. Auction has already finished.");
                return answer;
            }
            int price = query.getPrice();        
            if(price > lot.getLastRate()) {
                lot.setLastRate(price);                
                lot.setLastRateUser(user);
                auction.updateAuctionLot(lot);
                lot.notifyObservers();
                answer = new AnswerQuery(query.getPacketId(), 
                                        user.getUserId(), 
                                        "NewRateQuery - OK. Your rate is last.");
            } else {
                answer = new AnswerQuery(query.getPacketId(), 
                                        user.getUserId(), 
                                        "NewRateQuery - Error. Your rate is low.");
            }            
        } else if(user != null) {
            log.debug(classname, "Lot unknown");
            answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "NewRateQuery - Error. Lot unknown.");
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
            UserControlService.notifyService(user.getUserId());
            answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "Ping answer");
        } else {
            log.debug(classname, "User unknown");
            answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "Ping - Error. User unknown.");
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
            UserControlService.notifyService(user.getUserId());
            PollAnswerQuery answer = 
                new PollAnswerQuery(query.getPacketId(), 
                                    user.getUserId());

            //get list or Lots for current user
            List<AuctionLot> lots = auction.getObservableAuctionLotsByUser(user);
            User lastRateUser;
            int lastRateUserId;
            for(AuctionLot lot: lots) {
                lastRateUser = lot.getLastRateUser();    
                if(lastRateUser != null) 
                    lastRateUserId = lastRateUser.getUserId();
                else
                    lastRateUserId = 0;
                List<Integer> observers = 
                        auction.getObserverIdListByAuctionLot(lot);
                
                long timeToFinish = 0;
                if(lot.getFinishDate() != null) {
                    long finishTime = lot.getFinishDate().getTime();
                    long currentTime = Time.getTimeMs();
                    if(finishTime > currentTime)
                        timeToFinish = finishTime - currentTime;
                }                
            
                AuctionLotEntity entity = 
                        new AuctionLotEntity(lot.getLotId(),
                                            lot.getStartPrice(),
                                            lot.getLotName(),
                                            lot.getLastRate(),
                                            lastRateUserId,
                                            observers.size(),
                                            timeToFinish);
                answer.addLotToCollection(entity);
        }
        return answer;
        }         
    } 
    
    public AuctionQuery handle(RegistrationQuery query, long socketId) {
        log.debug(classname, "Registration query to controller");
        SocketRepository sr = SocketRepositoryRAM.getInstance();
        User user = auction.getAuctionUserById(query.getUserId());
        if(user == null) {
            user = new User(query.getUserId());
            user.setStatus(Auction.USER_ONLINE);
            sr.setSocketId(user.getUserId(), socketId);
            auction.addUserToAuction(user);            
        } else {
            auction.clearObservableAuctionLotsByUser(user);
            user.setStatus(Auction.USER_ONLINE);
            sr.setSocketId(user.getUserId(), socketId);
            auction.updateUser(user);
        }
//        socketRepository.addSocket(user, socket); 
        UserControlService.notifyService(user.getUserId());
        List<Integer> lotIdList = query.getObservableLotIdList();
        auction.addAuctionLotIdListToObservableByUser(user, lotIdList);
        AnswerQuery answer = 
                new AnswerQuery(query.getPacketId(), 
                                query.getUserId(),  
                                "Registration answer");
        return answer;
    } 
    
    public AuctionQuery handle(SubscribeQuery query) {
        log.debug(classname, "Subscribe query to controller");
        User user = auction.getAuctionUserById(query.getUserId());
        AnswerQuery answer;
        if(user != null) {
            UserControlService.notifyService(user.getUserId());
            int lotId = query.getLotId();
            AuctionLot lot = auction.getAuctionLotById(lotId);
            auction.addAuctionLotToObservableByUser(user, lot);
            answer = 
                new AnswerQuery(query.getPacketId(), 
                                query.getUserId(),  
                                "Subscribe answer");            
        } else {
            answer = new AnswerQuery(query.getPacketId(), 
                                        query.getUserId(), 
                                        "SubscribeQuery - Error. User unknown.");
        }
        return answer;
    }
    
    public static void main(String[] args) {
        RegistrationQuery query = new RegistrationQuery(1);
        query.addLotIdToObservableList(1);
//        new NetworkController().handle(query);
    }
    
}
