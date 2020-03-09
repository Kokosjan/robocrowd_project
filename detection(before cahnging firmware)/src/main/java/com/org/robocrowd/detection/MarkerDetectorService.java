package com.org.robocrowd.detection;


//import java.io.ByteArrayInputStream;
import java.util.ArrayList;
import java.util.List;
import java.io.*;
import java.net.*;

import org.opencv.aruco.Aruco;
import org.opencv.aruco.Dictionary;
import org.opencv.calib3d.Calib3d;
import org.opencv.core.CvType;
import org.opencv.core.Mat;
import org.opencv.core.MatOfByte;
import org.opencv.core.MatOfPoint;
import org.opencv.core.Point;
import org.opencv.core.Scalar;
import org.opencv.core.Size;
import org.opencv.highgui.HighGui;
import org.opencv.imgcodecs.Imgcodecs;
import org.opencv.imgproc.Imgproc;
import org.opencv.imgproc.Moments;
import org.opencv.videoio.VideoCapture;
import org.opencv.videoio.Videoio;

import javafx.concurrent.ScheduledService;
import javafx.concurrent.Task;
import javafx.concurrent.WorkerStateEvent;
import javafx.event.EventHandler;
import javafx.scene.image.Image;


public class MarkerDetectorService extends ScheduledService<Image> {
	final Dictionary dictionary;
	private Thread rxThread;
	private Mat cameraMatrix;
	private Mat distortionCoefficients;
	private double[] st_cen = new double[2];
	private double[] st_end = new double[2];
	private boolean st_con = true;
	private boolean move_bool = true;
	
	public static boolean r_working_out = false;
	public static boolean r_working = false; 
	private Point start_point = new Point();
	private Point end_point = new Point();
	private boolean first_e = true;
	private double start_angle;
	
	public static boolean f_working = false;
	public static boolean t_working = false;
	public static double angle;
	public static double rasst;
	static Movement_control thr;
	
	private TCPConnection out;
	
	Singleton x = Singleton.getInstance();
	
	static final int provisionalCameraNumber = 0;

	VideoCapture vc = null;
	int cameraNumber = 0;

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

	public MarkerDetectorService(Dictionary dictionary, TCPConnection conn_stream) {
		this(dictionary, provisionalCameraNumber);
		out = conn_stream;
	}

	public static synchronized boolean  get_forw() { return f_working; }
	public static synchronized boolean  get_turn() { return t_working; }
	
