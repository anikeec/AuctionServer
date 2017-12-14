/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.controller;

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
import com.apu.auctionserver.repository.SocketRepository;
import com.apu.auctionserver.utils.Coder;
import com.apu.auctionserver.utils.Decoder;
import com.apu.auctionserver.utils.Log;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author apu
 */
public class Controller {
    
    private static final Log log = Log.getInstance();
    private final Class classname = Controller.class;
    
    private final Auction auction = Auction.getInstance();
    private final Decoder decoder = Decoder.getInstance();
    private final Coder coder = Coder.getInstance();
    private final SocketRepository socketRepository = 
                            SocketRepository.getInstance();

    private static Controller instance;
    
    private Controller() {
    }
    
    public static Controller getInstance() {
        if(instance == null)
            instance = new Controller();
        return instance;
    }
    
    public void handle(String queryStr, 
                        Socket socket) throws IOException, Exception {
        AuctionQuery query = decoder.decode(queryStr);
                
        if(query instanceof DisconnectQuery) {
            handle((DisconnectQuery)query);
        } else if(query instanceof NewRateQuery) {
            handle((NewRateQuery)query);
        } else if(query instanceof PingQuery) { 
            handle((PingQuery)query);
        } else if(query instanceof PollQuery) { 
            handle((PollQuery)query);
        } else if(query instanceof RegistrationQuery) {
            handle((RegistrationQuery)query, socket);
        } else {
            
        }
    }
    
    public void handle(DisconnectQuery query) throws IOException {
        log.debug(classname, "Disconnect query to controller");
        User user = auction.getAuctionUserById(query.getUserId());        
        AnswerQuery answer;
        if(user != null) {
            user.eraseObservableList();
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
        packetSend(user, answer);
    }
    
    public synchronized void handle(NewRateQuery query) throws IOException {
        log.debug(classname, "NewRateQuery query to controller");
        AuctionLot lot = auction.getAuctionLotById(query.getLotId());
        User user = auction.getAuctionUserById(query.getUserId());
        if((lot != null)&& (user != null)) {
            int price = query.getPrice();            
            AnswerQuery answer;
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
            packetSend(user, answer);
        }
    }
    
    public void handle(PingQuery query) throws IOException {
        log.debug(classname, "Ping query to controller");
        User user = auction.getAuctionUserById(query.getUserId());
        if(user == null) {
            log.debug(classname, "User unknown");
            return;
        }

        AnswerQuery answer = 
            new AnswerQuery(query.getPacketId(), 
                            user.getUserId(), 
                            "Ping answer");
        packetSend(user, answer);
    }
    
    public void handle(PollQuery query) throws IOException {
        log.debug(classname, "Poll query to controller");
        User user = auction.getAuctionUserById(query.getUserId());
        if(user == null) {
            log.debug(classname, "User unknown");
            return;
        }      

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
        
        packetSend(user, answer);
    } 
    
    public void handle(RegistrationQuery query, 
                        Socket socket) throws IOException {
        log.debug(classname, "Registration query to controller");
        User user = auction.getAuctionUserById(query.getUserId());
        if(user == null) {
            user = new User(query.getUserId());            
        } else {
//            user.setSocket(socket);
        }
        socketRepository.addSocket(user, socket);
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
        packetSend(user, answer);
    } 
    
    public void handle(SubscribeQuery query) throws IOException {
        log.debug(classname, "Subscribe query to controller");
        User user = auction.getAuctionUserById(query.getUserId());
        if(user != null) {
            int lotId = query.getLotId();
            user.addLotToObserved(auction.getAuctionLotById(lotId));
            
            AnswerQuery answer = 
                new AnswerQuery(query.getPacketId(), 
                                user.getUserId(),  
                                "Subscribe answer");
            packetSend(user, answer);
        }
    }
    
    private void packetSend(User user, AuctionQuery answer) throws IOException {
        String str = coder.code(answer);        
        OutputStream os = 
                socketRepository.getSocketByUser(user).getOutputStream();
        BufferedWriter out = new BufferedWriter(new OutputStreamWriter(os));
        out.write(str);
        out.flush();
    }
    
}
