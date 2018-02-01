/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.entity;

import com.apu.auctionapi.AuctionLotEntity;
import com.apu.auctionapi.query.InternalQuery;
import com.apu.auctionserver.nw.utils.Coder;
import com.apu.auctionserver.observer.Observable;
import com.apu.auctionserver.observer.Observer;
import com.apu.auctionserver.repository.SocketRepository;
import com.apu.auctionserver.repository.ram.SocketRepositoryRAM;
import com.apu.auctionserver.server.NIO.message.Message;
import com.apu.auctionserver.server.NIO.message.MessageProcessor;
import com.apu.auctionserver.utils.Time;
import java.io.Serializable;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.Id;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ksusha
 */
@Entity
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId")
    , @NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.login = :login")
    , @NamedQuery(name = "User.findByPasswHash", query = "SELECT u FROM User u WHERE u.passwHash = :passwHash")
    , @NamedQuery(name = "User.findByStatus", query = "SELECT u FROM User u WHERE u.status = :status")
    , @NamedQuery(name = "User.findByUsed", query = "SELECT u FROM User u WHERE u.used = :used")})
public class User implements Observer, Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "login")
    private String login;
    @Column(name = "passw_hash")
    private String passwHash;
    @Column(name = "status")
    private String status;
    @Column(name = "used")
    private Boolean used;
    @OneToMany(mappedBy = "lastRateUser", fetch = FetchType.EAGER)
    private List<AuctionLot> auctionLotList;
    @OneToMany(cascade = CascadeType.REMOVE, mappedBy = "user", fetch = FetchType.EAGER)
    private List<Observe> observeList;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
        this.login = "";
        this.passwHash = "";
        this.status = "OFFLINE";
        this.used = true;
    }

    public Integer getUserId() {
        return userId;
    }

    public void setUserId(Integer userId) {
        this.userId = userId;
    }

    public String getLogin() {
        return login;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    public String getPasswHash() {
        return passwHash;
    }

    public void setPasswHash(String passwHash) {
        this.passwHash = passwHash;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    @XmlTransient
    public List<AuctionLot> getAuctionLotList() {
        return auctionLotList;
    }

    public void setAuctionLotList(List<AuctionLot> auctionLotList) {
        this.auctionLotList = auctionLotList;
    }

    @XmlTransient
    public List<Observe> getObserveList() {
        return observeList;
    }

    public void setObserveList(List<Observe> observeList) {
        this.observeList = observeList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (userId != null ? userId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof User)) {
            return false;
        }
        User other = (User) object;
        if ((this.userId == null && other.userId != null) || (this.userId != null && !this.userId.equals(other.userId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.auctionserver.DB.entity.User[ userId=" + userId + " ]";
    }

    @Override
    public void update(Observable object) {
        AuctionLot lot = (AuctionLot)object;
        long timeToFinish = 
                lot.getFinishDate().getTime() - Time.getTimeMs();
        InternalQuery query = new InternalQuery(0, this.userId);
        AuctionLotEntity entity = new AuctionLotEntity(lot.getLotId(),
                                                        lot.getStartPrice(),
                                                        lot.getLotName(),
                                                        lot.getLastRate(),
                                                        lot.getLastRateUser().getUserId(),
                                                        lot.getObserverList().size(),
                                                        timeToFinish);
        query.setLot(entity);        
        
        SocketRepository sr = SocketRepositoryRAM.getInstance();
        Long socketId = sr.getSocketIdByUserId(this.userId);
        if(socketId != null) {
            Message message = new Message();
            message.socketId = socketId;
            message.writeToMessage(Coder.getInstance().code(query).getBytes());
            MessageProcessor.getInstance().process(message);
        }
    }
    
}
