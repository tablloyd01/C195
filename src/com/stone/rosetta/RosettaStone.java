/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta;

import com.stone.rosetta.service.UserAuthenticationService;
import com.stone.rosetta.view.ViewFactory;
import java.util.logging.Level;
import java.util.logging.Logger;
import javafx.application.Application;
import javafx.stage.Stage;

/**
 *
 * @author jeeva
 */
public class RosettaStone extends Application {
    
    private static UserAuthenticationService authenticationService;
    private static ViewFactory viewFactory;
    
    static {
        try {
            authenticationService = UserAuthenticationService.getInstance();
            viewFactory = ViewFactory.getInstance();
        } catch (ClassNotFoundException ex) {
            Logger.getLogger(RosettaStone.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void start(Stage stage) throws Exception {
        viewFactory.setStage(stage);
        viewFactory.login();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

    @Override
    public void stop() throws Exception {
        super.stop();
        System.out.println("on stop");
    }
    
    
    
}
