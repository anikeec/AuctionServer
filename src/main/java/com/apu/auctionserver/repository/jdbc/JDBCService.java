/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.jdbc;

import com.apu.auctionserver.utils.Log;
import java.io.FileInputStream;
import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Properties;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class JDBCService {
    
    private static final Log log = Log.getInstance();
    private static final Class classname = JDBCService.class;
    private static Connection connection;
    private static Statement statement;
    private static JDBCService instance;
    
    private JDBCService() {
    }
    
    public static JDBCService getInstance() {
        if(instance == null)
            instance = new JDBCService();
        return instance;
    }
    
    private static String loadProperty(String propertyName) throws IOException {
        Properties props = new Properties();
        try (FileInputStream fis = 
                new FileInputStream("src/main/resources/config.properties")) {
            props.load(fis);
        }
        return props.getProperty(propertyName, "");        
    }

    public synchronized Connection getConnection() {
        return connection;
    }

    public synchronized Statement getStatement() {
        return statement;
    }
    
    public synchronized Connection dbConnect() 
                    throws IOException, ClassNotFoundException, SQLException  {
        connection = null;
//        try {
            String dbDriver = loadProperty("DB_DRIVER");
            String dbConnectionUrl = loadProperty("DB_CONNECTION_URL");
            String dbUser = loadProperty("DB_USER");
            String dbPassword = loadProperty("DB_PASSWORD");
            String dbName = loadProperty("DB_NAME");
            
            Class.forName(dbDriver);
            
            connection = DriverManager.getConnection(
                    dbConnectionUrl + dbName, dbUser, dbPassword);        
            statement = connection.createStatement();
//        } catch (SQLException | IOException | ClassNotFoundException ex) {
//            log.debug(classname,ExceptionUtils.getStackTrace(ex));
//        }
        return connection;
    }
    
    public synchronized void dbDisconnect() throws SQLException {
        if(statement != null) 
                statement.close();
        if(connection != null) 
                connection.close();
    }
}