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
public interface UserStatusRepository {
    
    public static String USER_ONLINE = "online";
    public static String USER_OFFLINE = "offline";

    String getUserStatusByUserId(int id);
    void setUserStatus(int userId, String status);
    void updateUserByIdSetOnline(int id);
    void updateUserByIdSetOffline(int id);
    void updateUserAllSetOffline();
    
}
