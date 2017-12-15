/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.nw.exception;

/**
 *
 * @author apu
 */
public class ErrorQueryException extends Exception {

    public ErrorQueryException(String message, Throwable throwable) {
        super(message, throwable);
    }
    
}
