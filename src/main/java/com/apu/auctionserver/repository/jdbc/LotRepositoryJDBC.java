/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.jdbc;

import com.apu.auctionserver.DB.entity.AuctionLot;
import com.apu.auctionserver.DB.entity.User;
import com.apu.auctionserver.repository.LotRepository;
import com.apu.auctionserver.utils.Log;
import java.io.IOException;
import java.sql.Connection;
import java.sql.Date;
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
public class LotRepositoryJDBC implements LotRepository {
    
    private static LotRepositoryJDBC instance;
    private final JDBCService dbService = JDBCService.getInstance();
    private static final Log log = Log.getInstance();
    private static final Class classname = LotRepositoryJDBC.class;
    
    PreparedStatement findStatement = null;
    PreparedStatement insertStatement = null;
    PreparedStatement updateStatement = null;
    PreparedStatement removeStatement = null;
    String findByIdString =
        "select * from AUCTIONLOT where lot_id = ?"; 
    String findAllString =
        "select * from AUCTIONLOT"; 
    String insertString = 
        " insert into AUCTIONLOT(lot_id,"
            + "lot_name,"
            + "start_price,"
            + "start_date,"
            + "finish_date,"
            + "last_rate,"
            + "last_rate_user,"
            + "status) "
            + "values(?,?,?,?,?,?,?,?);";
    String updateString = 
        " update AUCTIONLOT set lot_name = ?," +
        " start_price = ?," +
        " start_date = ?," +
        " finish_date = ?," +
        " last_rate = ?," +
        " last_rate_user = ?," +
        " status = ? " +
        " where lot_id = ? ";    
    String removeString =
        "delete from AUCTIONLOT where lot_id = ?";
    
    private LotRepositoryJDBC() {
    }
    
    public static LotRepositoryJDBC getInstance() {
        if(instance == null)
            instance = new LotRepositoryJDBC();
        return instance;
    }

    @Override
    public List<AuctionLot> getAuctionLots() {
        Connection con = null;
        List<AuctionLot> lotList = new ArrayList<>();
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(findAllString);
                log.debug(classname, findStatement.toString());
                ResultSet rs = findStatement.executeQuery();
                AuctionLot lot;
                while(rs.next()) {
                    lot = new AuctionLot(rs.getInt("lot_id"));
                    lot.setLotName(rs.getString("lot_name"));
                    lot.setStartPrice(rs.getInt("start_price"));
                    lot.setStartDate(rs.getDate("start_date"));
                    lot.setFinishDate(rs.getDate("finish_date"));
                    lot.setLastRate(rs.getInt("last_rate"));
                    Integer userId = rs.getInt("last_rate_user");
                    lot.setLastRateUser(new User(userId));
                    lot.setStatus(rs.getString("status"));
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
    public AuctionLot getAuctionLotById(int lotId) {
        Connection con = null;
        AuctionLot auctionLot = null;
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(findByIdString);
                findStatement.setInt(1, lotId);
                log.debug(classname, findStatement.toString());
                ResultSet rs = findStatement.executeQuery();
                while(rs.next()) {
                    auctionLot = new AuctionLot(rs.getInt("lot_id"));
                    auctionLot.setLotName(rs.getString("lot_name"));
                    auctionLot.setStartPrice(rs.getInt("start_price"));
                    auctionLot.setStartDate(rs.getDate("start_date"));
                    auctionLot.setFinishDate(rs.getDate("finish_date"));
                    auctionLot.setLastRate(rs.getInt("last_rate"));
                    Integer userId = rs.getInt("last_rate_user");
                    auctionLot.setLastRateUser(new User(userId));
                    auctionLot.setStatus(rs.getString("status"));
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
        return auctionLot;
    }

    @Override
    public void removeAuctionLotById(int lotId) {
        Connection con = null;
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                removeStatement = con.prepareStatement(removeString);
                removeStatement.setInt(1, lotId);
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

    @Override
    public void saveAuctionLot(AuctionLot lot) {
        Connection con = null;
        try {        
            try {
                con = dbService.dbConnect();
                con.setAutoCommit(false);
                findStatement = con.prepareStatement(findByIdString);
                findStatement.setInt(1, lot.getLotId());
                log.debug(classname, findStatement.toString());
                ResultSet rs = findStatement.executeQuery();
                String str;
                Integer intValue;
                Date date;
                if(!rs.next()) {
                    insertStatement = con.prepareStatement(insertString);
                    intValue = lot.getLotId();
                    if(intValue == null) 
                        intValue = 0;    
                    insertStatement.setInt(1, intValue);
                    str = lot.getLotName();
                    if(str == null) 
                        str = "";
                    insertStatement.setString(2, str);
                    intValue = lot.getStartPrice();
                    if(intValue == null) 
                        intValue = 0;    
                    insertStatement.setInt(3, intValue);
                    java.util.Date dateTemp = lot.getStartDate();
                    if(dateTemp != null) {
                        date = new Date(dateTemp.getTime());
                    } else {
                        date = null;
                    } 
                    insertStatement.setDate(4, date);
                    dateTemp = lot.getFinishDate();
                    if(dateTemp != null) {
                        date = new Date(dateTemp.getTime());
                    } else {
                        date = null;
                    }
                    insertStatement.setDate(5, date);
                    intValue = lot.getLastRate();
                    if(intValue == null) 
                        intValue = 0;    
                    insertStatement.setInt(6, intValue);
                    User user = lot.getLastRateUser();
                    if(user == null) 
                        intValue = null;
                    else
                        intValue = user.getUserId();
                    insertStatement.setObject(7, intValue);
                    str = lot.getStatus();
                    if(str == null) 
                        str = "";
                    insertStatement.setString(8, str);
                    insertStatement.executeUpdate();
                } else {
                    updateStatement = con.prepareStatement(updateString);                    
                    str = lot.getLotName();
                    if(str == null) 
                        str = "";
                    updateStatement.setString(1, str);
                    intValue = lot.getStartPrice();
                    if(intValue == null) 
                        intValue = 0;    
                    updateStatement.setInt(2, intValue);
                    java.util.Date dateTemp = lot.getStartDate();
                    if(dateTemp != null) {
                        date = new Date(dateTemp.getTime());
                    } else {
                        date = null;
                    }                    
                    updateStatement.setDate(3, date);
                    dateTemp = lot.getFinishDate();
                    if(dateTemp != null) {
                        date = new Date(dateTemp.getTime());
                    } else {
                        date = null;
                    }
                    updateStatement.setDate(4, date);
                    intValue = lot.getLastRate();
                    if(intValue == null) 
                        intValue = 0;    
                    updateStatement.setInt(5, intValue);
                    User user = lot.getLastRateUser();
                    if(user == null) 
                        intValue = null;
                    else
                        intValue = user.getUserId();
                    updateStatement.setObject(6, intValue);
                    str = lot.getStatus();
                    if(str == null) 
                        str = "";
                    updateStatement.setString(7, str);
                    intValue = lot.getLotId();
                    if(intValue == null) 
                        intValue = 0;    
                    updateStatement.setInt(8, intValue);
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
