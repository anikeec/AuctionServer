/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.jdbc;

import com.apu.auctionserver.DB.entity.User;
import com.apu.auctionserver.repository.UserRepository;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.lang3.exception.ExceptionUtils;

/**
 *
 * @author apu
 */
public class UserRepositoryJDBC implements UserRepository {
    
    private static UserRepositoryJDBC instance;
    private final JDBCService dbService = JDBCService.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = UserRepositoryJDBC.class;
    
    PreparedStatement findUser = null;
    PreparedStatement insertUser = null;
    PreparedStatement updateUser = null;
    PreparedStatement removeUser = null;
    String findByIdString =
        "select * from USER where user.user_id = ?"; 
    String findAllString =
        "select * from USER";
//    String saveString =
//        "IF EXISTS(select * from USER where id = ? )" +
//        " update USER set login = ?," +
//        " passw_hash = ?," +
//        " status = ? " +
//        " where id = ? " +
//        " ELSE" +
//        " insert into USER(login,passw_hash,status) values(?,?,?);"; 
    String insertString = 
        " insert into USER(user_id,login,passw_hash,status) values(?,?,?,?);";
    String updateString = 
        " update USER set login = ?," +
        " passw_hash = ?," +
        " status = ? " +
        " where user_id = ? ";    
    String removeString =
        "delete from USER where user.user_id = ?";
    
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
        List<User> userList = new ArrayList<>();
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                findUser = con.prepareStatement(findAllString);
                log.debug(classname, findUser.toString());
                ResultSet rs = findUser.executeQuery();
                User user;
                while(rs.next()) {
                    user = new User(rs.getInt("user_id"));
                    user.setLogin(rs.getString("login"));
                    user.setPasswHash(rs.getString("passw_hash"));
                    user.setStatus(rs.getString("status"));
                    userList.add(user);
                }
            } catch (SQLException ex ) {
                throw ex;
            } finally {
                if (findUser != null) {
                    findUser.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(IOException | ClassNotFoundException | SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
            return null;
        }
        return userList;
    }

    @Override
    public User getUserById(int userId) {
        Connection con = null;
        User user = null;
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                findUser = con.prepareStatement(findByIdString);
                findUser.setInt(1, userId);
                log.debug(classname, findUser.toString());
                ResultSet rs = findUser.executeQuery();
                while(rs.next()) {
                    user = new User(rs.getInt("user_id"));
                    user.setLogin(rs.getString("login"));
                    user.setPasswHash(rs.getString("passw_hash"));
                    user.setStatus(rs.getString("status"));
                }
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                }
                throw ex;
            } finally {
                if (findUser != null) {
                    findUser.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(IOException | ClassNotFoundException | SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
            return null;
        }
        return user;
    }

    @Override
    public void removeUserById(int userId) {
        Connection con = null;
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                removeUser = con.prepareStatement(removeString);
                removeUser.setInt(1, userId);
                log.debug(classname, removeUser.toString());
                removeUser.executeUpdate();
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (removeUser != null) {
                    removeUser.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(IOException | ClassNotFoundException | SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
    }

    @Override
    public void saveUser(User user) {
        Connection con = null;
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                findUser = con.prepareStatement(findByIdString);
                findUser.setInt(1, user.getUserId());
                log.debug(classname, findUser.toString());
                ResultSet rs = findUser.executeQuery();
                String str;
                Integer intValue;
                if(!rs.next()) {
                    //insert
                    insertUser = con.prepareStatement(insertString);
                    intValue = user.getUserId();
                    if(intValue == null) 
                        intValue = 0;    
                    insertUser.setInt(1, intValue);
                    str = user.getLogin();
                    if(str == null) 
                        str = "";
                    insertUser.setString(2, str);
                    str = user.getPasswHash();
                    if(str == null) 
                        str = "";
                    insertUser.setString(3, str);
                    str = user.getStatus();
                    if(str == null) 
                        str = "";
                    insertUser.setString(4, str);
                    insertUser.executeUpdate();
                } else {
                    //update
                    updateUser = con.prepareStatement(updateString);                    
                    str = user.getLogin();
                    if(str == null) 
                        str = "";
                    updateUser.setString(1, str);
                    str = user.getPasswHash();
                    if(str == null) 
                        str = "";
                    updateUser.setString(2, str);
                    str = user.getStatus();
                    if(str == null) 
                        str = "";
                    updateUser.setString(3, str);
                    intValue = user.getUserId();
                    if(intValue == null) 
                        intValue = 0;    
                    updateUser.setInt(4, intValue);
                    updateUser.executeUpdate();
                }                
                con.commit();
            } catch (SQLException ex ) {
                if (con != null) {
                    log.debug(classname, "Transaction is being rolled back");
                    con.rollback();
                }
                throw ex;
            } finally {
                if (insertUser != null) {
                    insertUser.close();
                }
                if (updateUser != null) {
                    updateUser.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(IOException | ClassNotFoundException | SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        }
    }
    
}
