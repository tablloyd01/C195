/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.service;

import com.stone.rosetta.repository.CountryRepository;
import com.stone.rosetta.repository.model.Country;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeeva
 */
public class CountryService {
    
    private CountryRepository countryRepository;

    public CountryService() throws ClassNotFoundException {
        countryRepository = new CountryRepository();
    }
    
    public List<Country> getAll() throws SQLException{
        return this.countryRepository.getAll();
    }
    
    
    
}
