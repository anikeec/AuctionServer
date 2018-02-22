/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.auction.controller;

import com.apu.auctionserver.auction.Auction;
import com.apu.auctionserver.repository.interfaces.Observer;
import com.apu.auctionserver.repository.SocketRepository;
import com.apu.auctionserver.repository.UserStatusRepository;
import com.apu.auctionserver.repository.entity.AuctionLot;
import com.apu.auctionserver.repository.entity.User;
import com.apu.auctionserver.repository.ram.SocketRepositoryRAM;
import com.apu.auctionserver.repository.ram.UserStatusRepositoryRAM;
import com.apu.auctionserver.msg.Msg;
import com.apu.auctionserver.msg.MsgParameter;
import com.apu.auctionserver.msg.MsgType;
import com.apu.auctionserver.server.UserControlService;
import java.util.concurrent.BlockingQueue;
import com.apu.auctionserver.utils.Log;
import com.apu.auctionserver.utils.Time;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class AuctionController implements Runnable {
    
    private final Log log = Log.getInstance();
    private final Class classname = AuctionController.class;
    
    private final Auction auction = Auction.getInstance();
    private final UserStatusRepository usr = 
                            UserStatusRepositoryRAM.getInstance();
    private final SocketRepository socketRepository = 
                                SocketRepositoryRAM.getInstance();

    private final BlockingQueue<Msg> inputMessageQueue;
    private final BlockingQueue<Msg> outputMessageQueue;    
    

    public AuctionController(BlockingQueue<Msg> inputMessageQueue, 
                                BlockingQueue<Msg> outputMessageQueue) {
        this.inputMessageQueue = inputMessageQueue;
        this.outputMessageQueue = outputMessageQueue;
    }
      
    @Override
    public void run() {
        Msg message;
        while(!Thread.currentThread().isInterrupted()) {
            message = null;
            try {
                message = inputMessageQueue.take();                
            } catch (InterruptedException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
            if(message == null) continue;
            handle(message);
        }
    }
    
    private void handle(Msg message) {
        switch(message.getMsgType()) {
            case REGISTRATION:
                                handleRegistrationQuery(message);
                                break;
            case DISCONNECT:
                                handleDisconnectQuery(message);
                                break;
            case NEW_RATE:
                                handleNewRateQuery(message);
                                break;
            case POLL:
                                handlePollQuery(message);
                                break;
            case PING:
                                handlePingQuery(message);
                                break;
            case LOAD_LOTS:
                                handleLoadLotsQuery(message);
                                break;
            case SUBSCRIBE:
                                handleSubscribeQuery(message);
                                break;
            default:
                                
                                break;
        }
    }
    
    private void handleRegistrationQuery(Msg message) {
        int userId = message.getUserId();
        long socketId = (long)message.getParameter(MsgParameter.SOCKET_ID);
        List<Integer> lotIdList = 
            (List<Integer>)message.getParameter(MsgParameter.OBSERVABLE_LIST);
        SocketRepository sr = SocketRepositoryRAM.getInstance();
        User user = auction.getAuctionUserById(userId);        
        if(user == null) {
            user = new User(userId);                //here must be login-passw checking
            usr.updateUserByIdSetOnline(userId);
            sr.setSocketId(userId, socketId);
            auction.addUserToAuction(user);            
        } else {
            auction.clearObservableAuctionLotsByUser(user);
            usr.updateUserByIdSetOnline(userId);
            sr.setSocketId(userId, socketId);
            auction.updateUser(user);
        }
        UserControlService.notifyService(user.getUserId());        
        auction.addAuctionLotIdListToObservableByUser(user, lotIdList);
    }
    
    private void handleDisconnectQuery(Msg message) {
        int userId = message.getUserId();
        User user = auction.getAuctionUserById(userId);        
        if(user != null) {
            auction.clearObservableAuctionLotsByUser(user);
            usr.updateUserByIdSetOffline(userId);
            auction.updateUser(user);
        } else {
            log.debug(classname, "User unknown");
            //ToDo - maybe I have to send message about it
        }
    }
    
    private void handleNewRateQuery(Msg message) { 
        int userId = message.getUserId();
        int lotId = (int)message.getParameter(MsgParameter.LOT_ID);
        int price = (int)message.getParameter(MsgParameter.PRICE);
        //we have to create Msg that contain notify user list and give this msg to....
        AuctionLot lot = auction.getAuctionLotById(lotId);
        User user = auction.getAuctionUserById(userId);
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
//                answer = new AnswerQuery(query.getPacketId(), 
//                                        user.getUserId(), 
//                        "NewRateQuery - Error. Auction has already finished.");
//                return answer;
                return;
            }        
            if(price > lot.getLastRate()) {
                lot.setLastRate(price);                
                lot.setLastRateUser(user);
                auction.updateAuctionLot(lot);
//                lot.notifyObservers();                                          //!!!!!
                notifyObservers(lot);
//                answer = new AnswerQuery(query.getPacketId(), 
//                                        user.getUserId(), 
//                                        "NewRateQuery - OK. Your rate is last.");
            } else {
//                answer = new AnswerQuery(query.getPacketId(), 
//                                        user.getUserId(), 
//                                        "NewRateQuery - Error. Your rate is low.");
            }            
        } else if(user != null) {
            log.debug(classname, "Lot unknown");
//            answer = new AnswerQuery(query.getPacketId(), 
//                                        query.getUserId(), 
//                                        "NewRateQuery - Error. Lot unknown.");
        } else {
            log.debug(classname, "User unknown");
//            answer = new AnswerQuery(query.getPacketId(), 
//                                        query.getUserId(), 
//                                        "NewRateQuery - Error. User unknown.");
        }
    }
    
    private void handlePollQuery(Msg message) {
        int userId = message.getUserId();
        User user = auction.getAuctionUserById(userId);
        if(user == null) {
            log.debug(classname, "User unknown");
//            AnswerQuery answer = new AnswerQuery(query.getPacketId(), 
//                                        query.getUserId(), 
//                                        "PollQuery - Error. User unknown.");
//            return answer;
        } else {    
            UserControlService.notifyService(userId);
            Msg msgOut = new Msg(MsgType.POLL_ANSWER, userId);
            //get list or Lots for current user
            List<AuctionLot> lots = auction.getObservableAuctionLotsByUser(user);
            User lastRateUser;
            int lastRateUserId;
            List<Msg> messages = new ArrayList<>();
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
                
                Msg msgInner = new Msg(MsgType.NOTIFY, userId);
                msgInner.setParameter(MsgParameter.LOT_ID, lot.getLotId());
                msgInner.setParameter(MsgParameter.START_PRICE, lot.getStartPrice());
                msgInner.setParameter(MsgParameter.NAME_STRING, lot.getLotName());
                msgInner.setParameter(MsgParameter.LAST_RATE, lot.getLastRate());
                msgInner.setParameter(MsgParameter.USER_ID, lot.getLastRateUser().getUserId());
                msgInner.setParameter(MsgParameter.OBSERVERS_LIST_SIZE, lot.getObserverList().size());
                msgInner.setParameter(MsgParameter.TIME_TO_FINISH, timeToFinish);
                messages.add(msgInner);
            }
            msgOut.setParameter(MsgParameter.LOT_LIST_OF_MSGS, messages);
            
            sendMessage(msgOut);
        }         
    }
    
    private void notifyObservers(AuctionLot lot) {
        User user;
        String status;
        for(Observer o:lot.getObserverList()) {
            user = (User)o;
            status = usr.getUserStatusByUserId(user.getUserId());
            if((status==null)||
                (status.equals(UserStatusRepository.USER_OFFLINE))) {
                continue;
            }
            long timeToFinish = 
                    lot.getFinishDate().getTime() - Time.getTimeMs();
            Msg msgOut = new Msg(MsgType.NOTIFY, user.getUserId());
            msgOut.setParameter(MsgParameter.LOT_ID, lot.getLotId());
            msgOut.setParameter(MsgParameter.START_PRICE, lot.getStartPrice());
            msgOut.setParameter(MsgParameter.NAME_STRING, lot.getLotName());
            msgOut.setParameter(MsgParameter.LAST_RATE, lot.getLastRate());
            msgOut.setParameter(MsgParameter.USER_ID, lot.getLastRateUser().getUserId());
            msgOut.setParameter(MsgParameter.OBSERVERS_LIST_SIZE, lot.getObserverList().size());
            msgOut.setParameter(MsgParameter.TIME_TO_FINISH, timeToFinish);       

            sendMessage(msgOut);
        }
    }
    
    private void handlePingQuery(Msg message) {
        
    }
    
    private void handleLoadLotsQuery(Msg message) {
        int userId = message.getUserId();
        User user = auction.getAuctionUserById(userId);
        if(user != null) {    
            UserControlService.notifyService(userId);
            Msg msgOut = new Msg(MsgType.LOAD_LOTS_ANSWER, userId);
            List<AuctionLot> lots = auction.getAuctionLots();
            List<Integer> observableList = new ArrayList<>();
            for(AuctionLot lot: lots) {                
                observableList.add(lot.getLotId());
            }
            msgOut.setParameter(MsgParameter.OBSERVABLE_LIST, observableList);            
            sendMessage(msgOut);
        }               
    }
    
    private void handleSubscribeQuery(Msg message) {
        int userId = message.getUserId();
        Integer lotId = (Integer)message.getParameter(MsgParameter.LOT_ID);
        User user = auction.getAuctionUserById(userId);        
        if(user != null) {
            auction.addAuctionLotToObservableByUser(user, 
                                auction.getAuctionLotById(lotId));
            UserControlService.notifyService(userId);
        }               
    }
    
    private void sendMessage(Msg message) {        
        Long socketId = socketRepository.getSocketIdByUserId(message.getUserId());
        message.setParameter(MsgParameter.SOCKET_ID, socketId);
        if(socketId != null) {
            try {
                outputMessageQueue.put(message);
            } catch (InterruptedException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
