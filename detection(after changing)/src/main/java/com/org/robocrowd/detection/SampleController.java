package com.org.robocrowd.detection;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.Socket;
import java.net.UnknownHostException;

import org.opencv.aruco.Aruco;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class SampleController implements TCPConnectionListener{
	MarkerDetectorService markerDetectorService = null;
	AnimationTimer imageAnimation = null;

	@FXML
	ImageView imageView;
	
	TCPConnection connection;
	
	@FXML
	public void initialize() {
		try {  
            connection = new TCPConnection(this, "192.168.31.58", 4444);
        } catch (IOException e) {
            e.printStackTrace();
        }
		markerDetectorService = new MarkerDetectorService(Aruco.getPredefinedDictionary(Aruco.DICT_4X4_50),
				connection);
		 
		imageAnimation = new AnimationTimer() {
			
			@Override
			public void handle(long now) {
				imageView.setImage(markerDetectorService.getLastValue());
			}
		};
	}

	@FXML
	public void onStartButton() {
        imageAnimation.start();
    	if (!markerDetectorService.isRunning()) markerDetectorService.start();
		
	}

	@FXML
	public void onStopButton() {
		imageAnimation.stop();
		markerDetectorService.cancel();
		connection.sendMessage("Stop");
		connection.setDissconnect();
		markerDetectorService.reset();
		imageView.setImage(null);
		System.exit(1);
	}

	@FXML
	public void onPauseButton() {
		imageAnimation.stop();
	}
	
	 @Override
	    public void onConnectionReady(TCPConnection tcpConnection) {

	    }

	    @Override
	    public void onReceiveString(TCPConnection tcpConnection, String value) {
	       // System.out.println(value);
	       // tcpConnection.coms = "Stop";
	    }

	    @Override
	    public void onDisconnect(TCPConnection tcpConnection) {

	    }

	    @Override
	    public void onExeption(TCPConnection tcpConnection, Exception e) {

	    }
}