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
 * @author apu
 */
@Entity
@Table(name = "lotstatus")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Lotstatus.findAll", query = "SELECT l FROM Lotstatus l")
    , @NamedQuery(name = "Lotstatus.findByStatusId", query = "SELECT l FROM Lotstatus l WHERE l.statusId = :statusId")
    , @NamedQuery(name = "Lotstatus.findByName", query = "SELECT l FROM Lotstatus l WHERE l.name = :name")})
public class Lotstatus implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "status_id")
    private Integer statusId;
    @Column(name = "name")
    private String name;
    @OneToMany(mappedBy = "statusId", fetch = FetchType.EAGER)
    private Collection<Lot> lotCollection;

    public Lotstatus() {
    }

    public Lotstatus(Integer statusId) {
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
    public Collection<Lot> getLotCollection() {
        return lotCollection;
    }

    public void setLotCollection(Collection<Lot> lotCollection) {
        this.lotCollection = lotCollection;
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
        if (!(object instanceof Lotstatus)) {
            return false;
        }
        Lotstatus other = (Lotstatus) object;
        if ((this.statusId == null && other.statusId != null) || (this.statusId != null && !this.statusId.equals(other.statusId))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.auctionserver.entities.Lotstatus[ statusId=" + statusId + " ]";
    }
    
}
