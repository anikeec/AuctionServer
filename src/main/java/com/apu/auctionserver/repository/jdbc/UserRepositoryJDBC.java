/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.jdbc;

import com.apu.auctionserver.repository.entity.User;
import com.apu.auctionserver.auction.Auction;
import com.apu.auctionserver.repository.UserRepository;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class UserRepositoryJDBC implements UserRepository {
    
    private static UserRepositoryJDBC instance;
    private static JDBCPool dbPool = JDBCPool.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = UserRepositoryJDBC.class;
    
    String findByIdString =
        "select * from USER where user.user_id = ?"; 
    String findAllString =
        "select * from USER"; 
    String insertString = 
        " insert into USER(user_id,login,passw_hash,status,socket_id) "
            + "values(?,?,?,?,?);";
    String updateString = 
        " update USER set login = ?," +
        " passw_hash = ?," +
        " status = ?," +
        " socket_id = ? " +
        " where user_id = ? ";    
    String removeString =
        "delete from USER where user.user_id = ?";
    String updateStatusString = 
        " update USER set status = ? " +
        " where user_id = ? ";
    String updateAllStatusString = 
        " update USER set status = ? ";
    
    private UserRepositoryJDBC() {
    }
    
    public static UserRepositoryJDBC getInstance() {
        if(instance == null)
            instance = new UserRepositoryJDBC();
        return instance;
    }
    
    @Override
    public List<User> getAuctionUsers() {
        Connection con = null;
        PreparedStatement findStatement = null;
        List<User> userList = new ArrayList<>();
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(findAllString);
                log.debug(classname, findStatement.toString());
                ResultSet rs = findStatement.executeQuery();
                User user;
                while(rs.next()) {
                    user = new User(rs.getInt("user_id"));
                    user.setLogin(rs.getString("login"));
                    user.setPasswHash(rs.getString("passw_hash"));
                    user.setStatus(rs.getString("status"));
                    user.setSocketId(rs.getLong("socket_id"));
                    userList.add(user);
                }
            } finally {
                if (findStatement != null) {
                    findStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
            return null;
        } finally {
            dbPool.putConnection(con);
        }
        return userList;
    }

    @Override
    public User getUserById(int userId) {
        Connection con = null;
        PreparedStatement findStatement = null;
        User user = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(findByIdString);
                findStatement.setInt(1, userId);
                log.debug(classname, findStatement.toString());
                ResultSet rs = findStatement.executeQuery();
                while(rs.next()) {
                    user = new User(rs.getInt("user_id"));
                    user.setLogin(rs.getString("login"));
                    user.setPasswHash(rs.getString("passw_hash"));
                    user.setStatus(rs.getString("status"));
                    user.setSocketId(rs.getLong("socket_id"));
                }
            } finally {
                if (findStatement != null) {
                    findStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
            return null;
        } finally {
            dbPool.putConnection(con);
        }
        return user;
    }

    @Override
    public void removeUserById(int userId) {
        Connection con = null;
        PreparedStatement removeStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                removeStatement = con.prepareStatement(removeString);
                removeStatement.setInt(1, userId);
                log.debug(classname, removeStatement.toString());
                removeStatement.executeUpdate();
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (removeStatement != null) {
                    removeStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        } finally {
            dbPool.putConnection(con);
        }
    }

    @Override
    public void saveUser(User user) {
        Connection con = null;
        PreparedStatement findStatement = null;
        PreparedStatement insertStatement = null;
        PreparedStatement updateStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(findByIdString);
                findStatement.setInt(1, user.getUserId());
                log.debug(classname, findStatement.toString());
                ResultSet rs = findStatement.executeQuery();
                String str;
                Integer intValue;
                Long longValue;
                if(!rs.next()) {
                    insertStatement = con.prepareStatement(insertString);
                    intValue = user.getUserId();
                    if(intValue == null) 
                        intValue = 0;    
                    insertStatement.setInt(1, intValue);
                    str = user.getLogin();
                    if(str == null) 
                        str = "";
                    insertStatement.setString(2, str);
                    str = user.getPasswHash();
                    if(str == null) 
                        str = "";
                    insertStatement.setString(3, str);
                    str = user.getStatus();
                    if(str == null) 
                        str = "";
                    insertStatement.setString(4, str);
                    longValue = user.getSocketId();   
                    insertStatement.setLong(5, longValue);
                    insertStatement.executeUpdate();
                } else {
                    updateStatement = con.prepareStatement(updateString);                    
                    str = user.getLogin();
                    if(str == null) 
                        str = "";
                    updateStatement.setString(1, str);
                    str = user.getPasswHash();
                    if(str == null) 
                        str = "";
                    updateStatement.setString(2, str);
                    str = user.getStatus();
                    if(str == null) 
                        str = "";
                    updateStatement.setString(3, str);
                    longValue = user.getSocketId();  
                    if(longValue == null) 
                        longValue = 0l;
                    updateStatement.setLong(4, longValue);
                    intValue = user.getUserId();
                    if(intValue == null) 
                        intValue = 0;    
                    updateStatement.setInt(5, intValue);                    
                    updateStatement.executeUpdate();
                }                
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (insertStatement != null) {
                    insertStatement.close();
                }
                if (updateStatement != null) {
                    updateStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        } finally {
            dbPool.putConnection(con);
        }
    }

    @Override
    public void updateUserByIdSetOnline(int userId) {
        Connection con = null;
        PreparedStatement updateStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                updateStatement = con.prepareStatement(updateStatusString);                    
                updateStatement.setString(1, Auction.USER_ONLINE); 
                updateStatement.setInt(2, userId);
                updateStatement.executeUpdate();                
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (updateStatement != null) {
                    updateStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        } finally {
            dbPool.putConnection(con);
        }
    }

    @Override
    public void updateUserByIdSetOffline(int userId) {
        Connection con = null;
        PreparedStatement updateStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                updateStatement = con.prepareStatement(updateStatusString);                    
                updateStatement.setString(1, Auction.USER_OFFLINE); 
                updateStatement.setInt(2, userId);
                updateStatement.executeUpdate();                
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (updateStatement != null) {
                    updateStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        } finally {
            dbPool.putConnection(con);
        }
    }

    @Override
    public void updateUserAllSetStatus(String status) {
        Connection con = null;
        PreparedStatement updateStatement = null;
        try {        
            try {
                con = dbPool.getConnection();
                con.setAutoCommit(false);
                updateStatement = con.prepareStatement(updateAllStatusString);                    
                updateStatement.setString(1, status); 
                updateStatement.executeUpdate();                
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (updateStatement != null) {
                    updateStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        } finally {
            dbPool.putConnection(con);
        }
    }
    
}

