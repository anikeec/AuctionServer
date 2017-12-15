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
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
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
@Table(name = "user")
@XmlRootElement
@NamedQueries({
    @NamedQuery(name = "User.findAll", query = "SELECT u FROM User u")
    , @NamedQuery(name = "User.findByUserId", query = "SELECT u FROM User u WHERE u.userId = :userId")
    , @NamedQuery(name = "User.findByLogin", query = "SELECT u FROM User u WHERE u.login = :login")
    , @NamedQuery(name = "User.findByPasswHash", query = "SELECT u FROM User u WHERE u.passwHash = :passwHash")
    , @NamedQuery(name = "User.findByUsed", query = "SELECT u FROM User u WHERE u.used = :used")})
public class User implements Serializable {

    private static final long serialVersionUID = 1L;
    @Id
    @Basic(optional = false)
    @Column(name = "user_id")
    private Integer userId;
    @Column(name = "login")
    private String login;
    @Column(name = "passw_hash")
    private String passwHash;
    @Column(name = "used")
    private Boolean used;
    @OneToMany(mappedBy = "lastRateUserId", fetch = FetchType.EAGER)
    private Collection<Lot> lotCollection;
    @OneToMany(mappedBy = "userId", fetch = FetchType.EAGER)
    private Collection<Observes> observesCollection;
    @JoinColumn(name = "status_id", referencedColumnName = "status_id")
    @ManyToOne(fetch = FetchType.EAGER)
    private Ustatus statusId;

    public User() {
    }

    public User(Integer userId) {
        this.userId = userId;
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

    public Boolean getUsed() {
        return used;
    }

    public void setUsed(Boolean used) {
        this.used = used;
    }

    @XmlTransient
    public Collection<Lot> getLotCollection() {
        return lotCollection;
    }

    public void setLotCollection(Collection<Lot> lotCollection) {
        this.lotCollection = lotCollection;
    }

    @XmlTransient
    public Collection<Observes> getObservesCollection() {
        return observesCollection;
    }

    public void setObservesCollection(Collection<Observes> observesCollection) {
        this.observesCollection = observesCollection;
    }

    public Ustatus getStatusId() {
        return statusId;
    }

    public void setStatusId(Ustatus statusId) {
        this.statusId = statusId;
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
        return "com.apu.auctionserver.entities.User[ userId=" + userId + " ]";
    }
    
}
