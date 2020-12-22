/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.calendarview.monthly;

import com.stone.rosetta.repository.model.Appointment;
import com.stone.rosetta.service.AppointmentService;
import com.stone.rosetta.view.calendarview.CalendarViewController;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Month;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.geometry.HPos;
import javafx.geometry.Insets;
import javafx.geometry.VPos;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.layout.GridPane;
import javafx.scene.layout.Priority;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class MonthlyViewController implements Initializable {

    /**
     * Initializes the controller class.
     */
    
       @FXML
    private ComboBox<Year> yearComboBox;
    @FXML
    private ComboBox<Month> monthComboBox;
    
    @FXML
    private GridPane calendarViewPane;
    private ResourceBundle rb;
    
    private AppointmentService appointmentService;

    public MonthlyViewController() throws ClassNotFoundException {
        this.appointmentService = new AppointmentService();
    }
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

    private List<Appointment> appointments;
    
    private void generateGrid() {
        Month selectedMonth = this.monthComboBox.getSelectionModel().getSelectedItem();
        Year year = this.yearComboBox.getSelectionModel().getSelectedItem();
        this.calendarViewPane.getChildren().clear();
        if(selectedMonth != null && year != null){
            
            LocalDate localDate = LocalDate.of(year.getValue(), selectedMonth.getValue(), 1);
            
            try {
                appointments = this.appointmentService.getByMonth(localDate);
            } catch (SQLException ex) {
                Logger.getLogger(CalendarViewController.class.getName()).log(Level.SEVERE, null, ex);
            }
            
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
//                Node node = getDayView(iteration + 1);
                Node node = getListView(LocalDate.of(localDate.getYear(), localDate.getMonthValue(), iteration+1));
                GridPane.setConstraints(node, initColumn, row, 1, 1, HPos.CENTER, VPos.CENTER, Priority.ALWAYS, Priority.ALWAYS, Insets.EMPTY);
                calendarViewPane.add(node, initColumn, row);
                initColumn++;
                iteration++;
            }
            
            calendarViewPane.getChildren().forEach(node -> {
                if(node instanceof Button){
                    Button button = ((Button) node);
                    button.setMaxSize(Double.MAX_VALUE, Double.MAX_VALUE);
                }
            });
            
//            calendarViewPane.setGridLinesVisible(true);
        }
    }
    
    private TableView<Appointment> getListView(LocalDate localDate){
        TableView<Appointment> view=  new TableView<>();
//        view.columnResizePolicyProperty().setValue((Callback<TableView.ResizeFeatures, Boolean>) (TableView.ResizeFeatures param) -> false);
        TableColumn<Appointment, String> tableColumn = new TableColumn<>(String.valueOf(localDate.getDayOfMonth()));
        tableColumn.setCellValueFactory((cellData) -> {
            if(cellData == null || cellData.getValue() == null)
                return new SimpleStringProperty();
            else{
                Appointment a = cellData.getValue();
                String s = a.getStart().toLocalTime().format(DateTimeFormatter.ISO_TIME).concat("-").concat(a.getTitle());
                return new SimpleStringProperty(s);
            }
        });
        view.getColumns().add(tableColumn);
        List<Appointment> filtered = new ArrayList<>();
        if(appointments != null)
            appointments.stream().filter(a -> a.getStart().toLocalDate().equals(localDate)).forEach(a -> filtered.add(a));
        view.getItems().addAll(filtered);
        view.setColumnResizePolicy(TableView.CONSTRAINED_RESIZE_POLICY);
        return view;
    }

    private Node getWeekdays(int weekdays) {
        Label label = new Label(DayOfWeek.of(weekdays + 1).name());
        javafx.scene.text.Font font = label.getFont();
        label.setFont(javafx.scene.text.Font.font("System Bold", 14));
        return label;
    }

    @FXML    
    public void yearNext(Event event){
        yearComboBox.getSelectionModel().selectPrevious();
    }
    
    @FXML    
    public void yearPrevious(Event event){
        yearComboBox.getSelectionModel().selectNext();
    }
    
    @FXML    
    public void monthNext(Event event){
        monthComboBox.getSelectionModel().selectNext();
    }
    
    @FXML    
    public void monthPrevious(Event event){
        monthComboBox.getSelectionModel().selectPrevious();
    }
    
}
