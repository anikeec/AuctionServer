/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 *
 * @author apu
 */
public class Time {
    public static final long SECOND = 1000l;
    public static final long MINUTE = 1000*SECOND;
    public static final long USER_TIMEOUT = 10*SECOND;
    public static final long USER_CHECK_TIMEOUT = 5*SECOND;
    
    public static String getTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy.MM.dd HH:mm:ss.SSSZ");
        return dateFormat.format(new Date());
    }
    
}
