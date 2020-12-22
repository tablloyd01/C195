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
import com.stone.rosetta.throwable.OutsideBusinessHoursException;
import com.stone.rosetta.throwable.SchedulingOverlappingAppointmentException;
import com.stone.rosetta.view.reports.Report1;
import com.stone.rosetta.view.reports.Report2;
import com.stone.rosetta.view.reports.Report3;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalTime;
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
    
    public Appointment save(Appointment a) throws SQLException, EntityNotUpdatedException, ClassNotFoundException,
            OutsideBusinessHoursException, SchedulingOverlappingAppointmentException {
        
        LocalTime businessStartHour = LocalTime.of(9, 0);
        LocalTime businessEndHour = LocalTime.of(18, 0);
        
        if(a.getStart().toLocalTime().isBefore(businessStartHour) || a.getStart().toLocalTime().isAfter(businessEndHour)){
            throw new OutsideBusinessHoursException("Outside business hour exception: start time:" + a.getStart().toLocalTime());
        }
        if(a.getEnd().toLocalTime().isBefore(businessStartHour) || a.getEnd().toLocalTime().isAfter(businessEndHour)){
            throw new OutsideBusinessHoursException("Outside business hour exception: end time:" + a.getEnd().toLocalTime());
        }
        
        List<Appointment> appointments = appointmentRepository.getAllByBetween(a.getId(), a.getStart(), a.getEnd());
        if(appointments != null && appointments.size() > 0){
            throw new SchedulingOverlappingAppointmentException();
        }
        
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

    public List<Appointment> getByMonth(LocalDate localDate) throws SQLException {
        return this.appointmentRepository.getByMonth(localDate);        
    }

    public List<Appointment> getAllByWeek(LocalDate localDate) throws SQLException {
        return this.appointmentRepository.getAllByWeek(localDate);
    }

    public List<Appointment> getAllNextWithin15Minutes() throws SQLException {
        return this.appointmentRepository.getAllNextWithin15Minutes();
    }

    public List<Report1> getAppointmentTypesByMonth(LocalDate ld) throws SQLException {
        return this.appointmentRepository.getAppointmentTypesByMonth(ld);
    }

    public List<Appointment> getSchedulesByConsultants(User user) throws SQLException {
        return this.appointmentRepository.getSchedulesByConsultants(user);
    }

    public List<Report3> getAppointmentCountGroupByDate(LocalDate ld) throws SQLException {
        return this.appointmentRepository.getAppointmentCountGroupByDate(ld);
    }
    
    
    
}
