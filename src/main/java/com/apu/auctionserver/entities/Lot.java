/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entities;

import java.io.Serializable;
import java.util.Collection;
import java.util.Date;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
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
 * @author apu
 */
@Entity
@Table(name = "lot")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lot.findAll", query = "SELECT l FROM Lot l")
    , @NamedQuery(name = "Lot.findByLotId", query = "SELECT l FROM Lot l WHERE l.lotId = :lotId")
    , @NamedQuery(name = "Lot.findByName", query = "SELECT l FROM Lot l WHERE l.name = :name")
    , @NamedQuery(name = "Lot.findByStartPrice", query = "SELECT l FROM Lot l WHERE l.startPrice = :startPrice")
    , @NamedQuery(name = "Lot.findByStartDate", query = "SELECT l FROM Lot l WHERE l.startDate = :startDate")
    , @NamedQuery(name = "Lot.findByFinishDate", query = "SELECT l FROM Lot l WHERE l.finishDate = :finishDate")
    , @NamedQuery(name = "Lot.findByLastRate", query = "SELECT l FROM Lot l WHERE l.lastRate = :lastRate")})
public class Lot implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "lot_id")
    private Integer lotId;
    @Column(name = "name")
    private String name;
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
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Lotstatus statusId;
    @JoinColumn(name = "last_rate_user_id", referencedColumnName = "user_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private User lastRateUserId;
    @OneToMany(mappedBy = "lotId", fetch = FetchType.EAGER)
    private Collection<Observes> observesCollection;

    public Lot() {
    }

    public Lot(Integer lotId) {
        this.lotId = lotId;
    }

    public Integer getLotId() {
        return lotId;
    }

    public void setLotId(Integer lotId) {
        this.lotId = lotId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
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

    public Lotstatus getStatusId() {
        return statusId;
    }

    public void setStatusId(Lotstatus statusId) {
        this.statusId = statusId;
    }

    public User getLastRateUserId() {
        return lastRateUserId;
    }

    public void setLastRateUserId(User lastRateUserId) {
        this.lastRateUserId = lastRateUserId;
    }

    @XmlTransient
    public Collection<Observes> getObservesCollection() {
        return observesCollection;
    }

    public void setObservesCollection(Collection<Observes> observesCollection) {
        this.observesCollection = observesCollection;
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
        if (!(object instanceof Lot)) {
            return false;
        }
        Lot other = (Lot) object;
        if ((this.lotId == null && other.lotId != null) || (this.lotId != null && !this.lotId.equals(other.lotId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.auctionserver.entities.Lot[ lotId=" + lotId + " ]";
    }
    
}
