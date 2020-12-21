/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.customerform;

import com.stone.rosetta.repository.model.Address;
import com.stone.rosetta.repository.model.City;
import com.stone.rosetta.repository.model.Country;
import com.stone.rosetta.repository.model.Customer;
import com.stone.rosetta.service.AddressService;
import com.stone.rosetta.service.CityService;
import com.stone.rosetta.service.CountryService;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.collections.transformation.FilteredList;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.CheckBox;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.ListCell;
import javafx.scene.control.ListView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import javafx.util.Callback;
import javafx.util.StringConverter;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class CustomerFormController implements Initializable {    

    private ResourceBundle rb;
    private Stage stage;
    private Customer customer;
    
    @FXML
    private TextField customerNameTextField;
    @FXML
    private Label customerNameHintLabel;
    @FXML
    private CheckBox customerActiveCheckBox;
    @FXML
    private ComboBox<Country> countryComboBox;
    @FXML
    private ComboBox<City> cityComboBox;
    @FXML
    private TextField addressLine1TextField;
    @FXML
    private Label addressLine1HintLabel;
    @FXML
    private TextField addressLine2TextField;
    @FXML
    private Label addressLine2HintLabel;
    @FXML
    private TextField phoneTextField;
    @FXML
    private Label phoneHintLabel;
    @FXML
    private TextField postalCodeTextField;
    @FXML
    private Label postalCodeHintLabel;

    private CountryService countryService;
    private CityService cityService;
    
    private Callback<ListView<Country>, ListCell<Country>> countryCellValue = (ListView<Country> param) -> {
                return new ListCell<Country>(){
                    @Override
                    protected void updateItem(Country item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item == null || empty ){
                            setGraphic(null);
                        }else
                            setText(item.getName());
                    }
                };
            };
    private Callback<ListView<City>, ListCell<City>> cityCellValue = (ListView<City> param) -> {
                return new ListCell<City>(){
                    @Override
                    protected void updateItem(City item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item == null || empty ){
                            setGraphic(null);
                        }else
                            setText(item.getName());
                    }
                };
            };
    private Callback<ListView<Address>, ListCell<Address>> addressCellValue = (ListView<Address> param) -> {
                return new ListCell<Address>(){
                    @Override
                    protected void updateItem(Address item, boolean empty) {
                        super.updateItem(item, empty);
                        if(item == null || empty ){
                            setGraphic(null);
                        }else
                            setText(item.getLine1().concat(" ").concat(item.getLine2()));
                    }
                };
            };
    private StringConverter<Country> countryConverter = new StringConverter<Country>() {
        @Override
        public String toString(Country object) {
            if(object != null)
               return object.getName();
            return null;
        }

        @Override
        public Country fromString(String string) {
            FilteredList<Country> filtered = countryComboBox.getItems().filtered((Country c) -> c.getName().equalsIgnoreCase(string));
            if(filtered != null && filtered.size() > 0)
                return filtered.get(0);
            return null;
        }
    };
    private StringConverter<City> cityConverter = new StringConverter<City>() {
        @Override
        public String toString(City object) {
            if(object != null)
                return object.getName();
            return null;
        }

        @Override
        public City fromString(String string) {
            FilteredList<City> cities = cityComboBox.getItems().filtered((City t) -> t.getName().equalsIgnoreCase(string));
            if(cities != null && cities.size() > 0)
                return cities.get(0);
            return null;
        }
    };
    

    public CustomerFormController() {
        try {
            this.cityService = new CityService();
            this.countryService = new CountryService();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    
    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        loadData();
    }    

    public void setCustomer(Customer selectedCustomer) {
        System.out.println("setCustomer");
        if(selectedCustomer != null){
            this.customer = selectedCustomer;
            this.customerNameTextField.setText(this.customer.getName());
            this.customerActiveCheckBox.setSelected(this.customer.getActive());
            if(selectedCustomer != null && selectedCustomer.getAddress() != null
                    && selectedCustomer.getAddress().getCity() != null
                    && selectedCustomer.getAddress().getCity().getCountry() != null){              
                this.addressLine1TextField.setText(this.customer.getAddress().getLine1());
                this.addressLine2TextField.setText(this.customer.getAddress().getLine2());
                this.postalCodeTextField.setText(this.customer.getAddress().getPostalCode());
                this.phoneTextField.setText(this.customer.getAddress().getPhone());
                FilteredList<Country> filtered = this.countryComboBox.getItems().filtered((Country c) -> c.getId() == selectedCustomer.getAddress().getCity().getCountry().getId());
                if(filtered != null && filtered.size() > 0){
                    this.countryComboBox.getSelectionModel().select(filtered.get(0));                    
                }
            }
        }
    }
    
    public void setStage(Stage stage) {
        this.stage = stage;
    }
    
    @FXML
    public void close(Event event){
        this.stage.close();
    }
    
    @FXML
    public void saveAndClose(Event event){
        save(event);
        close(event);
    }

    @FXML
    public void save(Event event) {
        if(this.customerFormAction != null && isFormValid()){
            if(customer == null)
                customer = new Customer();
            customer.setName(customerNameTextField.getText().trim());
            customer.setActive(customerActiveCheckBox.isSelected());
            if(customer.getAddress() == null)
                customer.setAddress(new Address());
            customer.getAddress().setLine1(this.addressLine1TextField.getText());
            customer.getAddress().setLine2(this.addressLine2TextField.getText());
            customer.getAddress().setPhone(this.phoneTextField.getText());
            customer.getAddress().setPostalCode(this.postalCodeTextField.getText());
            customer.getAddress().setCity(cityComboBox.getSelectionModel().getSelectedItem());
            this.customerFormAction.action(customer);
        }
    }
    
    private FormAction customerFormAction;

    public void setCustomerFormAction(FormAction customerFormAction) {
        this.customerFormAction = customerFormAction;
    }
    

    private void loadData() {
        System.out.println("loadDate");
        try {
            this.countryComboBox.setCellFactory(countryCellValue);
            this.countryComboBox.setConverter(countryConverter);
            this.cityComboBox.setCellFactory(cityCellValue);
            this.cityComboBox.setConverter(cityConverter);
            
            this.countryComboBox.getItems().addAll(countryService.getAll());
            this.countryComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Country> observable, Country oldValue, Country newValue) -> {
                if(newValue != null){
                    loadCityComboBox(newValue.getId());
                }
            });
            this.countryComboBox.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
        System.out.println("loadDate-end");
    }

    private void loadCityComboBox(Long id) {
        System.out.println("loadCityComboBox");
        cityComboBox.setDisable(false);
        cityComboBox.getSelectionModel().clearSelection();
        try {
            cityComboBox.getItems().clear();
            cityComboBox.getItems().addAll(cityService.getAllByCountryId(id));
            if(customer != null && customer.getAddress() != null
                    && customer.getAddress().getCity() != null){              
                FilteredList<City> filtered = this.cityComboBox.getItems().filtered((City c) -> c.getId() == customer.getAddress().getCity().getId());
                if(filtered != null && filtered.size() > 0){
                    this.cityComboBox.getSelectionModel().select(filtered.get(0));
                    return;
                }
            }
            cityComboBox.getSelectionModel().selectFirst();
        } catch (SQLException ex) {
            Logger.getLogger(CustomerFormController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    private boolean isFormValid() {
        boolean isValid = true;
        this.customerNameHintLabel.setText("");
        this.addressLine1HintLabel.setText("");
        this.addressLine2HintLabel.setText("");
        this.phoneHintLabel.setText("");
        this.postalCodeHintLabel.setText("");
        if(this.customerNameTextField.getText().isEmpty()){
            this.customerNameHintLabel.setText(rb.getString("ui.error.field.required"));
            isValid = false;
            
        }
        if(this.addressLine1TextField.getText().isEmpty()){
            this.addressLine1HintLabel.setText(rb.getString("ui.error.field.required"));
            isValid = false;
            
        }
        if(this.addressLine2TextField.getText().isEmpty()){
            this.addressLine2HintLabel.setText(rb.getString("ui.error.field.required"));
            isValid = false;
            
        }
        if(this.phoneTextField.getText().isEmpty()){
            this.phoneHintLabel.setText(rb.getString("ui.error.field.required"));
            isValid = false;
            
        }
        if(this.postalCodeTextField.getText().isEmpty()){
            this.postalCodeHintLabel.setText(rb.getString("ui.error.field.required"));
            isValid = false;
        }
        return isValid;
    }
    
    public interface FormAction{
        public Long action(Customer customer);
    }
}
