/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.utils;

import java.util.Date;

/**
 *
 * @author apu
 */
public class Time {
    
    public static String getTime() {
        return new Date().toString();
    }
    
}
