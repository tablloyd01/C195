/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package com.stone.rosetta.util;

import java.util.Locale;
import java.util.ResourceBundle;

/**
 *
 * @author jeeva
 */
public class ResourceBundlerUtil {
    
    private static String languageCode;
    private static String countryCode;
    
    public static void setLocale(String languageCode, String countryCode){
        ResourceBundlerUtil.languageCode = languageCode;
        ResourceBundlerUtil.countryCode = countryCode;
    }
    
    public static ResourceBundle getResourceBundle(){
        Locale locale = Locale.getDefault();
        System.out.printf("language: %s, countrycode: %s\n", locale.getLanguage(),locale.getCountry());
        return ResourceBundle.getBundle("labels", locale);
    }
    
}
