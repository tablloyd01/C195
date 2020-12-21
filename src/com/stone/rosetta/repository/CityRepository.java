/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.repository;

import com.stone.rosetta.repository.model.City;
import com.stone.rosetta.repository.model.Country;
import com.stone.rosetta.throwable.EntityNotUpdatedException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeeva
 */
public class CityRepository extends CrudRepository<City, Long>{
    
    private String SELECT_ALL_QUERY = "SELECT cityId, city "
            + "FROM u06bht.city ";
    private String SELECT_ALL_BY_COUNTRY_ID_QUERY = "SELECT cityId, city "
            + "FROM u06bht.city WHERE countryId = ?";

    public CityRepository() throws ClassNotFoundException {
    }

    @Override
    public City save(City t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public City update(City t) throws SQLException, EntityNotUpdatedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<City> getAll() throws SQLException {
        return jdbcHelper.findAll(SELECT_ALL_QUERY, (rs) -> {
            return new City(rs.getLong("cityId"), rs.getString("city"), new Country(rs.getLong("countryId")));
        });
    }
    
    public List<City> getAllByCountryId(Long countryId) throws SQLException {
        return jdbcHelper.findAllBy(SELECT_ALL_BY_COUNTRY_ID_QUERY, (ps)->{
            ps.setLong(1, countryId);
            return ps;
        }, (rs) -> {
            return new City(rs.getLong("cityId"), rs.getString("city"));
        });
    }

    @Override
    public City getById(Long u) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteById(Long u) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
