/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.appointment;

import com.stone.rosetta.repository.model.Appointment;
import com.stone.rosetta.service.AppointmentService;
import com.stone.rosetta.throwable.EntityNotUpdatedException;
import com.stone.rosetta.view.ViewFactory;
import com.stone.rosetta.view.appointmentform.AppointmentFormController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.time.LocalDateTime;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.ComboBox;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class AppointmentController implements Initializable {

    private ResourceBundle rb;
    private AppointmentService appointmentService;

    @FXML
    private Button addButton;
    @FXML
    private Button viewButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button editButton;
    @FXML
    private Button calendarViewButton;

    @FXML
    private TableView<Appointment> appointmentTable;
    @FXML
    private TableColumn<Appointment, String> customerNameTC;
    @FXML
    private TableColumn<Appointment, String> userNameTC;
    @FXML
    private TableColumn<Appointment, String> titleTC;
    @FXML
    private TableColumn<Appointment, String> descriptionTC;
    @FXML
    private TableColumn<Appointment, String> locationTC;
    @FXML
    private TableColumn<Appointment, String> contactTC;
    @FXML
    private TableColumn<Appointment, String> typTC;
    @FXML
    private TableColumn<Appointment, String> urlTC;
    @FXML
    private TableColumn<Appointment, LocalDateTime> startTC;
    @FXML
    private TableColumn<Appointment, LocalDateTime> endTC;

    @FXML
    private ComboBox<String> timeComboBox;

    private ChangeListener<Appointment> selectedItemChangeListener;

    public AppointmentController() throws ClassNotFoundException {
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

    public void disableActionButtons(Boolean disable) {
        this.viewButton.setDisable(disable);
        this.editButton.setDisable(disable);
        this.deleteButton.setDisable(disable);
    }

    @FXML
    public void edit(Event event) {
        try {
            AppointmentFormController controller = ViewFactory.getInstance().getAppointmentForm();
            controller.setAppointment(appointmentTable.getSelectionModel().getSelectedItem());
            controller.setFormAction((appointment) -> {
                try {
                    this.appointmentService.save(appointment);
                    refreshTable();
                    return appointment.getId();
                } catch (SQLException | EntityNotUpdatedException | ClassNotFoundException ex) {
                    Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
                }
                return null;
            });
        } catch (IOException ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @FXML
    public void view(Event event) {
        edit(event);
    }

    @FXML
    public void delete(Event event) {
        Appointment a = appointmentTable.getSelectionModel().getSelectedItem();
        Alert alert = ViewFactory.getInstance().confirmDelete(rb.getString("delete.appointment.confirmation.message").replace("%s", a.getTitle()));
        alert.showAndWait().ifPresent(buttonType -> {
            if(buttonType != null && buttonType.getButtonData().equals(ButtonData.YES)){
                try {
                    appointmentService.deleteById(a.getId());
                    refreshTable();
                } catch (SQLException ex) {
                    Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
                }
            }
        });
    }

    private void loadData() {
        appointmentTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
        selectedItemChangeListener = (ObservableValue<? extends Appointment> observable, Appointment oldValue, Appointment newValue) -> {
            if (newValue != null) {
                disableActionButtons(Boolean.FALSE);
            } else {
                disableActionButtons(Boolean.TRUE);
            }
        };

        appointmentTable.getSelectionModel().selectedItemProperty().addListener(selectedItemChangeListener);
        setTableColumn();

        timeComboBox.getItems().setAll(rb.getString("today"), rb.getString("upcoming"), rb.getString("past"));
        timeComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            if (newValue != null) {
                if (newValue.equalsIgnoreCase(rb.getString("today"))) {
                    refreshTodayTable();
                } else if (newValue.equalsIgnoreCase(rb.getString("upcoming"))) {
                    refreshUpComingTable();
                } else if (newValue.equalsIgnoreCase(rb.getString("past"))) {
                    refreshPastTable();
                }
            }
        });
        timeComboBox.getSelectionModel().selectFirst();
    }

    private void setTableColumn() {
        customerNameTC.setCellValueFactory((cellData) -> {
            if (cellData.getValue() != null && cellData.getValue().getCustomer() != null) {
                return new SimpleStringProperty(cellData.getValue().getCustomer().getName());
            }
            return new SimpleStringProperty();
        });
        userNameTC.setCellValueFactory((cellData) -> {
            if (cellData.getValue() != null && cellData.getValue().getUser() != null) {
                return new SimpleStringProperty(cellData.getValue().getUser().getUsername());
            }
            return new SimpleStringProperty();
        });
        titleTC.setCellValueFactory(new PropertyValueFactory<>("title"));
        descriptionTC.setCellValueFactory(new PropertyValueFactory<>("description"));
        locationTC.setCellValueFactory(new PropertyValueFactory<>("location"));
        contactTC.setCellValueFactory(new PropertyValueFactory<>("contact"));
        typTC.setCellValueFactory(new PropertyValueFactory<>("type"));
        urlTC.setCellValueFactory(new PropertyValueFactory<>("url"));
        startTC.setCellValueFactory(new PropertyValueFactory<>("start"));
        endTC.setCellValueFactory(new PropertyValueFactory<>("end"));
    }

    private void refreshTodayTable() {
        this.appointmentTable.getItems().clear();
        try {
            this.appointmentTable.getItems().addAll(this.appointmentService.getAllToday());
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
            this.appointmentTable.setItems(FXCollections.emptyObservableList());
        }
    }

    private void refreshUpComingTable() {
        this.appointmentTable.getItems().clear();
        try {
            this.appointmentTable.getItems().addAll(this.appointmentService.getUpCommingAll());
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
            this.appointmentTable.setItems(FXCollections.emptyObservableList());
        }
    }

    private void refreshPastTable() {
        this.appointmentTable.getItems().clear();
        try {
            this.appointmentTable.getItems().addAll(this.appointmentService.getPastAll());
        } catch (SQLException ex) {
            Logger.getLogger(AppointmentController.class.getName()).log(Level.SEVERE, null, ex);
            this.appointmentTable.setItems(FXCollections.emptyObservableList());
        }
    }

    private void refreshTable() {
        String newValue = this.timeComboBox.getSelectionModel().getSelectedItem();
        if (newValue.equalsIgnoreCase(rb.getString("today"))) {
            refreshTodayTable();
        } else if (newValue.equalsIgnoreCase(rb.getString("upcoming"))) {
            refreshUpComingTable();
        } else if (newValue.equalsIgnoreCase(rb.getString("past"))) {
            refreshPastTable();
        }
    }

}
