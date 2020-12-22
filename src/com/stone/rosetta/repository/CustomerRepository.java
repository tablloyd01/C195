package com.stone.rosetta.repository;

import com.stone.rosetta.repository.model.Address;
import com.stone.rosetta.repository.model.City;
import com.stone.rosetta.repository.model.Country;
import com.stone.rosetta.repository.model.Customer;
import com.stone.rosetta.throwable.EntityNotUpdatedException;

import java.sql.SQLException;
import java.util.List;

public class CustomerRepository extends CrudRepository<Customer, Long> {

    public CustomerRepository() throws ClassNotFoundException {
    }

    private static final String DELETE_BY_ID_QUERY = "DELETE FROM U06bHt.customer "
            + "WHERE customerId=?;";
    private static final String UPDATE_QUERY = "UPDATE U06bHt.customer "
            + "SET customerName=?, addressId=?, active=?, lastUpdate=CURRENT_TIMESTAMP, lastUpdateBy=? "
            + "WHERE customerId = ?";
    private static final String INSERT_QUERY = "INSERT INTO U06bHt.customer "
            + "(customerName, addressId, active, createDate, createdBy, lastUpdate, lastUpdateBy) "
            + "VALUES(?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?);";
    private static final String SELECT_ALL_QUERY = "select c.customerId, c.customerName, c.active, c.addressId, a.address, a.address2, a.postalCode, a.phone, c2.cityId, c2.city, c3.countryId, c3.country "
            + "from U06bHt.customer c "
            + "left join U06bHt.address a on a.addressId = c.addressId "
            + "left join U06bHt.city c2 on c2.cityId = a.cityId "
            + "left join U06bHt.country c3 on c3.countryId = c2.countryId";

    @Override
    public Customer save(Customer customer) throws SQLException {
        customer.setId(jdbcHelper.insert(INSERT_QUERY, (ps)-> {
            ps.setString(1, customer.getName());
            ps.setLong(2, customer.getAddress().getId());
            ps.setBoolean(3, customer.getActive());
            ps.setString(4, customer.getCreatedBy());
            ps.setString(5, customer.getUpdatedBy());
            return ps;
        }));
        return customer;
    }

    @Override
    public Customer update(Customer customer) throws SQLException, EntityNotUpdatedException {
        int rows = jdbcHelper.update(UPDATE_QUERY, (ps)-> {
            ps.setString(1, customer.getName());
            ps.setLong(2, customer.getAddress().getId());
            ps.setBoolean(3, customer.getActive());
            ps.setString(4, customer.getUpdatedBy());
            ps.setLong(5, customer.getId());
            return ps;
        });
        if(rows == 0){
            throw new EntityNotUpdatedException("Customer not updated: %s", customer);
        }
        return customer;
    }

    @Override
    public List<Customer> getAll() throws SQLException {
        return jdbcHelper.findAll(SELECT_ALL_QUERY, (rs) -> {
            Customer customer = new Customer(rs.getLong("customerId"), rs.getString("customerName"), rs.getBoolean("active"));
            Country country = new Country(rs.getLong("countryId"), rs.getString("country"));
            City city = new City(rs.getLong("cityId"), rs.getString("city"), country);
            Address address = new Address(rs.getLong("addressId"), rs.getString("address"), rs.getString("address2"), rs.getString("postalCode"), rs.getString("phone"), city);
            customer.setAddress(address);
            return customer;
        });
    }

    @Override
    public Customer getById(Long aLong) throws SQLException {
        return null;
    }

    @Override
    public int deleteById(Long aLong) throws SQLException {
        return jdbcHelper.update(DELETE_BY_ID_QUERY, (ps) -> {
            ps.setLong(1, aLong);
            return ps;
        });
    }
}
