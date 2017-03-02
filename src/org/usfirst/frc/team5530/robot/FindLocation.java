package org.usfirst.frc.team5530.robot;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

public class FindLocation{
	public FindLocation(){}
	
	static double previousLeft = 0,
			previousRight = 0;
	static double leftChange = 0,
			rightChange = 0,
			distanceChange = 0;
	public static Vector2 location = new Vector2();
	public static Vector2 change = new Vector2();
	
	public static void update(){
		leftChange = DriveTrainSystem.leftEncPosition() - previousLeft;
		rightChange = DriveTrainSystem.rightEncPosition() - previousRight;
		distanceChange = (leftChange + rightChange)/2;
		change = new Vector2(distanceChange, DriveTrainSystem.gyro.getAngle(), true);
		location = location.add(change);
		
		previousLeft = DriveTrainSystem.leftEncPosition();
		previousRight = DriveTrainSystem.rightEncPosition();
	}
}