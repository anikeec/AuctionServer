/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.DB.entity;

import java.io.Serializable;
import java.util.Date;
import java.util.List;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.OneToMany;
import javax.persistence.Table;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlTransient;

/**
 *
 * @author Ksusha
 */
@Entity
@Table(name = "auctionlot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuctionLot.findAll", query = "SELECT a FROM AuctionLot a")
    , @NamedQuery(name = "AuctionLot.findByLotId", query = "SELECT a FROM AuctionLot a WHERE a.lotId = :lotId")
    , @NamedQuery(name = "AuctionLot.findByLotName", query = "SELECT a FROM AuctionLot a WHERE a.lotName = :lotName")
    , @NamedQuery(name = "AuctionLot.findByStartPrice", query = "SELECT a FROM AuctionLot a WHERE a.startPrice = :startPrice")
    , @NamedQuery(name = "AuctionLot.findByStartDate", query = "SELECT a FROM AuctionLot a WHERE a.startDate = :startDate")
    , @NamedQuery(name = "AuctionLot.findByFinishDate", query = "SELECT a FROM AuctionLot a WHERE a.finishDate = :finishDate")
    , @NamedQuery(name = "AuctionLot.findByLastRate", query = "SELECT a FROM AuctionLot a WHERE a.lastRate = :lastRate")
    , @NamedQuery(name = "AuctionLot.findByStatus", query = "SELECT a FROM AuctionLot a WHERE a.status = :status")})
public class AuctionLot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "lot_id")
    private Integer lotId;
    @Column(name = "lot_name")
    private String lotName;
    @Column(name = "start_price")
    private Integer startPrice;
    @Column(name = "start_date")
    @Temporal(TemporalType.DATE)
    private Date startDate;
    @Column(name = "finish_date")
    @Temporal(TemporalType.DATE)
    private Date finishDate;
    @Column(name = "last_rate")
    private Integer lastRate;
    @Column(name = "status")
    private String status;
    @JoinColumn(name = "last_rate_user", referencedColumnName = "user_id")
    @ManyToOne
    private User lastRateUser;
    @JoinColumn(name = "observer", referencedColumnName = "user_id")
    @ManyToOne
    private User observer;
    @OneToMany(mappedBy = "observed")
    private List<User> userList;

    public AuctionLot() {
    }

    public AuctionLot(Integer lotId) {
        this.lotId = lotId;
    }

    public Integer getLotId() {
        return lotId;
    }

    public void setLotId(Integer lotId) {
        this.lotId = lotId;
    }

    public String getLotName() {
        return lotName;
    }

    public void setLotName(String lotName) {
        this.lotName = lotName;
    }

    public Integer getStartPrice() {
        return startPrice;
    }

    public void setStartPrice(Integer startPrice) {
        this.startPrice = startPrice;
    }

    public Date getStartDate() {
        return startDate;
    }

    public void setStartDate(Date startDate) {
        this.startDate = startDate;
    }

    public Date getFinishDate() {
        return finishDate;
    }

    public void setFinishDate(Date finishDate) {
        this.finishDate = finishDate;
    }

    public Integer getLastRate() {
        return lastRate;
    }

    public void setLastRate(Integer lastRate) {
        this.lastRate = lastRate;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public User getLastRateUser() {
        return lastRateUser;
    }

    public void setLastRateUser(User lastRateUser) {
        this.lastRateUser = lastRateUser;
    }

    public User getObserver() {
        return observer;
    }

    public void setObserver(User observer) {
        this.observer = observer;
    }

    @XmlTransient
    public List<User> getUserList() {
        return userList;
    }

    public void setUserList(List<User> userList) {
        this.userList = userList;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (lotId != null ? lotId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuctionLot)) {
            return false;
        }
        AuctionLot other = (AuctionLot) object;
        if ((this.lotId == null && other.lotId != null) || (this.lotId != null && !this.lotId.equals(other.lotId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.auctionserver.DB.entity.AuctionLot[ lotId=" + lotId + " ]";
    }
    
}
