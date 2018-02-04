/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.NIO.msg;

/**
 *
 * @author apu
 */
public enum MsgType {
    REGISTRATION,
    PING,
    POLL,
    NEW_RATE,
    POLL_ANSWER,
    NOTIFY,
    DISCONNECT,
    SUBSCRIBE,
    LOAD_LOTS,
    LOAD_LOTS_ANSWER,
    INTERNAL
}
