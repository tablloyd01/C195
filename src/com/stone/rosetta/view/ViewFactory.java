/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view;

import com.stone.rosetta.FXMLView;
import com.stone.rosetta.util.ResourceBundlerUtil;
import com.stone.rosetta.view.appointmentform.AppointmentFormController;
import com.stone.rosetta.view.customerform.CustomerFormController;
import com.stone.rosetta.view.login.LoginController;
import com.stone.rosetta.view.main.MainController;
import java.io.IOException;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.stage.StageStyle;

/**
 *
 * @author jeeva
 */
public class ViewFactory {

    private static ViewFactory viewFactory;
    
    private ViewFactory(){}
    
    public static ViewFactory getInstance(){
        if(viewFactory == null)
            viewFactory = new ViewFactory();
        return viewFactory;
    } 
    
    public LoginController login() throws IOException{
        FXMLLoader fxmlLoader = getFXMLLoader(FXMLView.LOGIN_FXML_LOCATION);
        Stage stage = new Stage();
        Pane root = fxmlLoader.load();
        LoginController loginController = fxmlLoader.getController();
        loginController.setStage(stage);
        if(this.parentStage != null)
            stage.initOwner(this.parentStage);
        stage.setScene(new Scene(root));
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.initStyle(StageStyle.UNDECORATED);
        stage.showAndWait();
        return loginController;
    }

    private Stage parentStage;
    
    public MainController main(Stage stage) throws IOException {
        this.parentStage = stage;
        return main();

    }

    public MainController main() throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(FXMLView.MAIN_FXML_LOCATION);
        Pane root = fxmlLoader.load();
        MainController controller = fxmlLoader.getController();
        controller.setStage(parentStage);
        parentStage.setScene(new Scene(root));
        parentStage.setTitle(ResourceBundlerUtil.getResourceBundle().getString("app.title"));
        parentStage.show();
        return controller;
    }

    public void setStage(Stage stage) {
        this.parentStage = stage;
    }
    
    public AnchorPane getCalendarViewPane() throws IOException {
        System.out.println("getCalendarViewPane");
        FXMLLoader fxmlLoader = getFXMLLoader(FXMLView.CALENDAR_VIEW_FXML_LOCATION);
        return fxmlLoader.load();
    }

    public BorderPane getUsersPane() throws IOException {
        System.out.println("getUsersPane");
        FXMLLoader fxmlLoader = getFXMLLoader(FXMLView.USER_FXML_LOCATION);
        return fxmlLoader.load();
    }

    public BorderPane getCustomersPane() throws IOException {
        System.out.println("getCustomersPane");
        FXMLLoader fxmlLoader = getFXMLLoader(FXMLView.CUSTOMER_FXML_LOCATION);
        return fxmlLoader.load();
    }

    public BorderPane getAppointmentPane() throws IOException {
        System.out.println("getAppointmentPane");
        FXMLLoader fxmlLoader = getFXMLLoader(FXMLView.APPOINTMENT_FXML_LOCATION);
        return fxmlLoader.load();
    }

    private FXMLLoader getFXMLLoader(String location) {
        return new FXMLLoader(getClass().getResource(location),
                ResourceBundlerUtil.getResourceBundle());
    }

    public CustomerFormController showCustomerForm(String title) throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(FXMLView.CUSTOMER_FORM_FXML_LOCATION);
        AnchorPane root = fxmlLoader.load();
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle(title);
        stage.setScene(new Scene(root));
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
        CustomerFormController controller = fxmlLoader.getController();
        controller.setStage(stage);
        return controller;
    }
    
    public Alert confirmDelete(String message){
        return new Alert(Alert.AlertType.CONFIRMATION, message, ButtonType.NO, ButtonType.YES);
    }

    public AppointmentFormController getAppointmentForm() throws IOException {
        FXMLLoader fxmlLoader = getFXMLLoader(FXMLView.APPOINTMENT_FORM_FXML_LOCATION);
        Pane root = fxmlLoader.load();
        Stage stage = new Stage(StageStyle.UTILITY);
        stage.setTitle(ResourceBundlerUtil.getResourceBundle().getString("appointment"));
        stage.setScene(new Scene(root));
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setResizable(false);
        stage.show();
        AppointmentFormController controller = fxmlLoader.getController();
        controller.setStage(stage);
        return controller;
    }
}
