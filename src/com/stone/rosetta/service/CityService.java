/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.service;

import com.stone.rosetta.repository.CityRepository;
import com.stone.rosetta.repository.model.City;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeeva
 */
public class CityService {

    private CityRepository cityRepository;

    public CityService() throws ClassNotFoundException {
        this.cityRepository = new CityRepository();
    }
    
    public List<City> getAllByCountryId(Long countryId) throws SQLException{
        return this.cityRepository.getAllByCountryId(countryId);
    }
    
    
    
}
