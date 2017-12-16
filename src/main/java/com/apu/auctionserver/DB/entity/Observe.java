/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.DB.entity;

import java.io.Serializable;
import javax.persistence.Basic;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.NamedQueries;
import javax.persistence.NamedQuery;
import javax.persistence.Table;
import javax.xml.bind.annotation.XmlRootElement;

/**
 *
 * @author Ksusha
 */
@Entity
@Table(name = "observe")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "Observe.findAll", query = "SELECT o FROM Observe o")
    , @NamedQuery(name = "Observe.findById", query = "SELECT o FROM Observe o WHERE o.id = :id")
    , @NamedQuery(name = "Observe.findByUserId", query = "SELECT o FROM Observe o WHERE o.user = :userId")
    , @NamedQuery(name = "Observe.findByLotId", query = "SELECT o FROM Observe o WHERE o.lot = :lotId")})
public class Observe implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Basic(optional = false)
    @Column(name = "id")
    private Integer id;
    @JoinColumn(name = "lot", referencedColumnName = "lot_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private AuctionLot lot;
    @JoinColumn(name = "user", referencedColumnName = "user_id")
    @ManyToOne(optional = false, fetch = FetchType.EAGER)
    private User user;

    public Observe() {
    }

    public Observe(Integer id) {
        this.id = id;
    }

    public Integer getId() {
        return id;
    }

    public void setId(Integer id) {
        this.id = id;
    }

    public AuctionLot getLot() {
        return lot;
    }

    public void setLot(AuctionLot lot) {
        this.lot = lot;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    @Override
    public int hashCode() {
        int hash = 0;
        hash += (id != null ? id.hashCode() : 0);
        return hash;
    }

    @Override
    public boolean equals(Object object) {
        // TODO: Warning - this method won't work in the case the id fields are not set
        if (!(object instanceof Observe)) {
            return false;
        }
        Observe other = (Observe) object;
        if ((this.id == null && other.id != null) || (this.id != null && !this.id.equals(other.id))) {
            return false;
        }
        return true;
    }

    @Override
    public String toString() {
        return "com.apu.auctionserver.DB.entity.Observe[ id=" + id + " ]";
    }
    
}
