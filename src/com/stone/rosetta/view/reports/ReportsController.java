/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.reports;

import com.stone.rosetta.repository.model.Appointment;
import com.stone.rosetta.repository.model.User;
import com.stone.rosetta.service.AppointmentService;
import com.stone.rosetta.service.UserService;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class ReportsController implements Initializable {
    
    @FXML
    private TableView<Report1> report1Table;
    
    @FXML
    private TableColumn<Report1, String> report1AppointmentTypeTC;
    @FXML
    private TableColumn<Report1, String> report1AppointmentNumberTC;
    
    @FXML
    private TableView<Appointment> report2Table;
    @FXML
    private TableColumn<Appointment, String> report2AppointmentNumberTC;
    @FXML
    private TableColumn<Appointment, String> report2ConsultantTC;
    
    @FXML
    private TableView<Report3> report3Table;
    @FXML
    private TableColumn<Report3, String> report3AppointmentsTC;
    @FXML
    private TableColumn<Report3, String> report3DayTC;
    
    @FXML
    private ComboBox<Month> report1MonthCB;
    @FXML
    private ComboBox<Month> report3MonthCB;
    @FXML
    private ComboBox<User> report2UserCB;
    
    private ResourceBundle rb;
    private AppointmentService appointmentService;

    public ReportsController() throws ClassNotFoundException {
        this.userService = new UserService();
        this.appointmentService = new AppointmentService();
    }
    

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.printf("url: %s\n", url);
        this.rb = rb;
        loadData();
    }    

    private void loadData() {
        this.report1AppointmentNumberTC.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.report1AppointmentTypeTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        this.report2AppointmentNumberTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        this.report2ConsultantTC.setCellValueFactory(new PropertyValueFactory<>("start"));
        this.report3AppointmentsTC.setCellValueFactory(new PropertyValueFactory<>("count"));
        this.report3DayTC.setCellValueFactory(new PropertyValueFactory<>("date"));
        List<Month> months = Arrays.asList(Month.values());
        Collections.reverse(months);
        report1MonthCB.getItems().addAll(months);
        report1MonthCB.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Month> observable, Month oldValue, Month newValue) -> {
            if(newValue != null)
                refreshReport1Table(newValue);
        });
        report1MonthCB.getSelectionModel().selectFirst();
        
        report2UserCB.setCellFactory((ListView<User> param) -> new ListCell<User>(){
            @Override
            protected void updateItem(User item, boolean empty) {
                super.updateItem(item, empty);
                if(item == null || empty){
                    setGraphic(null);
                }else{
                    setText(item.getUsername());
                }
            }
        });
        report2UserCB.setConverter(getStringConverter());
        report2UserCB.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends User> observable, User oldValue, User newValue) -> {
            if(newValue != null)
                refreshReport2Table(newValue);
        });
        report2UserCB.getItems().addAll(getConsultants());
        report2UserCB.getSelectionModel().selectFirst();
        report3MonthCB.getItems().addAll(months);
        report3MonthCB.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Month> observable, Month oldValue, Month newValue) -> {
            if(newValue != null)
                refreshReport3Table(newValue);
        });
        report3MonthCB.getSelectionModel().selectFirst();
    }
    
    private UserService userService;

    private List<User> getConsultants() {
        try {
            return this.userService.getAll();
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
        return new ArrayList<>();
    }
    
    @FXML
    public void nextReport1MonthCB(){
        report1MonthCB.getSelectionModel().selectPrevious();
    }
    @FXML
    public void previousReport1MonthCB(){
        report1MonthCB.getSelectionModel().selectNext();
    }
    @FXML
    public void nextReport2UserCB(){
        report2UserCB.getSelectionModel().selectNext();
    }
    @FXML
    public void previousReport2UserCB(){
        report2UserCB.getSelectionModel().selectPrevious();
    }
    @FXML
    public void nextReport3MonthCB(){
        report3MonthCB.getSelectionModel().selectPrevious();
    }
    @FXML
    public void previousReport3MonthCB(){
        report3MonthCB.getSelectionModel().selectNext();
    }
    
    private void refreshReport1Table(Month month) {
        try {
            LocalDate ld = LocalDate.now().withMonth(month.getValue());
            report1Table.getItems().clear();
            report1Table.getItems().addAll(appointmentService.getAppointmentTypesByMonth(ld));
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void refreshReport2Table(User user) {
        try {
            report2Table.getItems().clear();
            report2Table.getItems().addAll(appointmentService.getSchedulesByConsultants(user));
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    private void refreshReport3Table(Month month) {
        try {
            LocalDate ld = LocalDate.now().withMonth(month.getValue());
            report3Table.getItems().clear();
            report3Table.getItems().addAll(this.appointmentService.getAppointmentCountGroupByDate(ld));
        } catch (SQLException ex) {
            Logger.getLogger(ReportsController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private StringConverter<User> getStringConverter() {
        return new StringConverter<User>() {
            @Override
            public String toString(User object) {
                if(object != null)
                    return object.getUsername();
                return null;
            }
            @Override
            public User fromString(String string) {
                if(string != null && !string.trim().isEmpty()){
                    FilteredList<User> filtered = report2UserCB.getItems().filtered(u -> u.getUsername().equalsIgnoreCase(string));
                    if(filtered != null && filtered.size() > 0)
                        return filtered.get(0);
                }
                return null;
            }
        };
    }
}
