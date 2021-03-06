package application;
	
import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.core.Core;
import org.opencv.core.Mat;
import org.opencv.imgcodecs.Imgcodecs;

public class Main {

	static {
		System.loadLibrary(Core.NATIVE_LIBRARY_NAME);
	}

	public static void main(String[] args) {
		createMarker();
	}

	public static void createMarker() {
		Dictionary dictionary = Aruco.getPredefinedDictionary(Aruco.DICT_4X4_50);

		final int markerID = 2;
		final int sidePixels = 400;
		Mat markerImage = new Mat();
		Aruco.drawMarker(dictionary, markerID, sidePixels, markerImage);

		Imgcodecs.imwrite("C:\\OpenCV_Project\\mark2.png", markerImage);
	}
}