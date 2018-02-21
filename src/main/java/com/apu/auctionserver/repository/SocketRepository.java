/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository;

/**
 *
 * @author apu
 */
public interface SocketRepository {
    
    Long getSocketIdByUserId(int id);
    void setSocketId(int userId, long socketId);
    Integer getUserIdBySocketId(long socketId);
    
}
