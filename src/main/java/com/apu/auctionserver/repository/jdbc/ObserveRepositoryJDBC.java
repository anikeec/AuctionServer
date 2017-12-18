/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.jdbc;

import com.apu.auctionserver.DB.entity.AuctionLot;
import com.apu.auctionserver.DB.entity.User;
import com.apu.auctionserver.repository.LotRepository;
import com.apu.auctionserver.repository.ObserveRepository;
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
public class ObserveRepositoryJDBC implements ObserveRepository {
    
    private static ObserveRepositoryJDBC instance;
    private final JDBCService dbService = JDBCService.getInstance();
    private final LotRepository lotRepository = LotRepositoryJDBC.getInstance();
    private final UserRepository userRepository = UserRepositoryJDBC.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = ObserveRepositoryJDBC.class;
    
    PreparedStatement findStatement = null;
    PreparedStatement insertStatement = null;
    PreparedStatement removeStatement = null;
    String findByIdString =
        "select * from OBSERVE where lot_id = ?";
    String findByUserIdString =
        "select * from OBSERVE where user = ?"; 
    String findByLotIdString =
        "select * from OBSERVE where lot = ?"; 
    String findAllString =
        "select * from OBSERVE"; 
    String insertString = 
        "insert into OBSERVE(user,lot) values(?,?);";   
    String removeByIdString =
        "delete from OBSERVE where id = ?";
    String removeByUserIdString =
        "delete from OBSERVE where user = ?";
    String removeByLotIdString =
        "delete from OBSERVE where lot = ?";
    
    private ObserveRepositoryJDBC() {
    }
    
    public static ObserveRepositoryJDBC getInstance() {
        if(instance == null)
            instance = new ObserveRepositoryJDBC();
        return instance;
    }

    @Override
    public List<AuctionLot> getObservableAuctionLotsByUser(User user) {
        Connection con = null;
        List<AuctionLot> lotList = new ArrayList<>();
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(findByUserIdString);
                findStatement.setInt(1, user.getUserId());
                log.debug(classname, findStatement.toString());
                ResultSet rs = findStatement.executeQuery();
                AuctionLot lot;
                while(rs.next()) {
                    int lotId = rs.getInt("lot");
                    lot = lotRepository.getAuctionLotById(lotId);
                    lotList.add(lot);
                }
            } finally {
                if (findStatement != null) {
                    findStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(IOException | ClassNotFoundException | SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
            return null;
        } finally {
            try {
                dbService.dbDisconnect();
            } catch (SQLException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
        }
        return lotList;
    }

    @Override
    public List<Integer> getObserverIdListByAuctionLot(AuctionLot lot) {
        Connection con = null;
        List<Integer> userIdList = new ArrayList<>();
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(findByLotIdString);
                findStatement.setInt(1, lot.getLotId());
                log.debug(classname, findStatement.toString());
                ResultSet rs = findStatement.executeQuery();
                User user;
                while(rs.next()) {
                    int userId = rs.getInt("user");
                    userIdList.add(userId);
                }
            } finally {
                if (findStatement != null) {
                    findStatement.close();
                }
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(IOException | ClassNotFoundException | SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
            return null;
        } finally {
            try {
                dbService.dbDisconnect();
            } catch (SQLException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
        }
        return userIdList;
    }

    @Override
    public void addAuctionLotIdListToObservableByUser(User user, List<Integer> lotIds) {
        for(Integer lotId:lotIds) {
            addAuctionLotToObservableByUser(user,new AuctionLot(lotId));
        }
    }

    @Override
    public void addAuctionLotToObservableByUser(User user, AuctionLot lot) {
        Connection con = null;
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                Integer intValue;
                insertStatement = con.prepareStatement(insertString);
                intValue = user.getUserId();
                if(intValue == null) 
                    intValue = 0;    
                insertStatement.setInt(1, intValue);
                intValue = lot.getLotId();
                if(intValue == null) 
                    intValue = 0;    
                insertStatement.setInt(2, intValue);
                insertStatement.executeUpdate();
                log.debug(classname, insertStatement.toString());
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
                if(con != null) {
                    con.setAutoCommit(true);
                }
            }
        } catch(IOException | ClassNotFoundException | SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                dbService.dbDisconnect();
            } catch (SQLException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
        }  
    }

    @Override
    public void clearObservableAuctionLotsByUser(User user) {
        Connection con = null;
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                removeStatement = con.prepareStatement(removeByUserIdString);
                removeStatement.setInt(1, user.getUserId());
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
        } catch(IOException | ClassNotFoundException | SQLException ex) {
            log.debug(classname,ExceptionUtils.getStackTrace(ex));
        } finally {
            try {
                dbService.dbDisconnect();
            } catch (SQLException ex) {
                log.debug(classname,ExceptionUtils.getStackTrace(ex));
            }
        }
    }
    
}
