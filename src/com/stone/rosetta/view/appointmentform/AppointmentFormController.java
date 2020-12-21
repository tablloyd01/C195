/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.appointmentform;

import com.stone.rosetta.repository.model.Appointment;
import com.stone.rosetta.repository.model.Customer;
import com.stone.rosetta.service.AppointmentService;
import com.stone.rosetta.throwable.EntityNotUpdatedException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.DateCell;
import javafx.scene.control.DatePicker;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class AppointmentFormController implements Initializable {

    private Stage stage;
    private Customer customer;
    private ResourceBundle rb;
    
    private AppointmentService appointmentService;

    @FXML
    private TextField customerNameTF;
    @FXML
    private TextField titleTF;
    @FXML
    private Label titleHL;
    @FXML
    private TextArea descriptionTA;
    @FXML
    private Label descriptionHL;
    @FXML
    private TextField locationTF;
    @FXML
    private Label locationHL;
    @FXML
    private TextField contactTF;
    @FXML
    private Label contactHL;
    @FXML
    private TextField typeTF;
    @FXML
    private Label typeHL;
    @FXML
    private TextField urlTF;
    @FXML
    private Label urlHL;
    @FXML
    private DatePicker startDP;
    @FXML
    private Label startDPHL;
    @FXML
    private Spinner<Integer> startHourS;
    @FXML
    private Spinner<Integer> startMintS;
    @FXML
    private DatePicker endDP;
    @FXML
    private Label endDPHL;
    @FXML
    private Spinner<Integer> endHourS;
    @FXML
    private Spinner<Integer> endMintS;

    public AppointmentFormController() throws ClassNotFoundException {
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

    public void setCustomer(Customer customer) {
        this.customer = customer;
        if (customer != null) {
            this.customerNameTF.setText(customer.getName());
            if(this.locationTF.getText().trim().isEmpty())
                this.locationTF.setText(customer.getAddress().getFullAddress());
            if(this.contactTF.getText().trim().isEmpty())
                this.contactTF.setText(customer.getAddress().getPhone());
        }
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    private void loadData() {
        this.startHourS.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        this.startMintS.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 10));
        this.endHourS.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 23));
        this.endMintS.setValueFactory(new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 59, 0, 10));
    }
    
    private Appointment appointment;
    
    private FormAction formAction;

    public void setFormAction(FormAction formAction) {
        this.formAction = formAction;
    }
    
    @FXML
    public void save(Event event){
        if(this.formAction != null && isFormValid()){
            if(this.appointment == null)
                this.appointment = new Appointment();
            this.appointment.setTitle(titleTF.getText().trim());
            this.appointment.setDescription(descriptionTA.getText().trim());
            this.appointment.setLocation(locationTF.getText().trim());
            this.appointment.setContact(contactTF.getText().trim());
            this.appointment.setType(typeTF.getText().trim());
            this.appointment.setUrl(urlTF.getText().trim());
            this.appointment.setCustomer(this.customer);
            this.appointment.setStart(getStartLocalDateTime());
            this.appointment.setEnd(getEndLocalDateTime());
            this.formAction.action(appointment);
        }
    }
    
    @FXML
    public void close(Event event){
        stage.close();
    };
    @FXML
    public void saveAndClose(Event event){
        save(event);
        close(event);
    };

    private boolean isFormValid() {
        boolean isValid = true;
        this.titleHL.setText("");
        if(this.titleTF.getText().trim().isEmpty()){
            this.titleHL.setText(rb.getString("ui.error.field.required"));
            isValid = false;
        }
        this.descriptionHL.setText("");
        if(this.descriptionTA.getText().trim().isEmpty()){
            this.descriptionHL.setText(rb.getString("ui.error.field.required"));
            isValid = false;
        }
        this.locationHL.setText("");
        if(this.locationTF.getText().trim().isEmpty()){
            this.locationHL.setText(rb.getString("ui.error.field.required"));
            isValid = false;
        }
        this.contactHL.setText("");
        if(this.contactTF.getText().trim().isEmpty()){
            this.contactHL.setText(rb.getString("ui.error.field.required"));
            isValid = false;
        }
        this.typeHL.setText("");
        if(this.typeTF.getText().trim().isEmpty()){
            this.typeHL.setText(rb.getString("ui.error.field.required"));
            isValid = false;
        }
        this.urlHL.setText("");
        if(this.urlTF.getText().trim().isEmpty()){
            this.urlHL.setText(rb.getString("ui.error.field.required"));
            isValid = false;
        }
        this.startDPHL.setText("");
        if(this.startDP.getValue() == null){
            this.startDPHL.setText(rb.getString("ui.error.field.required"));
            isValid = false;
        }
        this.endDPHL.setText("");
        if(this.endDP.getValue() == null){
            this.endDPHL.setText(rb.getString("ui.error.field.required"));
            isValid = false;
        }
        return isValid;
    }

    private LocalDateTime getStartLocalDateTime() {
        LocalDate localDate = endDP.getValue();
        LocalTime localTime = LocalTime.of(endHourS.getValue(), endMintS.getValue());
        return LocalDateTime.of(localDate, localTime);
    }

    private LocalDateTime getEndLocalDateTime() {
        LocalDate localDate = startDP.getValue();
        LocalTime localTime = LocalTime.of(startHourS.getValue(), startMintS.getValue());
        return LocalDateTime.of(localDate, localTime);
    }

    public void setAppointment(Appointment selectedAppointment) {
        this.appointment = selectedAppointment;
        if(this.appointment != null){
            this.titleTF.setText(this.appointment.getTitle());
            this.descriptionTA.setText(this.appointment.getDescription());
            this.locationTF.setText(this.appointment.getLocation());
            this.contactTF.setText(this.appointment.getContact());
            this.typeTF.setText(this.appointment.getType());
            this.urlTF.setText(this.appointment.getUrl());
            this.startDP.setValue(this.appointment.getStart().toLocalDate());
            this.startHourS.getValueFactory().setValue(this.appointment.getStart().getHour());
            this.startMintS.getValueFactory().setValue(this.appointment.getStart().getMinute());
            this.endDP.setValue(this.appointment.getEnd().toLocalDate());
            this.endHourS.getValueFactory().setValue(this.appointment.getEnd().getHour());
            this.endMintS.getValueFactory().setValue(this.appointment.getEnd().getMinute());
            if(this.appointment.getCustomer() != null)
                this.setCustomer(this.appointment.getCustomer());
        }
    }
    
    public interface FormAction{
        public Long action(Appointment appointment);
    }


}
