/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.util;

import com.stone.rosetta.repository.model.User;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.time.LocalDateTime;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jeeva
 */
public class ActivityLogger {
    
    private static File file = new File("log-ins.txt");

    public static void logSignInActivity(User user) {
        try {
            if(!file.exists())
                file.createNewFile();
            FileWriter fileWriter = new FileWriter(file, true);
            try {
                fileWriter.append(LocalDateTime.now().toString().concat("- User \"").concat(user.getUsername()).concat("\" Signed In \n"));
//                fileWriter.append(String.valueOf(Character.LINE_SEPARATOR));
            } catch (Exception e) {
                Logger.getLogger(ActivityLogger.class.getName()).log(Level.SEVERE, null, e);            
            } finally {
                if(fileWriter != null)
                    fileWriter.close();
            }
        } catch (IOException ex) {
            Logger.getLogger(ActivityLogger.class.getName()).log(Level.SEVERE, null, ex);
        }
        
    }
    
}
