package com.org.robocrowd.detection;

import org.opencv.aruco.Aruco;

import javafx.animation.AnimationTimer;
import javafx.fxml.FXML;
import javafx.scene.image.ImageView;

public class SampleController {
	MarkerDetectorService markerDetectorService = null;
	AnimationTimer imageAnimation = null;

	@FXML
	ImageView imageView;

	@FXML
	public void initialize() {
		markerDetectorService = new MarkerDetectorService(Aruco.getPredefinedDictionary(Aruco.DICT_4X4_50));

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

		markerDetectorService.reset();
		imageView.setImage(null);
	}

	@FXML
	public void onPauseButton() {
		imageAnimation.stop();
	}
}