/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.jdbc;

import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Deque;
import java.util.LinkedList;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class JDBCPool {
    private static final int MAX_CONNECTIONS = 20;
    private static final Deque<Connection> connectionsStack = new LinkedList<>();
    private static final JDBCService dbService = JDBCService.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = JDBCPool.class;
    private static JDBCPool instance;    
    
    private JDBCPool() {
    }
    
    public static JDBCPool getInstance() {
        if(instance == null) {
            for(int i=0;i<MAX_CONNECTIONS;i++) {
                try {
                    connectionsStack.add(dbService.dbConnect());
                } catch (IOException | ClassNotFoundException | SQLException ex) {
                    log.debug(classname,ExceptionUtils.getStackTrace(ex));
                }
            }
            instance = new JDBCPool();
        }
        return instance;
    }
    
    public synchronized Connection getConnection() {
        while(connectionsStack.isEmpty()) {
            try {
                wait();
            } catch (InterruptedException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
        }
        Connection con = connectionsStack.pollFirst();
//        log.debug(classname,"Get connection. Id: " + con.toString());
        return con;
    }
    
    public synchronized void putConnection(Connection con) {
//        log.debug(classname,"Put connection. Id: " + con.toString());
        connectionsStack.addFirst(con);
        notifyAll();
    }    
    
}
