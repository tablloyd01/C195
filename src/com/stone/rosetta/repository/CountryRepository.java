/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.repository;

import com.stone.rosetta.repository.model.Country;
import com.stone.rosetta.throwable.EntityNotUpdatedException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeeva
 */
public class CountryRepository extends CrudRepository<Country, Long> {

    private String SELECT_ALL_QUERY = "SELECT countryId, country "
            + "FROM u06bht.country;";

    public CountryRepository() throws ClassNotFoundException {
    }

    @Override
    public Country save(Country t) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public Country update(Country t) throws SQLException, EntityNotUpdatedException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public List<Country> getAll() throws SQLException {
        return jdbcHelper.findAll(SELECT_ALL_QUERY, (rs) -> {
            return new Country(rs.getLong("countryId"), rs.getString("country"));
        });
    }

    @Override
    public Country getById(Long u) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteById(Long u) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

}
