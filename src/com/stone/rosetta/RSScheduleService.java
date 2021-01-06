/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta;

import com.stone.rosetta.repository.model.Appointment;
import java.util.ArrayList;
import java.util.List;
import java.util.Timer;
import javafx.concurrent.ScheduledService;

/**
 *
 * @author jeeva
 */
public class RSScheduleService {

    private static List<Timer> timers;
    private static List<ScheduledService> scheduledServices;

    public static void stopAllJob() {
        if (timers != null && timers.size() > 0) {
            timers.forEach(t -> t.cancel());
        }
        
        if(scheduledServices != null){
            scheduledServices.forEach(s -> {
                s.cancel();
            });
        }
    }

    public static void addJob(Timer timer){
        if(timers == null)
            timers = new ArrayList<>();
        timers.add(timer);
    }

    public static void addJob(ScheduledService scheduledService) {
        if(scheduledServices == null)
            scheduledServices = new ArrayList();
        scheduledServices.add(scheduledService);
    }
}
