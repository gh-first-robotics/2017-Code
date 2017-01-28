package org.usfirst.frc.team5530.robot;

import edu.wpi.first.wpilibj.networktables.NetworkTable;

public class VisionProcessing {
	public static double bestWHratio = 1; //figure out what this is
	//largest area is best???
	public static int bestTargetIndex =-1;
	public static int testIndex = 0;
	public static int secondBestTargetIndex = 0;
	static double screenCenter = 160; //50
	
	public int getbestTargetIndex(){
		return bestTargetIndex;
	}
	public static boolean targetsFound = false;
	static double k = 21*40; //is this the correct number?
	

	
	public static int bestTarget(double[] widths, double[] heights/*, double[] areas*/){
		if (widths.length > 0){		
			//bestTargetIndex=0;
			testIndex = 0;
			secondBestTargetIndex=0;
			System.out.println("widths.length" + widths.length);
			System.out.println("heights.length" + heights.length);
			
			for (int i=0; i<Math.min(widths.length, heights.length); i++){
				System.out.println("i " + i);
				System.out.println("widths.length in for loop"+widths.length);
				System.out.println("bestTargetIndex " + bestTargetIndex);
				System.out.println("widths[i] "+widths[i]);
				System.out.println("widths[testIndex] "+widths[testIndex]);
				System.out.println("heights[i] "+heights[i]);
				System.out.println("heights[testIndex] "+heights[testIndex]);
				if (Math.abs(widths[i]/heights[i] - bestWHratio)<Math.abs(widths[testIndex]/heights[testIndex] - bestWHratio)){
					//secondBestTargetIndex=bestTargetIndex;
					//bestTargetIndex=i;
					secondBestTargetIndex=testIndex;
					testIndex=i;
				}
				//add something to include area
			}
			bestTargetIndex = testIndex;
			return bestTargetIndex;
		}
		else{
			return -1;
		}
	}
	public static void printTargetInformation(){		
		double[] areas = table.getNumberArray("area", new double[0]);
		double[] widths = table.getNumberArray("width", new double[0]);
		double[] heights = table.getNumberArray("height", new double[0]);
		double[] centerXs = table.getNumberArray("centerX", new double[0]);
		double[] centerYs = table.getNumberArray("centerY", new double[0]);
		double[] solidities = table.getNumberArray("solidity", new double[0]);
		System.out.println(table.isConnected()); //prints true
		System.out.println(areas.length); //prints number of targets found
		System.out.println(widths.length);
		System.out.println(heights.length);
		if (widths.length > 1){
			targetsFound=true;
		}
		else {
			targetsFound=false;
		}
		if (widths.length > 0){
			
			System.out.println("index of best target: "+bestTarget(widths, heights));
			System.out.println("width: "+widths[bestTargetIndex]);
			table.putNumber("bestTargetIndex", bestTargetIndex);
			distanceToTarget = distanceToTarget(widths[bestTargetIndex]);
			System.out.println("distance to target: "+distanceToTarget(widths[bestTargetIndex]));
			table.putNumber("indexOfBestTargetdistanceToTarget", distanceToTarget(widths[bestTargetIndex]));
			System.out.println("centerxs.length" + centerXs.length);
			System.out.println("best target index for centerx" + bestTargetIndex);
			if (centerXs.length > bestTargetIndex){
			center0= centerXs[bestTargetIndex] - screenCenter;
			System.out.println("center X of best target: "+centerXs[bestTargetIndex]);

			}
					}
		else{
			targetsFound=false;
		}
	}//printTargetInfo
	public static double distanceToTarget(double width){ //TODO: change to use height in calculations as well
		return k/width; //inches
	}
	public static double center0;
	public static double distanceToTarget;
	static NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport");
}
