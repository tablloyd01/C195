/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.repository;

import com.stone.rosetta.repository.model.Address;
import com.stone.rosetta.throwable.EntityNotUpdatedException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeeva
 */
public class AddressRepository extends CrudRepository<Address, Long>{
    
    private String INSERT_SQL = "INSERT INTO U06bHt.address "
            + "(address, address2, cityId, postalCode, phone, createDate, createdBy, lastUpdate, lastUpdateBy) "
            + "VALUES(?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?);";
    private String UPDATE_SQL = "UPDATE U06bHt.address "
            + "SET address=?, address2=?, cityId=?, postalCode=?, phone=?, lastUpdate=CURRENT_TIMESTAMP, lastUpdateBy=? "
            + "WHERE addressId=?";
    private String SELECT_ALL_BY_CITY_ID_QUERY = "SELECT addressId, address, address2, postalCode, phone  "
            + "FROM U06bHt.address WHERE cityId=?";

    public AddressRepository() throws ClassNotFoundException {
    }
    
    

    @Override
    public Address save(Address t) throws SQLException {
        t.setId(jdbcHelper.insert(INSERT_SQL, (ps) -> {
            ps.setString(1, t.getLine1());
            ps.setString(2, t.getLine2());
            ps.setLong(3, t.getCity().getId());
            ps.setString(4, t.getPostalCode());
            ps.setString(5, t.getPhone());
            ps.setString(6, t.getCreatedBy());
            ps.setString(7, t.getUpdatedBy());
            return ps;
        }));
        return t;
    }

    @Override
    public Address update(Address t) throws SQLException, EntityNotUpdatedException {
        int rows = jdbcHelper.update(UPDATE_SQL, (ps) -> {
            ps.setString(1, t.getLine1());
            ps.setString(2, t.getLine2());
            ps.setLong(3, t.getCity().getId());
            ps.setString(4, t.getPostalCode());
            ps.setString(5, t.getPhone());
            ps.setString(6, t.getUpdatedBy());            
            ps.setLong(7, t.getId());            
            return ps;
        });
        if(rows == 0)
            throw new EntityNotUpdatedException("Address not updated: %s", t);
        return t;
    }

    @Override
    public List<Address> getAll() throws SQLException {
        return null;
    }
    
    public List<Address> getAllByCityId(Long cityId) throws SQLException {
        return jdbcHelper.findAllBy(SELECT_ALL_BY_CITY_ID_QUERY, (ps)->{
            ps.setLong(1, cityId);
            return ps;
        }, (rs) -> {
            return new Address(rs.getLong("addressId"), rs.getString("address"), rs.getString("address2"), rs.getString("postalCode"), rs.getString("phone"), null);
        });
    }

    @Override
    public Address getById(Long u) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteById(Long u) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
    
}
