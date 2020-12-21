/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view.login;

import com.stone.rosetta.repository.model.User;
import com.stone.rosetta.service.UserAuthenticationService;
import com.stone.rosetta.throwable.IncorrectPasswordException;
import com.stone.rosetta.throwable.UserNotFoundException;
import com.stone.rosetta.view.AbstractController;
import com.stone.rosetta.view.ViewFactory;
import java.io.IOException;
import java.net.URL;
import java.sql.SQLException;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Platform;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;

/**
 * FXML Controller class
 *
 * @author jeeva
 */
public class LoginController extends AbstractController implements Initializable {
    
        
    @FXML
    private Label usernameHintLabel;
    @FXML
    private Label passwordHintLabel;
    
    @FXML
    private TextField usernameTextField;
    @FXML
    private TextField passwordTextField;
    private ResourceBundle resourceBundle;

    /**
     * Initializes the controller class.
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        System.out.printf("url: %s\n", url);
        System.out.printf("rb: %s\n", rb);
        this.resourceBundle = rb;
    }
    
    @FXML
    public void login(Event event){
        this.usernameHintLabel.setText(null);
        this.passwordHintLabel.setText(null);
        this.usernameTextField.setText("admin");
        this.passwordTextField.setText("admin");
        try {
            User user = UserAuthenticationService.getInstance()
                    .findByUserName(this.usernameTextField.getText().trim(), this.passwordTextField.getText().trim());
            if(user != null){
                getStage().close();
                ViewFactory.getInstance().main();
            }
        } catch (ClassNotFoundException | SQLException | UserNotFoundException | IncorrectPasswordException | IOException ex) {
            Logger.getLogger(LoginController.class.getName()).log(Level.SEVERE, null, ex);
            if(ex instanceof UserNotFoundException){
                this.usernameHintLabel.setText(resourceBundle.getString("ui.error.username.not.found"));
            }
            if(ex instanceof IncorrectPasswordException){
                this.passwordHintLabel.setText(resourceBundle.getString("ui.error.password.incorrect"));
                this.passwordTextField.clear();
            }
        }
    }
    
    @FXML
    public void exit(Event event){
        getStage().close();
    }    
    
    
}
