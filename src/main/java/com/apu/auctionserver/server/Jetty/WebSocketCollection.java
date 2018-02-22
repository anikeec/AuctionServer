/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.Jetty;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

/**
 *
 * @author apu
 */
public class WebSocketCollection {
    
    private static WebSocketCollection instance;
    
    private final Map<WebSocketChannel, Long> webSockets = new HashMap<>();
    
    public static WebSocketCollection getInstance() {
        if(instance == null)
            instance = new WebSocketCollection();
        return instance;
    }
    
    public void put(Long socketId, WebSocketChannel socket) {
        webSockets.put(socket, socketId);
    }
    
    public Long put(WebSocketChannel socket) {
        Long id = webSockets.put(socket, null);
        if(id != null) return id;

        for(long idPtr=0; idPtr<Long.MAX_VALUE; idPtr++) {
            if(webSockets.containsValue(idPtr) == false) {
                webSockets.put(socket, idPtr);
                return idPtr;
            }
        }
        return null;
    }
    
    public List<WebSocketChannel> getChannelListBySocketId(long socketId) {
        List<WebSocketChannel> list = new ArrayList<>();
        Iterator<Entry<WebSocketChannel, Long>> it = 
                                        webSockets.entrySet().iterator();
        Entry<WebSocketChannel, Long> entry;
        while(it.hasNext()) {
            entry = it.next();
            if(entry.getValue() == socketId)
                list.add(entry.getKey());
        }
        return list;
    }
    
    public void remove(Long socketId) {
        Iterator<Entry<WebSocketChannel, Long>> it = 
                                        webSockets.entrySet().iterator();
        Entry<WebSocketChannel, Long> entry;
        while(it.hasNext()) {
            entry = it.next();
            Long value = entry.getValue();
            if((value != null) && (value.longValue() == socketId)) {
                entry.getKey().close();     //close socket before removing
                it.remove();
            }                
        }
    }
    
    public void remove(WebSocketChannel socket) {
        Long id = findIdBySocket(socket);
        if(id != null)
                WebSocketCollection.this.remove(id);
    }   
    
    public Long findIdBySocket(WebSocketChannel socket) {
        Iterator<Entry<WebSocketChannel, Long>> it = 
                                        webSockets.entrySet().iterator();
        Entry<WebSocketChannel, Long> entry;
        while(it.hasNext()) {
            entry = it.next();
            if(entry.getKey() == socket)
                return entry.getValue();
        }
        return null;
    }
}
