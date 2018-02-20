/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server.Jetty;

import com.apu.auctionserver.auction.controller.AuctionControllerPool;
import com.apu.auctionserver.server.NIO.message.Message;
import com.apu.auctionserver.server.Server;
import com.apu.auctionserver.server.nw.controller.NwInputController;
import com.apu.auctionserver.server.nw.controller.NwOutputController;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import java.util.LinkedList;
import java.util.Queue;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class ServerJetty implements Server {
    
    private final Log log = Log.getInstance();
    private final Class classname = ServerJetty.class;
    
    private final int serverPort;
    private final int backlog;
    
    private final Queue<Message> outboundMessageQueue = new LinkedList<>();

    public ServerJetty(int serverPort, int backlog) {
        this.serverPort = serverPort;
        this.backlog = backlog;
    }

    @Override
    public void accept() throws IOException {
        AuctionControllerPool acPool = new AuctionControllerPool();
        
        NwInputController niController = 
                new NwInputController(acPool.getInputMsgQueue(),
                                        acPool.getOutputMsgQueue());
        
        WriteProxyJetty writeProxy = 
                new WriteProxyJetty(this.outboundMessageQueue);
        
        NwOutputController noController = 
                new NwOutputController(acPool.getOutputMsgQueue(), 
                                        writeProxy);
        
        Thread outputThread = new Thread(
                        new JettyOutputThread(this.outboundMessageQueue));
        outputThread.setDaemon(true);
               
        Thread nwOutputThread = new Thread(noController);
        nwOutputThread.setDaemon(true);
        
        acPool.init();
        outputThread.start();
        nwOutputThread.start();   
        
        try {
            org.eclipse.jetty.server.Server serverJetty =
                    new org.eclipse.jetty.server.Server(serverPort);
            serverJetty.setHandler(new ServerWebSocketHandler());
            serverJetty.start();
            serverJetty.join();
        } catch (Exception ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
    }
    
}
