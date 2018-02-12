/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.msg;

import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author apu
 */
public class Msg {
    private int userId;
    private MsgType msgType;
    private final Map<MsgParameter, Object> params;

    public Msg(MsgType msgType, int userId) {
        this.params = new HashMap<>();
        this.msgType = msgType;
        this.userId = userId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public MsgType getMsgType() {
        return msgType;
    }

    public void setMsgType(MsgType msgType) {
        this.msgType = msgType;
    }
    
    public Object getParameter(MsgParameter param) {
        return params.get(param);
    }
    
    public void setParameter(MsgParameter param, Object value) {
        params.put(param, value);
    }
    
}
