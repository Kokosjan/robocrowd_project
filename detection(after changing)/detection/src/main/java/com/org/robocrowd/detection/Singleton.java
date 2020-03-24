package com.org.robocrowd.detection;

import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;

import org.json.simple.JSONObject; 
import org.json.simple.parser.*; 

class Singleton 
{ 
    // static variable single_instance of type Singleton 
    private static Singleton single_instance = null; 
  
    // variable of type String 
    public double coef []; 
  
    // private constructor restricted to this class itself 
    private Singleton() 
    { 	
    	coef = new double[2];
    	
    	// parsing file "JSONExample.json" 
    	Object obj;
		try {
			obj = new JSONParser().parse(new FileReader("src/main/opencv_config.json"));
		
    	
        JSONObject jo = (JSONObject) obj;
        
        Double alpha1 = (Double) jo.get("x_coef"); 
        Double alpha2 = (Double) jo.get("y_coef");

        coef[0] = alpha1.doubleValue();
        coef[1] = alpha2.doubleValue();
		} catch (FileNotFoundException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ParseException e) {
			e.printStackTrace();
		} 
    	
    } 
  
    // static method to create instance of Singleton class 
    public static Singleton getInstance() 
    { 
        if (single_instance == null) 
            single_instance = new Singleton(); 
  
        return single_instance; 
    } 
}