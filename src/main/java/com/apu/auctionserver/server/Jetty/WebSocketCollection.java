/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.Jetty;

import java.util.HashMap;
import java.util.Iterator;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author apu
 */
public class WebSocketCollection {
    
    private static WebSocketCollection instance;
    
    private final Map<Long, WebSocketChannel> webSockets = new HashMap<>();
    
    public static WebSocketCollection getInstance() {
        if(instance == null)
            instance = new WebSocketCollection();
        return instance;
    }
    
    public WebSocketChannel get(long socketId) {
        return webSockets.get(socketId);
    }
    
    public void put(Long socketId, WebSocketChannel socket) {
        webSockets.put(socketId, socket);
    }
    
    public long put(WebSocketChannel socket) {
        Long id = findIdBySocket(socket);
        if(id != null)  
            return id;
        for(long idPtr=0; idPtr<Long.MAX_VALUE; idPtr++) {
            if(webSockets.containsKey(idPtr) == false) {
                webSockets.put(idPtr, socket);
                return idPtr;
            }
        }
        webSockets.put(Long.MAX_VALUE - 1, socket);
        return Long.MAX_VALUE - 1;                                  //error
    }
    
    public void remove(Long socketId) {
        webSockets.remove(socketId);
    }
    
    public void remove(WebSocketChannel socket) {
        Long id = findIdBySocket(socket);
        if(id != null)
                remove(id);
    }   
    
    public Long findIdBySocket(WebSocketChannel socket) {
        Iterator<Entry<Long,WebSocketChannel>> it = 
                                        webSockets.entrySet().iterator();
        Entry<Long,WebSocketChannel> entry;
        while(it.hasNext()) {
            entry = it.next();
            if(entry.getValue() == socket)
                return entry.getKey();
        }
        return null;
    }
}
