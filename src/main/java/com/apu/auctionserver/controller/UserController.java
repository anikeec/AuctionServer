/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.apu.auctionserver.controller;

import com.apu.auctionserver.repository.UserRepository;

/**
 *
 * @author apu
 */
public class UserController {
    private final UserRepository userRepository = UserRepository.getInstance();
    
    private static UserController instance;
    
    private UserController() {
    }
    
    public static UserController getInstance() {
        if(instance == null)
            instance = new UserController();
        return instance;
    }
    
}