	public void initialize() {
		vc = new VideoCapture();
		vc.open(cameraNumber);   //"http://192.168.31.166:8080/videofeed");//cameraNumber);
		vc.set(Videoio.CAP_PROP_FRAME_WIDTH, 800);
		vc.set(Videoio.CAP_PROP_FRAME_HEIGHT, 600);
		thr = new Movement_control();
		thr.start();
		/*rxThread=new Thread(new Runnable() {
            @Override
            public void run() {
         if (f_working)
         System.out.println(f_working);
         try {
        	    Thread.sleep(1000);
        	   } catch (InterruptedException e) {
        	    System.out.println("Thread has been interrupted");
        	   }
            }
            
        });
        rxThread.start();*/
		
		//vc.set(Videoio.CAP_PROP_FRAME_COUNT, 10);
		//System.out.println( (int) (vc.get(5)));       //(Videoio.CAP_PROP_FRAME_COUNT)) );
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
	protected Task<Image> createTask(){
		return new Task<Image>() {
			@Override
			protected Image call() throws Exception {
				
				if (!vc.isOpened()) {
					System.err.println("VC is not opened.");
					this.cancel();
					return null;
				}
				
				Mat inputImage = new Mat();
		        Mat undistored = inputImage;
				
			//	time_elapsed = (System.currentTimeMillis()) - prev;
				//System.out.println(time_elapsed);
			    //if (time_elapsed > 1./frame_rate) {
			      	      
			   //     prev = System.currentTimeMillis();
		        
				
				

				if (!vc.read(inputImage) || inputImage == null) {
					System.err.println("Cannot load camera image.");
					this.cancel();
					return null;
				}

				List<Mat> corners = new ArrayList<Mat>();
				Mat markerIds = new Mat();
				//Mat undistored = new Mat();
			    ArrayList<double[]> mark_centers = new ArrayList<double[]>();
				
			    //String fromUser = "";
		       
			  //  String hostName = "192.168.31.58";
		      //  int portNumber = 4444;
		       /* 
		        try (   Socket kkSocket = new Socket(hostName, portNumber);
		                PrintWriter out = new PrintWriter(kkSocket.getOutputStream(), true);
		                BufferedReader in = new BufferedReader(
		                    new InputStreamReader(kkSocket.getInputStream()));
		            ) {*/
		               
		              //  String fromServer;
		                
		                	
		                      
		                    
		                
		          
			    
			    //possible calibration
			    
			    /*double data[] = new double[]{ 482.55129703, 0, 308.9229375, 0, 483.11494584, 232.45787334, 0, 0, 1};
				double data1[] = new double[]{6.23054572e-02, -2.45866533e-01,  1.79145670e-04,  4.08462722e-03,
						   2.58571738e-01};
				
				cameraMatrix = new Mat(3, 3, CvType.CV_32FC1);
			    distortionCoefficients = new Mat(1,5, CvType.CV_32FC1);	
				cameraMatrix.put( 0, 0, data);
				distortionCoefficients.put(0, 0, data1);*/
				
				//Calib3d.undistort(inputImage, undistored, cameraMatrix, distortionCoefficients);
				
		        //undistored = inputImage;
				
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
				double[] rnd_point2 = new double[2];
				double[] rnd_point3 = new double[2];
				for (Mat mat : corners) {
					List<Point> cornerPoints = new ArrayList<Point>();
					for (int row = 0; row < mat.height(); row++) {
						for (int col = 0; col < mat.width(); col++) {
							if (row == 0 && col==0) {rnd_point = mat.get(row,col);// continue;
							}
							if (row == 0 && col==1) { rnd_point2 = mat.get(row,col);// continue;
							}
							if (row == 0 && col==2) { rnd_point3 = mat.get(row,col);// continue;
							}
							final Point point = new Point(mat.get(row, col));
							cornerPoints.add(point);
							if ((row == 0 && col==2) || (row == 0 && col==2)) {}
							else 
								Imgproc.circle(inputImage, point, 5, new Scalar(100, 100, 0), -1);
						}
					}
					Imgproc.circle(inputImage, new Point(getCenter(cornerPoints)), 5, new Scalar(0, 0, 255), -1);
					mark_centers.add(getCenter(cornerPoints));
				}
			//	Point pt1 = new Point(100, 100);
				
				//Point pt2 = new Point(105, 200);
			//	Point pt_c = new Point(205,200);
				int radius = 100;
				Imgproc.line(inputImage, start_point, end_point , new Scalar(255,0,0), 2);
				Imgproc.ellipse(inputImage, new Point(end_point.x+100, end_point.y), new Size(radius,radius), 0, 180 , 
						360 , new Scalar(255,0,0), 2, 8, 0);
				Imgproc.line(inputImage, new Point(end_point.x+200, end_point.y), new Point(end_point.x+200, end_point.y+80) , new Scalar(255,0,0), 2);
				Imgproc.line(inputImage, new Point(end_point.x+200, end_point.y+80), start_point , new Scalar(255,0,0), 2);
				//Output distance 
				//System.out.println("Actual distance between 1st centre and others: ");
				//for (int i=1; i<mark_centers.size();i++) {
				
				//out.sendMessage("forward");
				if (f_working) move_forward2(mark_centers, rnd_point2, rnd_point3, out, rasst);
				else if (t_working) turn_right(mark_centers, rnd_point3, out, angle);
				else if (r_working) {if (r_working_out) {out.sendMessage("Stop"); r_working = false;} else out.sendMessage("round");}
	
			///	System.out.println("Actual distance between 1st centre and others: ");
			//	for (int i=1; i<mark_centers.size();i++) {
			//		System.out.println(distance_count(mark_centers.get(0),mark_centers.get(i)));	
			//	}
				
				//out.sendMessage("right");
				//return convertMatToImage(undistored);
		        
			/*	} catch (UnknownHostException e) {
	                System.err.println("Don't know about host " + hostName);
	                System.exit(1);
	            } catch (IOException e) {
	                System.err.println("Couldn't get I/O for the connection to " +
	                    hostName);
	                System.exit(1);
	            }*/
		        
			    //}
		        
			    return convertMatToImage(undistored);
		        
			}
		};
	}
	public double distance_count(double[] a, double[] b) {
		double alpha1 = 0.6849;//x.coef[0]; //1.227747;
		double alpha2 = 0.7216;//x.coef[1];  //1.228501229;
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
	
	private void turn_right (ArrayList<double[]> mark_centers,double[] rnd_point, TCPConnection out, 
			double angel) {
		System.out.println("Start turn");
		if (st_con) {
			st_cen = mark_centers.get(0);
			st_end = angleturn_coordinates(mark_centers.get(0), rnd_point, angel );
			System.out.println("Start coordinates of center are " + st_cen[0] + " " + st_cen[1]);
			System.out.println("Start coordinates of point are " + rnd_point[0] + " " + rnd_point[1]);
			System.out.println("Calculated end of point are " + st_end[0] + " " + st_end[1]);
			st_con = false;
		}
		if ( (rnd_point[0] > st_end[0]-4 && rnd_point[0] < st_end[0]+4 &&
				rnd_point[1] > st_end[1]-4 && rnd_point[1] < st_end[1]+4) ||//) {
				(rnd_point[0]+rnd_point[1] > st_end[0]+st_end[1]-3 && 
				rnd_point[0]+rnd_point[1] < st_end[0]+st_end[1]+3 )	) {
			out.sendMessage("Stop");
			System.out.println("Stop");
			System.out.println("Current coordinates of point " + rnd_point[0] + 
					" " + rnd_point[1]);
			//out.println("Stop");
			//func_flag = false;
			//return;
			t_working = false;
			st_con = true;
			//System.exit(1);
		}
		else {
			  //System.out.println("Client: " + fromUser);
            out.sendMessage("right");
			System.out.println("Turn" );
			System.out.println("Current coordinates of point " + rnd_point[0] + 
					" " + rnd_point[1]);
			System.out.println("Needed coordinates of point " + (st_end[0]-5) + 
					 " to " + (st_end[0]+5) +" and " + (st_end[1]-5) + 
					 " to " + (st_end[1]+5));
		}
		//System.out.println(distance_count(mark_centers.get(0),static_point));
		System.out.println("End \n");
		
	}
	
	public void move_forward(ArrayList<double[]> mark_centers,double[] rnd_point, double[] rnd_point2, 
			TCPConnection out, double distance) {
		//double distance = 150;
		System.out.println("Start forward");
		if (st_con) {
			st_cen = mark_centers.get(0); //1
			
			System.out.println("Start is " + st_cen[0] + " " + st_cen[1]);
			System.out.println("Points are " + rnd_point[0] + " " + rnd_point[1] + " and "
					+ rnd_point2[0] + " " + rnd_point2[1]);
			
			double k = (rnd_point[1] - rnd_point2[1])/(rnd_point[0]-rnd_point2[0]);
			double b = -k*rnd_point[0]+rnd_point[1]; //2
			System.out.println("Р›РёРЅРёСЏ РјРµР¶РґСѓ С‚РѕС‡РєР°РјРё СЃ k = " + k + " Рё b = " + b);
			
			double m = -1./k;
			double l = st_cen[0]/k + st_cen[1];
			System.out.println("РџРµСЂРїРµРЅРґРёРєСѓР»СЏСЂ, РёРґСѓС‰РёР№ РёР· С†РµРЅС‚СЂР° СЃ РєРѕСЌС„ m = " + m + " Рё l = " +l );
			
			
			double fi = Math.atan(m);
			System.out.println("РЈРіРѕР» СЂР°РІРµРЅ " + fi);
			
			double D =  -st_cen[1] + st_cen[0]*k+b;
			System.out.println("D is " + D);
			if (D>=0) {    //D>=0) {
			st_end[0] = st_cen[0] - (distance/0.6849)*Math.cos(fi); // /0.6849
			st_end[1] =  st_cen[1] - (distance/0.7216)*Math.sin(fi);// /0.7216
			System.out.println("End is " + st_end[0] + " " + st_end[1]);
			}
			else {
				st_end[0] = st_cen[0] + (distance/0.6849)*Math.cos(fi);
				st_end[1] =  st_cen[1] + (distance/0.7216)*Math.sin(fi);
				System.out.println("End is " + st_end[0] + " " + st_end[1]);
			}
			
			/*System.out.println("Start is " + st_cen[0] + " " + st_cen[1]);
			System.out.println("Points are " + rnd_point[0] + " " + rnd_point[1] + " and "
					+ rnd_point2[0] + " " + rnd_point2[1]);
			System.out.println("End is " + st_end[0] + " " + st_end[1]);*/
			st_con = false;
		}
		if (mark_centers.get(0)[0] > st_end[0]-4 && mark_centers.get(0)[0] < st_end[0]+4 &&
				mark_centers.get(0)[1] > st_end[1]-4 && mark_centers.get(0)[1] < st_end[1]+4) {
			out.sendMessage("Stop");
			System.out.println("Stop");
			System.out.println("Current coordinates of point " + mark_centers.get(0)[0] + 
					" " + mark_centers.get(0)[1]);
			//out.println("Stop");
			//func_flag = false;
			move_bool = true;
			st_con = true;
			f_working = false;
			//System.exit(1);
			//return;
		}
		else {
			  //System.out.println("Client: " + fromUser);
           if (move_bool) {out.sendMessage("forward"); move_bool = false;}
			System.out.println("Moving" );
			System.out.println("Current coordinates of point " + mark_centers.get(0)[0] + 
					" " + mark_centers.get(0)[1]);
			System.out.println("Needed coordinates of point " + (st_end[0]-4) + 
					 " to " + (st_end[0]+4) +" and " + (st_end[1]-4) + 
					 " to " + (st_end[1]+4));
		}
		
	}

	public void move_forward2(ArrayList<double[]> mark_centers,double[] rnd_point, double[] rnd_point2, 
			TCPConnection out, double distance) {
		//double distance = 150;
		System.out.println("");
		//System.out.println("Start forward");
		if (st_con) {
			
			st_cen[0] = (rnd_point[1] - rnd_point2[1])/(rnd_point[0]-rnd_point2[0]);
			st_cen[1] = -st_cen[0]*rnd_point[0]+rnd_point[1]; //2
			
			System.out.println("Line is k = " + st_cen[0] + " and b = " + st_cen[1]);
			
			double bvx = rnd_point[0] - rnd_point2[0];
			double bvy = rnd_point[1] - rnd_point2[1];
		
			st_end[0] = rnd_point2[0] - (distance)/(Math.sqrt(bvx*bvx+bvy*bvy))*bvx;//(distance/0.6849)/(Math.sqrt(bvx*bvx+bvy*bvy))*bvx;
			st_end[1] =  rnd_point2[1] - (distance)/(Math.sqrt(bvx*bvx+bvy*bvy))*bvy;//(distance/0.7216)/(Math.sqrt(bvx*bvx+bvy*bvy))*bvy;
			
			if (first_e) {
			start_point = new Point(rnd_point2[0],rnd_point2[1]);
			end_point = new Point(st_end[0],st_end[1]);
			start_angle = Math.toDegrees(Math.atan(st_cen[0]));
			first_e = false;
			}
			/*System.out.println("Start is " + st_cen[0] + " " + st_cen[1]);*/
			System.out.println("Points are " + rnd_point[0] + " " + rnd_point[1] + " and "
					+ rnd_point2[0] + " " + rnd_point2[1]);
			System.out.println("End is " + st_end[0] + " " + st_end[1]);
			if (rnd_point2[0] > st_end[0]) move_bool = true; else move_bool = false;
			st_con = false;
		}
		if (rnd_point2[0] > st_end[0]-4.5 && rnd_point2[0] < st_end[0]+4.5 &&
				rnd_point2[1] > st_end[1]-4.5 && rnd_point2[1] < st_end[1]+4.5) {
			out.sendMessage("Stop");
			System.out.println("Stop");
			System.out.println("Current coordinates of point " + rnd_point2[0] + 
					" " + rnd_point2[1]);
			//out.println("Stop");
			//func_flag = false;
		    //	move_bool = true;
			st_con = true;
			f_working = false;
			//System.exit(1);
			//return;
		}
		else {
			if (rnd_point2[1] > (st_cen[0]*rnd_point2[0] + st_cen[1] + 3 + Math.abs(st_cen[1])*0.015)) { 
				if (move_bool) out.sendMessage("rforward"); else out.sendMessage("lforward");
				System.out.println("Correcting to right");
				//System.out.println(rnd_point2[1] + " > " + (st_cen[0]*rnd_point2[0] + st_cen[1] + 3 + st_cen[1]*0.015));
			}
			else if (rnd_point2[1] < (st_cen[0]*rnd_point2[0] + st_cen[1] - 3 - Math.abs(st_cen[1])*0.015)) { 
				if (move_bool) out.sendMessage("lforward"); else out.sendMessage("rforward");
				System.out.println("Correcting to left");
				//System.out.println(rnd_point2[1] + " < " + (st_cen[0]*rnd_point2[0] + st_cen[1] + 3 - st_cen[1]*0.015));
			}
			
			else  {
				out.sendMessage("forward"); 
				System.out.println("Forwarding");
				//move_bool = false;
				}
			//System.out.println("Moving" );
			System.out.println("Current coordinates of point " + rnd_point2[0] + 
					" " + rnd_point2[1]);
			System.out.println("Needed coordinates of point " + (st_end[0]-4) + 
					 " to " + (st_end[0]+4) +" and " + (st_end[1]-4) + 
					 " to " + (st_end[1]+4));
		}
		
	}
	private Image convertMatToImage(Mat inputImage) {
		MatOfByte byte_mat = new MatOfByte();
		Imgcodecs.imencode(".bmp", inputImage, byte_mat);

		return new Image(new ByteArrayInputStream(byte_mat.toArray()));
	}
}