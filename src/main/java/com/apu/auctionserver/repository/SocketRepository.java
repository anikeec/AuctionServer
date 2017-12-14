/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository;

import com.apu.auctionserver.entity.User;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;

/**
 *
 * @author apu
 */
public class SocketRepository {
    Map<User,Socket> sockets = new HashMap<>();
    UserRepository usreRepository = UserRepository.getInstance();
    
    private static SocketRepository instance;
    
    private SocketRepository() {
    }
    
    public static SocketRepository getInstance() {
        if(instance == null)
            instance = new SocketRepository();
        return instance;
    }
    
    public Socket getSocketByUser(User user) {
        if(sockets.containsKey(user))
            return sockets.get(user);
        return null;
    }
    
    public Socket getSocketByUserId(int userId) {
        User user = usreRepository.getUserById(userId);
        if(sockets.containsKey(user))
            return sockets.get(user);
        return null;
    }
    
    public void addSocket(User user, Socket socket) {
        sockets.put(user, socket);
    }
    
}
