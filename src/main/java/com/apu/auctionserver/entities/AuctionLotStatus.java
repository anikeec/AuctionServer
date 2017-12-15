/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.entities;

import java.io.Serializable;
import java.util.Collection;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
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
@Table(name = "auctionlotstatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "AuctionLotStatus.findAll", query = "SELECT a FROM AuctionLotStatus a")
    , @NamedQuery(name = "AuctionLotStatus.findByStatusId", query = "SELECT a FROM AuctionLotStatus a WHERE a.statusId = :statusId")
    , @NamedQuery(name = "AuctionLotStatus.findByName", query = "SELECT a FROM AuctionLotStatus a WHERE a.name = :name")})
public class AuctionLotStatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "status_id")
    private Integer statusId;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "statusId")
    private Collection<AuctionLot> auctionLotCollection;

    public AuctionLotStatus() {
    }

    public AuctionLotStatus(Integer statusId) {
        this.statusId = statusId;
    }

    public Integer getStatusId() {
        return statusId;
    }

    public void setStatusId(Integer statusId) {
        this.statusId = statusId;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    @XmlTransient
    public Collection<AuctionLot> getAuctionLotCollection() {
        return auctionLotCollection;
    }

    public void setAuctionLotCollection(Collection<AuctionLot> auctionLotCollection) {
        this.auctionLotCollection = auctionLotCollection;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (statusId != null ? statusId.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof AuctionLotStatus)) {
            return false;
        }
        AuctionLotStatus other = (AuctionLotStatus) object;
        if ((this.statusId == null && other.statusId != null) || (this.statusId != null && !this.statusId.equals(other.statusId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.auctionserver.entities.AuctionLotStatus[ statusId=" + statusId + " ]";
    }
    
}
