/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.nw.controller;

import com.apu.auctionapi.answer.AnswerQuery;
import com.apu.auctionapi.AuctionQuery;
import com.apu.auctionapi.query.DisconnectQuery;
import com.apu.auctionapi.query.NewRateQuery;
import com.apu.auctionapi.query.PingQuery;
import com.apu.auctionapi.query.InternalQuery;
import com.apu.auctionapi.query.PollQuery;
import com.apu.auctionapi.query.RegistrationQuery;
import com.apu.auctionapi.query.SubscribeQuery;
import com.apu.auctionserver.server.nw.exception.ErrorQueryException;
import com.apu.auctionserver.server.nw.utils.Coder;
import com.apu.auctionserver.server.nw.utils.Decoder;
import com.apu.auctionserver.server.NIO.message.Message;
import com.apu.auctionserver.msg.Msg;
import com.apu.auctionserver.msg.MsgParameter;
import com.apu.auctionserver.msg.MsgType;
import com.apu.auctionserver.utils.Log;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class NwInputController {
    
    private static final Log log = Log.getInstance();
    private final Class classname = NwInputController.class;    
    
    private final Decoder decoder = Decoder.getInstance();
    private final Coder coder = Coder.getInstance();    
    private NwOutputController nwOutputController;

    private final BlockingQueue<Msg> inputMessageQueue;
    private final BlockingQueue<Msg> outputMessageQueue; 

    public NwInputController(BlockingQueue<Msg> inputMessageQueue, 
                                    BlockingQueue<Msg> outputMessageQueue) {
        this.inputMessageQueue = inputMessageQueue;
        this.outputMessageQueue = outputMessageQueue;
        instance = this;
    }    
    
    private static NwInputController instance;
    
    public static NwInputController getInstance() {
//        if(instance == null)
//            instance = new NwInputController();
        return instance;
    }
    
    public void handle(String queryStr, long socketId) throws ErrorQueryException {
        AuctionQuery query = decoder.decode(queryStr);
        
        AuctionQuery answer = 
                new AnswerQuery(query.getPacketId(), query.getUserId(), "OK");
        String answerStr = coder.code(answer);
        
        Message answerMessage = new Message();
        answerMessage.socketId = socketId;
        answerMessage.writeToMessage(answerStr.getBytes());
        nwOutputController = NwOutputController.getInstance();
        nwOutputController.sendMessage(answerMessage);
        
        if(query instanceof RegistrationQuery) {
            handle((RegistrationQuery)query, socketId);
        } else if(query instanceof DisconnectQuery) {
            handle((DisconnectQuery)query);
        } else if(query instanceof NewRateQuery) {
            handle((NewRateQuery)query);
        } else if(query instanceof PingQuery) { 
            handle((PingQuery)query);
        } else if(query instanceof PollQuery) { 
            handle((PollQuery)query);
        } else if(query instanceof InternalQuery) {
            handle((InternalQuery)query);
        }   
    }
    
    public void handle(RegistrationQuery query, long socketId) {
        log.debug(classname, "Registration query to controller");
        Msg msg = new Msg(MsgType.REGISTRATION, query.getUserId());
        List<Integer> observList = query.getObservableLotIdList();
        msg.setParameter(MsgParameter.SOCKET_ID, socketId);
        msg.setParameter(MsgParameter.OBSERVABLE_LIST, observList);
        handle(msg);
    } 
    
    public void handle(DisconnectQuery query) {
        log.debug(classname, "Disconnect query to controller");
        Msg msg = new Msg(MsgType.DISCONNECT, query.getUserId());
        handle(msg);
    }
    
    public void handle(NewRateQuery query) {
        log.debug(classname, "NewRateQuery query to controller");
        Msg msg = new Msg(MsgType.NEW_RATE, query.getUserId());        
        msg.setParameter(MsgParameter.LOT_ID, query.getLotId());
        msg.setParameter(MsgParameter.PRICE, query.getPrice());
        handle(msg);
    }
    
    public void handle(PollQuery query) {
        log.debug(classname, "Poll query to controller");
        Msg msg = new Msg(MsgType.POLL, query.getUserId());        
        handle(msg);
    }
    
    public void handle(PingQuery query) {
        log.debug(classname, "Ping query to controller");

    }  
    
    public void handle(SubscribeQuery query) {
        log.debug(classname, "Subscribe query to controller");
        
    }
        
    public void handle(InternalQuery query) { 
        log.debug(classname, "Internal query to controller");

    }
    
    private void handle(Msg msg) {
        try {
            inputMessageQueue.put(msg);
        } catch (InterruptedException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
    }
    
}
