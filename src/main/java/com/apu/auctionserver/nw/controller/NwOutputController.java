/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.nw.controller;

import com.apu.auctionapi.AuctionLotEntity;
import com.apu.auctionapi.AuctionQuery;
import com.apu.auctionapi.answer.AnswerQuery;
import com.apu.auctionapi.answer.PollAnswerQuery;
import com.apu.auctionapi.query.NotifyQuery;
import com.apu.auctionserver.nw.utils.Coder;
import com.apu.auctionserver.server.NIO.WriteProxy;
import com.apu.auctionserver.server.NIO.message.Message;
import com.apu.auctionserver.server.NIO.msg.Msg;
import com.apu.auctionserver.server.NIO.msg.MsgParameter;
import com.apu.auctionserver.utils.Log;
import java.util.List;
import java.util.concurrent.BlockingQueue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class NwOutputController implements Runnable {
    
    private static final Log log = Log.getInstance();
    private final Class classname = NwOutputController.class;
    
    private final Coder coder = Coder.getInstance();   
    
    private final BlockingQueue<Msg> outputMessageQueue;
    private final WriteProxy writeProxy;

    public NwOutputController(BlockingQueue<Msg> outputMessageQueue, 
                                WriteProxy writeProxy) {
        this.outputMessageQueue = outputMessageQueue;
        this.writeProxy = writeProxy;
        instance = this;
    }  
    
    private static NwOutputController instance;
    
    public static NwOutputController getInstance() {
//        if(instance == null)
//            instance = new NwOutputController();
        return instance;
    }
//    
//    private NwOutputController() {}
//    
//    public void setOutputMsgQueue(BlockingQueue<Msg> queue) {
//        this.outputMessageQueue = queue;
//    }
//    
//    public void setWriteProxy(WriteProxy proxy) {
//        this.writeProxy = proxy;
//    }
    
    public synchronized void handle(Msg msg) {
        
        AuctionQuery answer = null;
        Message message = new Message();
        switch(msg.getMsgType()) {
            case NOTIFY:
                        answer = handleNotifyQuery(msg);
                        break;
            case POLL_ANSWER:
                        answer = handlePollAnswerQuery(msg);
                        break;
            default:
                        break;
        }
        String answerStr = coder.code(answer);
        message.socketId = (long)msg.getParameter(MsgParameter.SOCKET_ID);
        message.writeToMessage(answerStr.getBytes());
        sendMessage(message);
    }
    
    private AuctionQuery handleNotifyQuery(Msg msg) {
        NotifyQuery query = new NotifyQuery(0, msg.getUserId());
        AuctionLotEntity entity = new AuctionLotEntity(
                        (int)msg.getParameter(MsgParameter.LOT_ID),
                        (int)msg.getParameter(MsgParameter.START_PRICE),
                        (String)msg.getParameter(MsgParameter.NAME_STRING),
                        (int)msg.getParameter(MsgParameter.LAST_RATE),
                        (int)msg.getParameter(MsgParameter.USER_ID),
                        (int)msg.getParameter(MsgParameter.OBSERVERS_LIST_SIZE),
                        (long)msg.getParameter(MsgParameter.TIME_TO_FINISH));
        query.setLot(entity);
        return query;
    }
    
    private AuctionQuery handlePollAnswerQuery(Msg msg) {
        PollAnswerQuery query = new PollAnswerQuery(0, msg.getUserId());        
        List<Msg> messages = 
                    (List<Msg>)msg.getParameter(MsgParameter.LOT_LIST_OF_MSGS);
        for(Msg message:messages) {
            AuctionLotEntity entity = new AuctionLotEntity(
                        (int)message.getParameter(MsgParameter.LOT_ID),
                        (int)message.getParameter(MsgParameter.START_PRICE),
                        (String)message.getParameter(MsgParameter.NAME_STRING),
                        (int)message.getParameter(MsgParameter.LAST_RATE),
                        (int)message.getParameter(MsgParameter.USER_ID),
                        (int)message.getParameter(MsgParameter.OBSERVERS_LIST_SIZE),
                        (long)message.getParameter(MsgParameter.TIME_TO_FINISH));
            query.addLotToCollection(entity);
        }
        return query;
    }
    
    public void sendMessage(Message message) {
        log.debug(classname,"Write to output: " + message.getMessageStr());
        this.writeProxy.enqueue(message);
    }

    @Override
    public void run() {
        Msg msg;
        while(!Thread.currentThread().isInterrupted()) {
            msg = null;
            try {
                msg = outputMessageQueue.take();                
            } catch (InterruptedException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
            if(msg == null) continue;
            handle(msg);
        }
    }
    
}
