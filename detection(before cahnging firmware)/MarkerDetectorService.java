package com.org.robocrowd.detection;


import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;


public class MarkerDetectorService extends ScheduledService<Image> {
	final Dictionary dictionary;
	private Mat cameraMatrix;
	private Mat distortionCoefficients;
	private double[] st_cen = new double[2];
	private double[] st_end = new double[2];
	private boolean st_con = true;
	
	Singleton x = Singleton.getInstance();
	
	static final int provisionalCameraNumber = 1;

	VideoCapture vc = null;
	int cameraNumber = 1;

	public MarkerDetectorService(Dictionary dictionary, int cameraNumber) {
		this.dictionary = dictionary;
		this.cameraNumber = cameraNumber;
		initialize();

		this.setOnCancelled(new EventHandler<WorkerStateEvent>() {
			public void handle(WorkerStateEvent event) {
				vc.release();
			}
		});
	}

	public MarkerDetectorService(Dictionary dictionary) {
		this(dictionary, provisionalCameraNumber);
	}

	public void initialize() {
		vc = new VideoCapture();
		vc.open(cameraNumber);
	}

	@Override
	public void reset() {
		super.reset();

		initialize();
	}

	@Override
	public boolean cancel() {
		return super.cancel();
	}

	@Override
	protected Task<Image> createTask() {
		return new Task<Image>() {
			@Override
			protected Image call() throws Exception {
				if (!vc.isOpened()) {
					System.err.println("VC is not opened.");
					this.cancel();
					return null;
				}

				Mat inputImage = new Mat();

				if (!vc.read(inputImage) || inputImage == null) {
					System.err.println("Cannot load camera image.");
					this.cancel();
					return null;
				}

				List<Mat> corners = new ArrayList<Mat>();
				Mat markerIds = new Mat();
				Mat undistored = new Mat();
			    ArrayList<double[]> mark_centers = new ArrayList<double[]>();
				
			    
			    //possible calibration
			    
			    /*double data[] = new double[]{ 482.55129703, 0, 308.9229375, 0, 483.11494584, 232.45787334, 0, 0, 1};
				double data1[] = new double[]{6.23054572e-02, -2.45866533e-01,  1.79145670e-04,  4.08462722e-03,
						   2.58571738e-01};
				
				cameraMatrix = new Mat(3, 3, CvType.CV_32FC1);
			    distortionCoefficients = new Mat(1,5, CvType.CV_32FC1);	
				cameraMatrix.put( 0, 0, data);
				distortionCoefficients.put(0, 0, data1);*/
				
				//Calib3d.undistort(inputImage, undistored, cameraMatrix, distortionCoefficients);
				undistored = inputImage;
				
				Aruco.detectMarkers(undistored, dictionary, corners, markerIds);

				if (corners.isEmpty()) return convertMatToImage(undistored);

				Aruco.drawDetectedMarkers(undistored, corners, markerIds);

				//Possible translation and rotation use
			    
				//Mat rotationMatrix = new Mat(), translationVectors = new Mat(); 
			
				//Aruco.estimatePoseSingleMarkers(corners, 0.05f, cameraMatrix, distortionCoefficients,
					//	rotationMatrix, translationVectors);
				//System.out.println("Rotation: "+ rotationMatrix.dump());
				//System.out.println("Translation: "+ translationVectors.dump());
				
				//for (int i = 0; i < markerIds.rows(); i++) {
					//System.out.println(markerIds.get(i, 0)[0]);
				//}
				double[] rnd_point = new double[2];
				for (Mat mat : corners) {
					List<Point> cornerPoints = new ArrayList<Point>();
					for (int row = 0; row < mat.height(); row++) {
						for (int col = 0; col < mat.width(); col++) {
							if (row == 0 && col==0) rnd_point = mat.get(row,col);
							final Point point = new Point(mat.get(row, col));
							cornerPoints.add(point);
							Imgproc.circle(inputImage, point, 5, new Scalar(100, 100, 0), -1);
						}
					}
					Imgproc.circle(inputImage, new Point(getCenter(cornerPoints)), 5, new Scalar(0, 0, 255), -1);
					mark_centers.add(getCenter(cornerPoints));
				}

				//Output distance 
				//System.out.println("Actual distance between 1st centre and others: ");
				//for (int i=1; i<mark_centers.size();i++) {
					System.out.println("Start");
					if (st_con) {
						st_cen = mark_centers.get(0);
						st_end = angleturn_coordinates(mark_centers.get(0), rnd_point, 1.5708);
						System.out.println("Start coordinates of center are " + st_cen[0] + " " + st_cen[1]);
						System.out.println("Start coordinates of point are " + rnd_point[0] + " " + rnd_point[1]);
						System.out.println("Calculated end of point are " + st_end[0] + " " + st_end[1]);
						st_con = false;
					}
					if (rnd_point[0] > st_end[0]-0.5 && rnd_point[0] < st_end[0]+0.5 &&
							rnd_point[1] > st_end[1]-0.5 && rnd_point[1] < st_end[1]+0.5) {
						System.out.println("Stop");
						System.out.println("Current coordinates of point " + rnd_point[0] + 
								" " + rnd_point[1]);
					}
					else {
						System.out.println("Turn" );
						System.out.println("Current coordinates of point " + rnd_point[0] + 
								" " + rnd_point[1]);
						System.out.println("Needed coordinates of point " + (st_end[0]-0.5) + 
								 " to " + (st_end[0]+0.5) +" and " + (st_end[1]-0.5) + 
								 " to " + (st_end[1]+0.5));
					}
					//System.out.println(distance_count(mark_centers.get(0),static_point));
					System.out.println("End \n");
					//}
				
	
				return convertMatToImage(undistored);
			}
		};
	}
	public double distance_count(double[] a, double[] b) {
		double alpha1 = x.coef[0]; //1.227747;
		double alpha2 = x.coef[1];  //1.228501229;
		return Math.sqrt(Math.pow((a[0]-b[0])*alpha1,2)+Math.pow((a[1]-b[1])*alpha2,2));
		
	}
	
	public double[] getCenter(List<Point> points) {
		final MatOfPoint points_ = new MatOfPoint();
		points_.fromList(points);
		return getCenter(points_);
	}

	public double[] getCenter(MatOfPoint points) {
		Moments moments = Imgproc.moments(points);
		double[] center = { moments.get_m10() / moments.get_m00(), moments.get_m01() / moments.get_m00() };
		return center;
	}

	private double[] angleturn_coordinates (double[] center, double[] f_point, double angel) {
		double[] result = {center[0]+(f_point[0]-center[0])*Math.cos(angel)-(f_point[1] - center[1])*Math.sin(angel),
				           center[1]+(f_point[0]-center[0])*Math.sin(angel)+(f_point[1]-center[1])*Math.cos(angel)};
		
		return result;
	}
	
	private Image convertMatToImage(Mat inputImage) {
		MatOfByte byte_mat = new MatOfByte();
		Imgcodecs.imencode(".bmp", inputImage, byte_mat);

		return new Image(new ByteArrayInputStream(byte_mat.toArray()));
	}
}