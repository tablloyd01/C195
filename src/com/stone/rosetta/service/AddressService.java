/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.service;

import com.stone.rosetta.repository.AddressRepository;

/**
 *
 * @author jeeva
 */
public class AddressService {

    private AddressRepository addressRepository;

    public AddressService() throws ClassNotFoundException {
        this.addressRepository = new AddressRepository();
    }
    
}
