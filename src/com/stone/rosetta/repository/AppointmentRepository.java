/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.repository;

import com.stone.rosetta.repository.model.Appointment;
import com.stone.rosetta.repository.model.Customer;
import com.stone.rosetta.repository.model.User;
import com.stone.rosetta.throwable.EntityNotUpdatedException;
import com.stone.rosetta.util.ConvertUtil;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

/**
 *
 * @author jeeva
 */
public class AppointmentRepository extends CrudRepository<Appointment, Long> {
    
    private final String INSERT_QUERY = "INSERT INTO u06bht.appointment "
            + "(customerId, userId, title, description, location, contact, `type`, url, `start`, `end`, createDate, createdBy, lastUpdate, lastUpdateBy) "
            + " VALUES(?, ?, ?, ?, ?, ?, ?, ?, ?, ?, CURRENT_TIMESTAMP, ?, CURRENT_TIMESTAMP, ?)";
    private final String UPDATE_QUERY = "UPDATE u06bht.appointment "
            + "SET customerId=?, userId=?, title=?, description=?, location=?, contact=?, `type`=?, url=?, `start`=?, `end`=?, lastUpdate=CURRENT_TIMESTAMP, lastUpdateBy=? "
            + "WHERE appointmentId=?";
    private final String SELECT_ALL_QUERY = "SELECT a.appointmentId, a.customerId, c.customerName, a.userId, u.userName, a.title, a.description, a.location, a.contact, a.`type`, a.url, a.`start`, a.`end` "
            + "FROM u06bht.appointment a "
            + "LEFT JOIN u06bht.customer c on c.customerId = a.customerId "
            + "LEFT JOIN u06bht.user u on u.userId = a.userId ";
    
    private final String SELECT_ALL_TODAY_QUERY = SELECT_ALL_QUERY 
            + "WHERE date(a.start) = date(now()) "
            + "ORDER BY a.start asc ";
    private final String SELECT_ALL_UPCOMING_QUERY = SELECT_ALL_QUERY + ""
            + "WHERE date(a.start) > date(now())"
            + "ORDER BY a.start asc ";
    private final String SELECT_ALL_PAST_QUERY = SELECT_ALL_QUERY + ""
            + "WHERE date(a.start) < date(now())"
            + "ORDER BY a.start desc ";
    
    private final String SELECT_ALL_BY_MONTH_QUERY = SELECT_ALL_QUERY + ""
            + "WHERE date(a.start) between ? and ? "
            + "ORDER BY a.start asc ";
    private final String SELECT_ALL_BY_BETWEEN_DATETIME_QUERY = SELECT_ALL_QUERY + ""
            + "WHERE ((? between a.start and a.end) or (? between a.start and a.end)) and a.appointmentId != ? "
            + "ORDER BY a.start asc ";
    private final String SELECT_ALL_WITHIN_15_MINUTES_QUERY = SELECT_ALL_QUERY + ""
            + "WHERE a.start between now() and ? "
            + "ORDER BY a.start asc ";
    
    private final String DELETE_BY_ID_QUERY = "DELETE FROM u06bht.appointment "
            + "WHERE appointmentId=?";

    private RowMapper<Appointment> rowMapper = null;
    
    public AppointmentRepository() throws ClassNotFoundException {
        this.rowMapper = (rs) -> {
            Appointment appointment = new Appointment(rs.getLong("appointmentId"), 
                    rs.getString("title"), rs.getString("description"), 
                    rs.getString("location"),rs.getString("contact"),rs.getString("type"),rs.getString("url"),
                    ConvertUtil.toLocalDateTime(rs.getTimestamp("start")),
                    ConvertUtil.toLocalDateTime(rs.getTimestamp("end")));
            appointment.setCustomer(new Customer(rs.getLong("customerId"), rs.getString("customerName")));
            appointment.setUser(new User(rs.getLong("userId"), rs.getString("userName")));
            return appointment;
         };
    }

    /**
     *
     * @param t
     * @return
     * @throws SQLException
     * @throws EntityNotUpdatedException
     */
    @Override
    public Appointment save(Appointment t) throws SQLException {
        t.setId(jdbcHelper.insert(INSERT_QUERY, (ps) -> {
            ps.setLong(1, t.getCustomer().getId());
            ps.setLong(2, t.getUser().getId());
            ps.setString(3, t.getTitle());
            ps.setString(4, t.getDescription());
            ps.setString(5, t.getLocation());
            ps.setString(6, t.getContact());
            ps.setString(7, t.getType());
            ps.setString(8, t.getUrl());
            ps.setTimestamp(9, ConvertUtil.toTimestamp(t.getStart()));
            ps.setTimestamp(10, ConvertUtil.toTimestamp(t.getEnd()));
            ps.setString(11, t.getCreatedBy());
            ps.setString(12, t.getUpdatedBy());
            return ps;
        }));
        return t;
    }

