/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.calendarview;

import java.net.URL;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import javafx.beans.value.ObservableValue;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.layout.ColumnConstraints;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;
import javafx.scene.layout.RowConstraints;
import javafx.scene.layout.VBox;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class CalendarViewController implements Initializable {
    
    
    @FXML
    private ComboBox<Year> yearComboBox;
    @FXML
    private ComboBox<Month> monthComboBox;
    
    @FXML
    private GridPane calendarViewPane;
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
        monthComboBox.getItems().addAll(Month.values());
        monthComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Month> observable, Month oldValue, Month newValue) -> {
            if(newValue != null)
                generateGrid();
        });
        yearComboBox.getItems().addAll(getYears());
        yearComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Year> observable, Year oldValue, Year newValue) -> {
            if(newValue != null)
                generateGrid();
        });
        yearComboBox.getSelectionModel().select(getCurrentYear());
        monthComboBox.getSelectionModel().select(getCurrentMonth());
    }

    private List<Year> getYears() {
        List<Year> years = new ArrayList<>();
        for(int i = LocalDate.now().plusMonths(2).getYear(); i > Year.now().minusYears(10).getValue(); i--){
            years.add(Year.of(i));
        }
        return years;
    }

    private Year getCurrentYear() {
        return Year.of(LocalDate.now().getYear());
    }

    private Month getCurrentMonth() {
        return LocalDate.now().getMonth();
    }

    private void generateGrid() {
        Month selectedMonth = this.monthComboBox.getSelectionModel().getSelectedItem();
        Year year = this.yearComboBox.getSelectionModel().getSelectedItem();
        this.calendarViewPane.getChildren().clear();
        if(selectedMonth != null && year != null){
//            this.calendarViewPane.getColumnConstraints().add(new ColumnConstraints(100, 100, Double.MAX_VALUE, Priority.ALWAYS, HPos.CENTER, true));
//            this.calendarViewPane.getRowConstraints().add(new RowConstraints(100, 100, Double.MAX_VALUE, Priority.ALWAYS, VPos.CENTER, true));
            LocalDate localDate = LocalDate.of(year.getValue(), selectedMonth.getValue(), 1);
            int initColumn = LocalDate.of(localDate.getYear(), localDate.getMonthValue(), 1).getDayOfWeek().getValue()-1;
            int row = 1;
            int iteration = 0;
            int lastDay = localDate.plusMonths(1).minusDays(1).getDayOfMonth();
            int weekdays = 0;
            while(weekdays < 7){
                calendarViewPane.add(getWeekdays(weekdays), weekdays, 0);
                weekdays++;
            }
            while(iteration < lastDay){
                if(initColumn > 6){
                    initColumn = 0;
                    row++;
                }
                Button node = getDayView(iteration + 1);
                calendarViewPane.add(node, initColumn, row);
                initColumn++;
                iteration++;
            }
//            calendarViewPane.setGridLinesVisible(true);
        }
    }

    private Button getDayView(int i) {
        Button button = new Button(String.valueOf(i));
        button.maxHeight(Double.MAX_VALUE);
        button.maxWidth(Double.MAX_VALUE);
        button.prefWidth(200);
        GridPane.setFillWidth(button, Boolean.TRUE);
        GridPane.setFillHeight(button, Boolean.TRUE);
        GridPane.setHgrow(button, Priority.ALWAYS);
        GridPane.setVgrow(button, Priority.ALWAYS);
        return button;
    }

    private Node getWeekdays(int weekdays) {
        return new Label(DayOfWeek.of(weekdays + 1).name());
    }
    
}
