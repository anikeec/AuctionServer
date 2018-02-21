/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.Jetty;

import com.apu.auctionapi.query.DisconnectQuery;
import com.apu.auctionserver.repository.SocketRepository;
import com.apu.auctionserver.repository.ram.SocketRepositoryRAM;
import com.apu.auctionserver.server.nw.controller.NwInputController;
import com.apu.auctionserver.server.nw.exception.ErrorQueryException;
import com.apu.auctionserver.server.nw.utils.Coder;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import org.apache.commons.lang3.exception.ExceptionUtils;
import org.eclipse.jetty.websocket.WebSocket;

/**
 *
 * @author apu
 */
public class WebSocketChannel implements WebSocket.OnTextMessage {
    
    private final Log log = Log.getInstance();
    private final Class classname = WebSocketChannel.class;
    
    private Connection connection;
    private long socketId;
    private final WebSocketCollection socketCollection = 
                            WebSocketCollection.getInstance();
    private final SocketRepository socketRepository = 
                            SocketRepositoryRAM.getInstance();
    private NwInputController nwInputController;

    @Override
    public synchronized void onMessage(String query) {
        log.debug(classname, "Input query: " + query);
        nwInputController = NwInputController.getInstance();
        try {
            nwInputController.handle(query, this.socketId);
        } catch (ErrorQueryException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }    
        
    }

    @Override
    public synchronized void onOpen(Connection cnctn) {
        this.connection = cnctn;
        this.socketId = socketCollection.put(this);
        log.debug(classname, "Client is connected. SocketId: " + this.socketId);
    }

    @Override
    public synchronized void onClose(int i, String string) {
        log.debug(classname, "Client is disconnected. SocketId: " + this.socketId);        
        Integer userId = socketRepository.getUserIdBySocketId(this.socketId);    //it is wrong way
        if(userId != null) {
            DisconnectQuery query = new DisconnectQuery(0, userId, "");
            String queryStr = Coder.getInstance().code(query);
            this.onMessage(queryStr);
        }
        socketCollection.remove(this);
    }
    
    public synchronized void sendTextMessage(String message) {
        try {
            connection.sendMessage(message);
        } catch (IOException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
    }
    
}
