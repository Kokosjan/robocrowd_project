package com.org.robocrowd.detection;

import java.io.IOException;

public class Movement_control extends Thread {
   // private boolean f_working = true; 	
   // private boolean t_done = true;
	
    public Movement_control() {}
    
    void run_forward(double distance) { // Р·Р°РїСѓСЃС‚РёС‚СЊ РµРіРѕ РІ С†РёРєР»Рµ РїРѕРєР° РЅРµ РїРѕР»СѓС‡РёС‚ РІРѕР·РІСЂР°С‚ С‚СЂСѓ
    	MarkerDetectorService.rasst = distance ;
    	MarkerDetectorService.f_working = true;
    	System.out.println("Threas start");
    	while (MarkerDetectorService.get_forw()) {
    		//System.out.println("Thread forwarding");
		}
		//return;
	//	while (f_working) {  };
	};
	void turn_right(double angle) {
		MarkerDetectorService.angle = angle;
    	MarkerDetectorService.t_working = true;
    	System.out.println("Threas start");
    	while (MarkerDetectorService.get_turn()) {
    		//System.out.println("Thread turning");
		}
    	
	};
	
	void round_turn(double angle) {
		MarkerDetectorService.angle = angle;
    	MarkerDetectorService.r_working = true;
    	System.out.println("Rounding start");
    	try {
			Thread.sleep(7000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
    	System.out.println("Rounding ended");
    	MarkerDetectorService.r_working_out = true;
	};
	@Override
	public void run() {
		try {
			Thread.sleep(5000);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		
	//	run_forward(80);
	//	turn_right(0.523599);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			
			e.printStackTrace();
		}
	//	run_forward(80);
		//turn_right(0.523599);
		
		 //РїСЂРѕСЃС‚Рѕ РїРѕРјРµРЅСЏС‚СЊ РїРµСЂРµРјРµРЅРЅСѓСЋ РґР»СЏ РґРІРёР¶РµРЅРёСЏ РІРѕ РІС‚РѕСЂРѕРј СЃР»СѓС‡Р°Рµ
		//System.out.println("Thread forward stop");
	//	run_forward(40);
	//	System.exit(1);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	//	turn_right(0.785398);
		round_turn(1);
		
		//turn_right(1.5708);
		
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
	//	turn_right(1.571);
		run_forward(200);
		try {
			Thread.sleep(10);
		} catch (InterruptedException e) {
			e.printStackTrace();
		}
		//run_forward(200);
	//	turn_right(0.785398);
		System.out.println("Thread turn stop");
		System.exit(1);
		return;	
	};
	
}
