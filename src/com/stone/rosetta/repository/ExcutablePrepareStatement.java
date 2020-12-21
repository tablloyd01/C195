package com.stone.rosetta.repository;

import java.sql.PreparedStatement;
import java.sql.SQLException;

public interface ExcutablePrepareStatement {
    public PreparedStatement execute(PreparedStatement preparedStatement) throws SQLException;
}
