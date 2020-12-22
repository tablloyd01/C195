/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.customer;

import com.stone.rosetta.repository.model.Customer;
import com.stone.rosetta.service.AppointmentService;
import com.stone.rosetta.service.CustomerService;
import com.stone.rosetta.throwable.EntityNotUpdatedException;
import com.stone.rosetta.view.ViewFactory;
import com.stone.rosetta.view.appointmentform.AppointmentFormController;
import com.stone.rosetta.view.customerform.CustomerFormController;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar.ButtonData;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class CustomerController implements Initializable {

    @FXML
    private Button viewButton;
    @FXML
    private Button editButton;
    @FXML
    private Button deleteButton;
    @FXML
    private Button addButton;
    
    @FXML
    private Button addAppointmentButton;

    @FXML
    private TableView<Customer> customerTable;
    @FXML
    private TableColumn<Customer, String> customerNameTableColumn;
    @FXML
    private TableColumn<Customer, Boolean> customerActiveTableColumn;
    @FXML
    private TableColumn<Customer, String> phoneTableColumn;
    @FXML
    private TableColumn<Customer, String> postalCodeTableColumn;
    @FXML
    private TableColumn<Customer, String> addressLine1TableColumn;
    @FXML
    private TableColumn<Customer, String> addressLine2TableColumn;
    @FXML
    private TableColumn<Customer, String> cityTableColumn;
    @FXML
    private TableColumn<Customer, String> countryTableColumn;

    private CustomerService customerService;
    private AppointmentService appointmentService;
    private ResourceBundle rb;

    public CustomerController() {
        try {
            appointmentService = new AppointmentService();
            customerService = new CustomerService();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
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
        this.addAppointmentButton.setDisable(false);
    }

    private void loadData() {
        setTableColumn();
        refreshTable();
        customerTable.getSelectionModel().setSelectionMode(SelectionMode.SINGLE);
//       lambda function, on customer table selection listener
        customerTable.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Customer> observable, Customer oldValue, Customer newValue) -> {
            if (newValue != null) {
                disableActionButtons(Boolean.FALSE);
            } else {
                disableActionButtons(Boolean.TRUE);
            }
        });
        
        customerTable.setOnMouseClicked((MouseEvent event) -> {
            if (event.getButton().equals(MouseButton.PRIMARY) && event.getClickCount() == 2){
                edit(event);
            }
        });
    }

    private void setTableColumn() {
        customerNameTableColumn.setCellValueFactory(new PropertyValueFactory("name"));
        customerActiveTableColumn.setCellValueFactory(new PropertyValueFactory("active"));
//        call back lambda function to set cell value factory
        addressLine1TableColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getAddress() != null) {
                return new SimpleStringProperty(cellData.getValue().getAddress().getLine1());
            }
            return new SimpleStringProperty();
        });
//        call back lambda function to set cell value factory
        addressLine2TableColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getAddress() != null && cellData.getValue().getAddress().getLine2() != null) {
                return new SimpleStringProperty(cellData.getValue().getAddress().getLine2());
            }
            return new SimpleStringProperty();
        });
//        call back lambda function to set cell value factory
        phoneTableColumn.setCellValueFactory(cellDate -> {
            return new SimpleStringProperty(cellDate.getValue().getAddress().getPhone());
        });
//        call back lambda function to set cell value factory
        postalCodeTableColumn.setCellValueFactory(cellDate -> {
            return new SimpleStringProperty(cellDate.getValue().getAddress().getPostalCode());
        });
//        call back lambda function to set cell value factory
        cityTableColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getAddress() != null && cellData.getValue().getAddress().getCity() != null) {
                return new SimpleStringProperty(cellData.getValue().getAddress().getCity().getName());
            }
            return new SimpleStringProperty();
        });
        countryTableColumn.setCellValueFactory(cellData -> {
            if (cellData.getValue().getAddress() != null && cellData.getValue().getAddress().getCity().getCountry() != null) {
                return new SimpleStringProperty(cellData.getValue().getAddress().getCity().getCountry().getName());
            }
            return new SimpleStringProperty();
        });
    }

    @FXML
    public void view(Event event) {
        edit(event);
    }

    @FXML
    public void edit(Event event) {
        openCustomerDialogForm(true, "edit.customer");
    }

    @FXML
    public void delete(Event event) {
        Customer cs = customerTable.getSelectionModel().selectedItemProperty().get();
        Alert alert = ViewFactory.getInstance().confirmDelete(rb.getString("delete.customer.confirmation.message").replace("%s", cs.getName()));
        alert.showAndWait().ifPresent(buttonType -> {
            if(buttonType != null && buttonType.getButtonData().equals(ButtonData.YES)){
                try {
                    customerService.deleteById(cs.getId());
                    refreshTable();
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
                }                
            }
        });
    }

    @FXML
    public void add(Event event) {
        openCustomerDialogForm(false, "add.customer");
    }

    private void refreshTable() {
        if (customerTable.getItems() != null) {
            customerTable.getItems().clear();
        } else {
            customerTable.setItems(FXCollections.emptyObservableList());
        }
        try {
            customerTable.getItems().addAll(customerService.getAll());
        } catch (SQLException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private void openCustomerDialogForm(boolean isEdit, String title) {
        try {
            CustomerFormController controller = ViewFactory.getInstance().showCustomerForm(rb.getString(title));
            if (isEdit) {
                controller.setCustomer(customerTable.getSelectionModel().getSelectedItem());
            }
            controller.setCustomerFormAction((Customer cs) -> {
                Customer customer = null;
                try {
                    customer = customerService.save(cs);
                    refreshTable();
                } catch (SQLException ex) {
                    Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
                } catch (EntityNotUpdatedException ex) {
                    Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
                }
                if (customer != null) {
                    return customer.getId();
                }
                return null;
            });
        } catch (IOException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    

    
    @FXML
    public void addAppointment(Event event){
        AppointmentFormController controller;
        try {
            controller = ViewFactory.getInstance().getAppointmentForm();
            controller.setCustomer(customerTable.getSelectionModel().selectedItemProperty().get());
            controller.setFormAction((appointment) -> {
                try {
                    appointmentService.save(appointment);
                    return appointment.getId();
                } catch (SQLException | EntityNotUpdatedException | ClassNotFoundException ex) {
                    Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
                    throw ex;
                }
            });
        } catch (IOException ex) {
            Logger.getLogger(CustomerController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
