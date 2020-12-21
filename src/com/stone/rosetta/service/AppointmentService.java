/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.service;

import com.stone.rosetta.repository.AppointmentRepository;
import com.stone.rosetta.repository.model.Appointment;
import com.stone.rosetta.repository.model.User;
import com.stone.rosetta.throwable.EntityNotUpdatedException;
import java.sql.SQLException;
import java.util.List;

/**
 *
 * @author jeeva
 */
public class AppointmentService {

    private AppointmentRepository appointmentRepository;

    public AppointmentService() throws ClassNotFoundException {
        this.appointmentRepository = new AppointmentRepository();
    }
    
    public List<Appointment> getAllToday() throws SQLException{
        return this.appointmentRepository.getAllToday();
    }
    
    public Appointment save(Appointment a) throws SQLException, EntityNotUpdatedException, ClassNotFoundException{
        User user = UserAuthenticationService.getInstance().getAuthenticatedUser();
        a.setUpdatedBy(user.getUsername());
        a.setUser(user);
        if(a.getId() != null && a.getId() > 0){
            this.appointmentRepository.update(a);
        }else{
            a.setCreatedBy(user.getUsername());
            this.appointmentRepository.save(a);
        }
        return a;
    }
    
    public int deleteById(Long id) throws SQLException{
        return this.appointmentRepository.deleteById(id);
    }

    public List<Appointment> getPastAll() throws SQLException {
        return this.appointmentRepository.getPastAll();
    }

    public List<Appointment> getUpCommingAll() throws SQLException {
        return this.appointmentRepository.getUpCommingAll();
    }
    
    
    
}
