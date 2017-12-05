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
import com.apu.auctionserver.entity.AuctionLot;
import com.apu.auctionserver.entity.User;
import com.apu.auctionserver.repository.LotRepository;
import com.apu.auctionserver.repository.UserRepository;
import com.apu.auctionserver.utils.Coder;
import com.apu.auctionserver.utils.Decoder;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.Socket;
import java.util.List;

/**
 *
 * @author apu
 */
public class Controller {
    
    private final UserRepository userRepository = UserRepository.getInstance();
    private final LotRepository lotRepository = LotRepository.getInstance();
    private final Decoder decoder = Decoder.getInstance();
    private final Coder coder = Coder.getInstance();

    private static Controller instance;
    
    private Controller() {
    }
    
    public static Controller getInstance() {
        if(instance == null)
            instance = new Controller();
        return instance;
    }
    
    public void handle(String queryStr, 
                        Socket socket,
                        BufferedReader in, 
                        BufferedWriter out) throws IOException, Exception {
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
            handle((RegistrationQuery)query, socket, in, out);
        } else {
            
        }
    }
    
    public void handle(DisconnectQuery query) {
        System.out.println("Disconnect query to controller");
        User user = userRepository.getUserById(query.getUserId());
        if(user != null) {
            userRepository.removeUser(user);
        } else {
            System.out.println("User unknown");
        }
    }
    
    public void handle(NewRateQuery query) {
        
    }
    
    public void handle(PingQuery query) throws IOException {
        System.out.println("Ping query to controller");
        User user = userRepository.getUserById(query.getUserId());
        if(user == null) {
            System.out.println("User unknown");
            return;
        }

        AnswerQuery answer = 
            new AnswerQuery(query.getPacketId(), 
                            user.getUserId(), 
                            "Ping answer");
        packetSend(user, answer);
    }
    
    public void handle(PollQuery query) throws IOException {
        System.out.println("Poll query to controller");
        User user = userRepository.getUserById(query.getUserId());
        if(user == null) {
            System.out.println("User unknown");
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
                                        lastRateUserId);
            answer.addLotToCollection(entity);
        }
        
        packetSend(user, answer);
    } 
    
    public void handle(RegistrationQuery query, 
                        Socket socket,
                        BufferedReader in, 
                        BufferedWriter out) throws IOException {
        System.out.println("Registration query to controller");
        User user = userRepository.getUserById(query.getUserId());
        if(user == null) {
            user = new User(query.getUserId(), socket, in, out);
            userRepository.addUser(user);
            
            AnswerQuery answer = 
                new AnswerQuery(query.getPacketId(), 
                                user.getUserId(),  
                                "Registration answer");
            packetSend(user, answer);
        } else {
            user.setSocket(socket);
            user.setIn(in);
            user.setOut(out);
        }
    } 
    
    public void handle(SubscribeQuery query) throws IOException {
        System.out.println("Subscribe query to controller");
        User user = userRepository.getUserById(query.getUserId());
        if(user != null) {
            int lotId = query.getLotId();
            user.addLotToObserved(lotRepository.getAuctionLotById(lotId));
            
            AnswerQuery answer = 
                new AnswerQuery(query.getPacketId(), 
                                user.getUserId(),  
                                "Subscribe answer");
            packetSend(user, answer);
        }
    }
    
    private void packetSend(User user, AuctionQuery answer) throws IOException {
        String str = coder.code(answer);
        user.getOut().write(str);
        user.getOut().flush();
    }
    
}
