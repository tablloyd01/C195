/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.throwable;

/**
 *
 * @author jeeva
 */
public class OutsideBusinessHoursException extends Throwable{

    public OutsideBusinessHoursException() {
        super();
    }
    
    public OutsideBusinessHoursException(String message) {
        super(message);
    }    
}
