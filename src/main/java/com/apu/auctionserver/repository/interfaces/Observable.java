/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.repository.interfaces;

/**
 *
 * @author apu
 */
public interface Observable {
    
    void registerObserver(Observer o);
    void removeObserver(Observer o);
    void notifyObservers();
    
}
