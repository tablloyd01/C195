package com.stone.rosetta.service;

import com.stone.rosetta.repository.AddressRepository;
import com.stone.rosetta.repository.CustomerRepository;
import com.stone.rosetta.repository.model.Address;
import com.stone.rosetta.repository.model.Customer;
import com.stone.rosetta.repository.model.User;
import com.stone.rosetta.throwable.EntityNotUpdatedException;

import java.sql.SQLException;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

public class CustomerService {

    private CustomerRepository customerRepository;

    public CustomerService() throws ClassNotFoundException {
        customerRepository = new CustomerRepository();
        addressRepository = new AddressRepository();
    }

    public List<Customer> getAll() throws SQLException {
        return customerRepository.getAll();
    }

    private Customer update(Customer cs) throws SQLException, EntityNotUpdatedException {
        try {
            User user = UserAuthenticationService.getInstance().getAuthenticatedUser();
            cs.setUpdatedBy(user.getUsername());
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        }
        updateAddress(cs.getAddress());
        return customerRepository.update(cs);
    }

    public int deleteById(Long id) throws SQLException {
        return customerRepository.deleteById(id);
    }

    public Customer save(Customer cs) throws SQLException, EntityNotUpdatedException {
        if(cs.getId() != null && cs.getId() > 0){
            update(cs);
        }else{           
            try {
                User user = UserAuthenticationService.getInstance().getAuthenticatedUser();
                cs.setCreatedBy(user.getUsername());
                cs.setUpdatedBy(user.getUsername());
            } catch (ClassNotFoundException ex) {
                Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            }
            updateAddress(cs.getAddress());
            customerRepository.save(cs);
        }
        return cs;
    }

    private AddressRepository addressRepository;

    private void updateAddress(Address address) {
        try {
            User user = UserAuthenticationService.getInstance().getAuthenticatedUser();
            address.setCreatedBy(user.getUsername());
            address.setUpdatedBy(user.getUsername());
            try {
                if (address.getId() == null) {
                    addressRepository.save(address);
                } else if (address.getId() > 0) {
                    addressRepository.update(address);
                }
            } catch (SQLException ex) {
                Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            } catch (EntityNotUpdatedException ex) {
                Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
            }
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerService.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
