package com.org.robocrowd.detection;

import java.io.IOException;

//import org.opencv.core.Core;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;


public class App extends Application
{
		public static Stage primaryStage = null;
	
		
		static {
			System.load("C:\\Games\\mb_thistime\\install\\java\\opencv_java412.dll"); //change this directory
		}

		public static void main(String[] args) {
			launch(args);
		}

		@Override
		public void start(Stage primaryStage) throws Exception {
			App.primaryStage = primaryStage;
			try {
				Scene scene = new Scene((Parent) FXMLLoader.load(getClass().getResource("window.fxml")));
				primaryStage.setTitle("test");
				// // Set Window
				primaryStage.setResizable(false);
				// Set Scene
				primaryStage.setScene(scene);
				primaryStage.show();
			} catch (IOException e) {
				e.printStackTrace();
			}
		}

	}
