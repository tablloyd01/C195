/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.calendarview;

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
import javafx.scene.control.ComboBox;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class CalendarViewController implements Initializable {

    @FXML
    private ComboBox<String> viewsComboBox;

    @FXML
    private BorderPane borderPane;
    private ResourceBundle rb;

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
        this.viewsComboBox.getItems().addAll(rb.getString("monthly.view"), rb.getString("weekly.view"));
        this.viewsComboBox.getSelectionModel().selectedItemProperty().addListener(new ChangeListener<String>() {
            @Override
            public void changed(ObservableValue<? extends String> observable, String oldValue, String newValue) {
                if (newValue != null && !newValue.trim().isEmpty()) {
                    changeView(newValue);
                }
            }
        });
        this.viewsComboBox.getSelectionModel().selectFirst();
    }

    private void changeView(String newValue) {
        Pane pane = null;
        try {
            if (newValue.equalsIgnoreCase(rb.getString("monthly.view"))) {
                pane = ViewFactory.getInstance().getCalendarMonthlyView();
            } else if (newValue.equalsIgnoreCase(rb.getString("weekly.view"))) {
                pane = ViewFactory.getInstance().getCalendarWeeklyView();
            }
            if(pane != null){
                this.borderPane.setCenter(pane);
            }
        } catch (IOException ex) {
            Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

}
