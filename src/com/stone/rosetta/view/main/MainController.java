/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.main;

import com.stone.rosetta.service.UserAuthenticationService;
import com.stone.rosetta.view.AbstractController;
import com.stone.rosetta.view.ViewFactory;
import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ListView;
import javafx.scene.control.SelectionMode;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

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

    public MainController() {

        this.menuChangeListener = (ObservableValue<? extends String> observable, String oldValue, String newValue) -> {
            changePane(newValue);
        };
        try {
            authenticationService = UserAuthenticationService.getInstance();
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
        menuList.getItems().add(rb.getString("users"));
        menuList.getSelectionModel().selectedItemProperty().addListener(menuChangeListener);
        menuList.getSelectionModel().select(rb.getString("appointments"));
//        menu list
    }

    private void changePane(String newValue) {
        try {
            Pane pane = null;
            if (newValue.equalsIgnoreCase(rb.getString("users"))) {
                pane = ViewFactory.getInstance().getUsersPane();
            } else if (newValue.equalsIgnoreCase(rb.getString("customers"))) {
                pane = ViewFactory.getInstance().getCustomersPane();
            } else if (newValue.equalsIgnoreCase(rb.getString("appointments"))) {
                pane = ViewFactory.getInstance().getAppointmentPane();
            } else if (newValue.equalsIgnoreCase(rb.getString("calendar"))) {
                pane = ViewFactory.getInstance().getCalendarViewPane();
            }
            if (pane != null) {
                borderPane.setCenter(pane);
            }
        } catch (IOException ex) {
            Logger.getLogger(MainController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
