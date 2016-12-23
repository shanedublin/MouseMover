package com.rusd.clicker;

import java.awt.MouseInfo;
import java.awt.Point;
import java.awt.Robot;

public class MouseMove implements Runnable {

	public static long sleep = 120000;
	public boolean runThread = false;
	
	
	public void run(){
		
		Robot robot; 
		runThread = true;
		
		try{
			robot = new Robot();
			while(runThread){
				try{
					Point p = MouseInfo.getPointerInfo().getLocation();
					robot.mouseMove(p.x +1, p.y);
					Thread.sleep(sleep);
					
					p = MouseInfo.getPointerInfo().getLocation();
					robot.mouseMove(p.x -1, p.y);
					Thread.sleep(sleep);
				}catch(Exception e){
					Thread.sleep(sleep);
				}
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	
		
	}
}
