package com.stone.rosetta.repository;

import com.stone.rosetta.jdbc.JdbcHelper;
import com.stone.rosetta.repository.model.Entity;
import com.stone.rosetta.repository.model.User;
import com.stone.rosetta.throwable.EntityNotUpdatedException;

import java.sql.SQLException;
import java.util.List;

public abstract class CrudRepository<T extends Entity, U> {
    protected JdbcHelper jdbcHelper = null;

    CrudRepository() throws ClassNotFoundException {
        jdbcHelper = JdbcHelper.getJdbcHelper();
    }

    public abstract T save(T t) throws SQLException;
    public abstract T update(T t) throws SQLException, EntityNotUpdatedException;
    public abstract List<T> getAll() throws SQLException;
    public abstract T getById(U u) throws SQLException;
    public abstract int deleteById(U u) throws SQLException;
}
