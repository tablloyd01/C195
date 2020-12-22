package com.stone.rosetta.repository;

import com.stone.rosetta.repository.model.User;
import com.stone.rosetta.throwable.EntityNotUpdatedException;

import java.sql.SQLException;
import java.util.List;

public class UserRepository extends CrudRepository<User, Long>{

    private static final String INSERT_QUERY = "INSERT INTO U06bHt.`user` " +
            "(userName, password, active, createDate, createdBy, lastUpdate, lastUpdateBy) " +
            "VALUES(?, ?, 1, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?); ";
    private static final String SELECT_QUERY = "SELECT u.userId, u.userName, u.active FROM U06bHt.`user` u";
    private static final String UPDATE_QUERY = "UPDATE U06bHt.`user` SET " +
            "`userName` = ?, `active` = ?, `lastUpdate` = CURRENT_TIMESTAMP, `lastUpdateBy` = ? " +
            "WHERE userId = ? ";
    private static final String DELETE_QUERY = "DELETE FROM U06bHt.`user` " +
            "WHERE userId = ?;";
    private static final String SELECT_BY_USERID_QUERY = "SELECT * FROM U06bHt.`user` u WHERE u.userId = ?";
    private static final String SELECT_BY_USERNAME_QUERY = "SELECT u.userId, u.userName, u.password, u.active FROM U06bHt.`user` u WHERE u.userName = ?";

    public UserRepository() throws ClassNotFoundException {
        super();
    }

    @Override
    public User save(User user) throws SQLException {
        user.setId(jdbcHelper.insert(INSERT_QUERY, (preparedStatement -> {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setString(2, user.getPassword());
            preparedStatement.setString(3, user.getCreatedBy());
            preparedStatement.setString(4, user.getUpdatedBy());
            return preparedStatement;
        })));
        return user;
    }
    
    @Override
    public User update(User user) throws SQLException, EntityNotUpdatedException {
        int rows = jdbcHelper.update(UPDATE_QUERY, (preparedStatement -> {
            preparedStatement.setString(1, user.getUsername());
            preparedStatement.setBoolean(2, user.getActive());
            preparedStatement.setString(3, user.getUpdatedBy());
            preparedStatement.setLong(4, user.getId());
            return preparedStatement;
        }));
        if(rows == 0)
            throw new EntityNotUpdatedException("User not updated %s", user);
        return user;
    }

    @Override
    public List<User> getAll() throws SQLException {
        return jdbcHelper.findAll(SELECT_QUERY, (rs) -> {
            User user = new User(rs.getLong("userId"), rs.getString("userName"), rs.getBoolean("active"));
            return user;
        });
    }

    @Override
    public User getById(Long aLong) throws SQLException{
        return jdbcHelper.findById(SELECT_BY_USERID_QUERY, (ps) -> {
            ps.setLong(1, aLong);
            return ps;
        },  (rs) -> {
            User user = new User(rs.getLong("userId"), rs.getString("userName"), rs.getBoolean("active"));
            return user;
        });
    }

    @Override
    public int deleteById(Long aLong) throws SQLException {
        return jdbcHelper.update(DELETE_QUERY, (ps) -> {
            ps.setLong(1, aLong);
            return ps;
        });
    }

    public User getByUserName(String userName) throws SQLException{
        return jdbcHelper.findById(SELECT_BY_USERNAME_QUERY, (ps) -> {
            ps.setString(1, userName);
            return ps;
        },  (rs) -> {
            User user = new User(rs.getLong("userId"), rs.getString("userName"), rs.getString("password"), rs.getBoolean("active"));
            return user;
        });
    }

    // FIXME
    public static void main(String[] args) {
        try {
            UserRepository userRepository = new UserRepository();

            User userById = userRepository.getById(6l);
            System.out.printf("userById: %s\n", userById.getUsername());

            User userByUserName = userRepository.getByUserName("admin4");
            System.out.printf("userByUserName: %s\n", userByUserName.getUsername());

            User tobeSaveUser = new User("test", "test");
            tobeSaveUser.setCreatedBy("sys");
            tobeSaveUser.setUpdatedBy("sys");
            User saveUser = userRepository.save(tobeSaveUser);
            System.out.printf("saveUser: %s\n", saveUser.getId());

            try {
                saveUser.setActive(Boolean.TRUE);
                saveUser.setUpdatedBy(saveUser.getUpdatedBy() + "_" + saveUser.getId());
                System.out.printf("update rows affected: %s\n", userRepository.update(saveUser));
            } catch (EntityNotUpdatedException e) {
                e.printStackTrace();
            }
            System.out.printf("delete rows affected: %d\n", userRepository.deleteById(saveUser.getId()));

            /*List<User> users = userRepository.getAll();
            for (User u: users){
                System.out.println(u.getUsername());
                if(u.getId() == 7){
                    System.out.printf("deleted: rows affected: %d\n", userRepository.deleteById(u.getId()));
                }else {
                    try {
                        u.setUpdatedBy("sys_"+u.getId());
                        System.out.printf("update: rows affected: %s\n", userRepository.update(u));
                    } catch (EntityNotUpdatedException e) {
                        e.printStackTrace();
                    }
                }
            }*/
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
