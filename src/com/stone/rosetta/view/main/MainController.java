/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.main;

import com.stone.rosetta.RSScheduleService;
import com.stone.rosetta.repository.model.Appointment;
import com.stone.rosetta.service.AppointmentService;
import com.stone.rosetta.service.UserAuthenticationService;
import com.stone.rosetta.view.AbstractController;
import com.stone.rosetta.view.ViewFactory;
import java.io.IOException;
import java.net.URL;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.util.Duration;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class MainController extends AbstractController implements Initializable {

    private UserAuthenticationService authenticationService;

    @FXML
    private ListView<String> menuList;
    private ChangeListener<String> menuChangeListener;

    @FXML
    private BorderPane borderPane;
    private ResourceBundle rb;
    private AppointmentService appointmentService;

    public MainController() {
//        on menu change listener lambda function
        this.menuChangeListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changePane(newValue);
        };
        try {
            authenticationService = UserAuthenticationService.getInstance();
            appointmentService = new AppointmentService();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.printf("url: %s \n", url);
        this.rb = rb;
        loadData();
    }

    private void loadData() {
        System.out.println("loadData");

//        menu list
        menuList.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        menuList.getItems().add(rb.getString("appointments"));
        menuList.getItems().add(rb.getString("customers"));
        menuList.getItems().add(rb.getString("calendar"));
        menuList.getItems().add(rb.getString("reports"));
//        menuList.getItems().add(rb.getString("users"));
        menuList.getSelectionModel().selectedItemProperty().addListener(menuChangeListener);
        menuList.getSelectionModel().select(rb.getString("appointments"));
        startAlertJob();
    }

    private void changePane(String newValue) {
        try {
            Pane pane = null;
//            if (newValue.equalsIgnoreCase(rb.getString("users"))) {
//                pane = ViewFactory.getInstance().getUsersPane();
//            } else 
            if (newValue.equalsIgnoreCase(rb.getString("customers"))) {
                pane = ViewFactory.getInstance().getCustomersPane();
            } else if (newValue.equalsIgnoreCase(rb.getString("appointments"))) {
                pane = ViewFactory.getInstance().getAppointmentPane();
            } else if (newValue.equalsIgnoreCase(rb.getString("calendar"))) {
                pane = ViewFactory.getInstance().getCalendarViewPane();
            } else if (newValue.equalsIgnoreCase(rb.getString("reports"))) {
                pane = ViewFactory.getInstance().getReportsViewPane();
            }
            if (pane != null) {
                borderPane.setCenter(pane);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void startAlertJob() {
        ScheduledService<Appointment> scheduledService = new ScheduledService<Appointment>() {
            @Override
            protected Task<Appointment> createTask() {
                System.out.println("createTask");
                return new Task<Appointment>() {
                    @Override
                    protected Appointment call() throws Exception {
                        System.out.println("call");
                        List<Appointment> as = appointmentService.getAllNextWithin15Minutes();
                        System.out.println("upcomming appointmetns within 15: " + as);
                        if(as != null && as.size() > 0)
                            return as.get(0);
                        return null;
                    }
                };
            }
        };
        scheduledService.setPeriod(Duration.minutes(15));
        scheduledService.setDelay(Duration.seconds(5));
//        lambda function used here to on succeed of schedule service
        scheduledService.setOnSucceeded(event -> {
            System.out.println("setOnSucceeded");
            if(event.getSource().getValue() != null){
                if(event.getSource().getValue() instanceof Appointment){
                    Appointment a = (Appointment) event.getSource().getValue();
                    System.out.println("appointment alert : " + a);
                    new Alert(Alert.AlertType.INFORMATION, "Next Appointment \"".concat(a.getTitle()).concat("\" will start at "+ a.getStart().toLocalTime()))
                            .show();
                }
            }
        });
        scheduledService.start();
        RSScheduleService.addJob(scheduledService);
    }

}
