/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.view;

import javafx.stage.Stage;

/**
 *
 * @author jeeva
 */
public abstract class AbstractController {
 
    private Stage stage;
    
    public void setStage(Stage stage){
        this.stage = stage;
    }
    
    public Stage getStage(){
        System.out.printf("getStage %s\n", this.stage);
        return stage;
    }
    
}
