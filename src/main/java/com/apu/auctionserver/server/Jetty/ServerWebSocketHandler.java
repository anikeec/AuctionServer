/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.Jetty;

import javax.servlet.http.HttpServletRequest;
import org.eclipse.jetty.websocket.WebSocket;
import org.eclipse.jetty.websocket.WebSocketHandler;

/**
 *
 * @author apu
 */
public class ServerWebSocketHandler extends WebSocketHandler {

    @Override
    public WebSocket doWebSocketConnect(HttpServletRequest hsr, String string) {
        return new WebSocketChannel();
    }
    
}