    @Override
    public Appointment update(Appointment t) throws SQLException, EntityNotUpdatedException{
        int rows = jdbcHelper.update(UPDATE_QUERY, (ps) -> {
            ps.setLong(1, t.getCustomer().getId());
            ps.setLong(2, t.getUser().getId());
            ps.setString(3, t.getTitle());
            ps.setString(4, t.getDescription());
            ps.setString(5, t.getLocation());
            ps.setString(6, t.getContact());
            ps.setString(7, t.getType());
            ps.setString(8, t.getUrl());
            ps.setTimestamp(9, ConvertUtil.toTimestamp(t.getStart()));
            ps.setTimestamp(10, ConvertUtil.toTimestamp(t.getEnd()));
            ps.setString(11, t.getUpdatedBy());
            ps.setLong(12, t.getId());
            return ps;
        });
        if(rows == 0){
            throw new EntityNotUpdatedException("Appointment Not Updated: %s", t);
        }
        return t;
    }

    @Override
    public List<Appointment> getAll() throws SQLException {
        return jdbcHelper.findAll(SELECT_ALL_QUERY, rowMapper);
    }
    
    public List<Appointment> getAllToday() throws SQLException {
        return jdbcHelper.findAll(SELECT_ALL_TODAY_QUERY, rowMapper);
    }

    @Override
    public Appointment getById(Long u) throws SQLException {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }

    @Override
    public int deleteById(Long id) throws SQLException {
        return jdbcHelper.update(DELETE_BY_ID_QUERY, (ps) -> {
            ps.setLong(1, id);
            return ps;
        });
    }

    public List<Appointment> getPastAll() throws SQLException {
        return jdbcHelper.findAll(SELECT_ALL_PAST_QUERY, rowMapper);
    }

    public List<Appointment> getUpCommingAll() throws SQLException {
        return jdbcHelper.findAll(SELECT_ALL_UPCOMING_QUERY, rowMapper);
    }

    public List<Appointment> getByMonth(LocalDate localDate) throws SQLException {
        return jdbcHelper.findAllBy(SELECT_ALL_BY_MONTH_QUERY,(ps) -> {
            ps.setTimestamp(1, Timestamp.valueOf(localDate.atStartOfDay()));
            LocalDate endOfMonth = localDate.plusMonths(1).minusDays(1);
            ps.setTimestamp(2, Timestamp.valueOf(endOfMonth.atStartOfDay()));
            return ps;
        }, rowMapper);
    }

    public List<Appointment> getAllByWeek(LocalDate localDate) throws SQLException {
        return jdbcHelper.findAllBy(SELECT_ALL_BY_MONTH_QUERY,(ps) -> {
            ps.setTimestamp(1, Timestamp.valueOf(localDate.atStartOfDay()));
            LocalDate endOfWeek = localDate.plusDays(6);
            ps.setTimestamp(2, Timestamp.valueOf(endOfWeek.atStartOfDay()));
            return ps;
        }, rowMapper);
    }

    public List<Appointment> getAllByBetween(Long id, LocalDateTime start, LocalDateTime end) throws SQLException {
        return jdbcHelper.findAllBy(SELECT_ALL_BY_BETWEEN_DATETIME_QUERY,(ps) -> {
            ps.setTimestamp(1, Timestamp.valueOf(start.plusMinutes(1)));
            ps.setTimestamp(2, Timestamp.valueOf(end.minusMinutes(1)));
            ps.setLong(3, id == null ? 0l: id);
            return ps;
        }, rowMapper);
    }

    public List<Appointment> getAllNextWithin15Minutes() throws SQLException {
        return jdbcHelper.findAllBy(SELECT_ALL_WITHIN_15_MINUTES_QUERY,(ps) -> {
            LocalDateTime withInLD = LocalDateTime.now().plusMinutes(15);
            ps.setTimestamp(1, Timestamp.valueOf(withInLD));
            return ps;
        }, rowMapper);
    }

}
