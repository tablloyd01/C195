package com.stone.rosetta.service;

import com.stone.rosetta.repository.UserRepository;
import com.stone.rosetta.repository.model.User;
import com.stone.rosetta.throwable.EntityNotUpdatedException;

import java.sql.SQLException;
import java.util.List;

public class UserService {

    private UserRepository userRepository;

    public UserService() throws ClassNotFoundException {
        this.userRepository = new UserRepository();
    }

    public User save(User user) throws SQLException {
        return userRepository.save(user);
    }

    public User update(User user) throws SQLException, EntityNotUpdatedException {
        return userRepository.update(user);
    }

    public int delete(Long id) throws SQLException {
        return userRepository.deleteById(id);
    }

    public List<User> getAll() throws SQLException {
        return userRepository.getAll();
    }

    public User getById(Long id) throws SQLException {
        return userRepository.getById(id);
    }

}
