/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.calendarview.weekly;

import com.stone.rosetta.repository.model.Appointment;
import com.stone.rosetta.service.AppointmentService;
import com.stone.rosetta.view.calendarview.weekly.model.WeeklyAppointment;
import java.net.URL;
import java.sql.SQLException;
import java.time.DayOfWeek;
import java.time.LocalDate;
import java.time.Year;
import java.time.format.DateTimeFormatter;
import java.time.temporal.IsoFields;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.List;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.beans.value.ObservableValue;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class WeeklyViewController implements Initializable {

    @FXML
    private ComboBox<Year> yearComboBox;
    @FXML
    private ComboBox<Integer> weekComboBox;

    @FXML
    private Label monthLabel;

    @FXML
    private TableView<WeeklyAppointment> appointmentTable;
    @FXML
    private TableColumn<WeeklyAppointment, String> mondayTC;
    @FXML
    private TableColumn<WeeklyAppointment, String> tuedayTC;
    @FXML
    private TableColumn<WeeklyAppointment, String> wednesdayTC;
    @FXML
    private TableColumn<WeeklyAppointment, String> thursdayTC;
    @FXML
    private TableColumn<WeeklyAppointment, String> fridayTC;
    @FXML
    private TableColumn<WeeklyAppointment, String> saturdayTC;
    @FXML
    private TableColumn<WeeklyAppointment, String> sundayTC;
    
    private ResourceBundle rb;
    private AppointmentService appointmentService;

    public WeeklyViewController() throws ClassNotFoundException {
        this.appointmentService = new AppointmentService();
    }

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        this.rb = rb;
        loadData();
    }

    @FXML
    public void nextYear(Event event) {
        yearComboBox.getSelectionModel().selectPrevious();
    }

    @FXML
    public void previousYear(Event event) {
        yearComboBox.getSelectionModel().selectNext();
    }

    @FXML
    public void nextWeek(Event event) {
        weekComboBox.getSelectionModel().selectPrevious();
    }

    @FXML
    public void previousWeek(Event event) {
        weekComboBox.getSelectionModel().selectNext();
    }

    private void loadData() {
        setTableColumn();
        weekComboBox.getItems().addAll(getWeeks());
        yearComboBox.getItems().addAll(getYears());
        weekComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Integer> observable, Integer oldValue, Integer newValue) -> {
            if (newValue != null) {
                populateTable();
            }
        });
        yearComboBox.getSelectionModel().selectedItemProperty().addListener((ObservableValue<? extends Year> observable, Year oldValue, Year newValue) -> {
            if (newValue != null) {
                populateTable();
            }
        });
        weekComboBox.getSelectionModel().select(getCurrentWeek());
        yearComboBox.getSelectionModel().select(getCurrentYear());

    }

    List<Appointment> appointments = null;

    private void populateTable() {
        if (!yearComboBox.getSelectionModel().isEmpty()
                && !weekComboBox.getSelectionModel().isEmpty()) {
            int year = yearComboBox.getSelectionModel().getSelectedItem().getValue();
            int week = weekComboBox.getSelectionModel().getSelectedItem().intValue();
            if (year != 0 && week != 0) {
                LocalDate desiredDate = LocalDate.now()
                        .withYear(year)
                        .with(IsoFields.WEEK_OF_WEEK_BASED_YEAR, week)
                        .with(TemporalAdjusters.previousOrSame(DayOfWeek.MONDAY));
                setMonthLabel(desiredDate);
                appointmentTable.getItems().clear();
                try {
                    appointments = this.appointmentService.getAllByWeek(desiredDate);
                    appointmentTable.getItems().addAll(getWeeklyAppointments(appointments));
                } catch (SQLException ex) {
                    Logger.getLogger(WeeklyViewController.class.getName()).log(Level.SEVERE, null, ex);
                }
                DateTimeFormatter columnDateFormatter = DateTimeFormatter.ofPattern("dd E");
                for (int i = 0; i < 7; i++) {
                    appointmentTable.getColumns().get(i).setText(desiredDate.format(columnDateFormatter));
                    desiredDate = desiredDate.plusDays(1);
                }

            }
        }
    }

    private List<Year> getYears() {
        List<Year> years = new ArrayList<>();
        for (int i = LocalDate.now().plusMonths(2).getYear(); i > Year.now().minusYears(10).getValue(); i--) {
            years.add(Year.of(i));
        }
        return years;
    }

    private Year getCurrentYear() {
        return Year.of(LocalDate.now().getYear());
    }

    private Integer getCurrentWeek() {
        return Calendar.getInstance().get(Calendar.WEEK_OF_YEAR);
    }

    private List<Integer> getWeeks() {
        List<Integer> weeks = new ArrayList<>();
        for (int i = Calendar.getInstance().getMaximum(Calendar.WEEK_OF_YEAR); i > 0; i--) {
            weeks.add(i);
        }
        return weeks;
    }

    private List<WeeklyAppointment> getWeeklyAppointments(List<Appointment> appointments) {
        List<WeeklyAppointment> weeklyAppointments = new ArrayList<>();
        List<String> mondays = new ArrayList<>();
        appointments.forEach(a -> {
            addAppointmentToWeeklyAppointment(a, weeklyAppointments);
        });
        return weeklyAppointments;
    }

    private void addAppointmentToWeeklyAppointment(Appointment a, List<WeeklyAppointment> weeklyAppointments) {
        int i = 0;
        added_loop:
        do {
            if(i > (weeklyAppointments.size()-1)){
                weeklyAppointments.add(new WeeklyAppointment());                
            }
            
            WeeklyAppointment wa = weeklyAppointments.get(i);
            switch (a.getStart().getDayOfWeek()) {
                case MONDAY:
                    if (wa.getMonday() == null) {
                        wa.setMonday(getAppointmentTimeWithTitle(a));
                        break added_loop;
                    } else {
                        i++;
                    }   break;
                case TUESDAY:
                    if (wa.getTuesday()== null) {
                        wa.setTuesday(getAppointmentTimeWithTitle(a));
                        break added_loop;
                    } else {
                        i++;
                    }   break;
                case WEDNESDAY:
                    if (wa.getWednesday() == null) {
                        wa.setWednesday(getAppointmentTimeWithTitle(a));
                        break added_loop;
                    } else {
                        i++;
                    }   break;
                case THURSDAY:
                    if (wa.getThursday()== null) {
                        wa.setThursday(getAppointmentTimeWithTitle(a));
                        break added_loop;
                    } else {
                        i++;
                    }   break;
                case FRIDAY:
                    if (wa.getFriday() == null) {
                        wa.setFriday(getAppointmentTimeWithTitle(a));
                        break added_loop;
                    } else {
                        i++;
                    }   break;
                case SATURDAY:
                    if (wa.getSaturday()== null) {
                        wa.setSaturday(getAppointmentTimeWithTitle(a));
                        break added_loop;
                    } else {
                        i++;
                    }   break;
                case SUNDAY:
                    if (wa.getSunday()== null) {
                        wa.setSunday(getAppointmentTimeWithTitle(a));
                        break added_loop;
                    } else {
                        i++;
                    }   break;
                default:
                    break;
            }
        } while (true);
    }

    private void setTableColumn() {
        mondayTC.setCellValueFactory(new PropertyValueFactory("monday"));
        tuedayTC.setCellValueFactory(new PropertyValueFactory("tuesday"));
        wednesdayTC.setCellValueFactory(new PropertyValueFactory("wednesday"));
        thursdayTC.setCellValueFactory(new PropertyValueFactory("thursday"));
        fridayTC.setCellValueFactory(new PropertyValueFactory("friday"));
        saturdayTC.setCellValueFactory(new PropertyValueFactory("saturday"));
        sundayTC.setCellValueFactory(new PropertyValueFactory("sunday"));
    }

    private String getAppointmentTimeWithTitle(Appointment a) {
        return a.getStart().format(DateTimeFormatter.ISO_TIME).concat("-").concat(a.getTitle());
    }

    private void setMonthLabel(LocalDate desiredDate) {
        StringBuilder sb = new StringBuilder();
        DateTimeFormatter df = DateTimeFormatter.ofPattern("MMM dd, yyyy");
        sb.append(desiredDate.format(df))
                .append(" - ")
                .append(desiredDate.plusDays(6).format(df));
        monthLabel.setText(sb.toString());
    }

}
