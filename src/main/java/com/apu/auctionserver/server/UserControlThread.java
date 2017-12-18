/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.server;

import com.apu.auctionserver.utils.Time;

/**
 *
 * @author apu
 */
public class UserControlThread implements Runnable {    

    @Override
    public void run() {
        while(true) {
            try {
                Thread.sleep(Time.USER_CHECK_TIMEOUT);
            } catch (InterruptedException ex) { }
            UserControlService.runChecking();
        }
    }
    
}
