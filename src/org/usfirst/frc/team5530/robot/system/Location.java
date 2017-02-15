package org.usfirst.frc.team5530.robot.system;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

public class Location implements RobotSystem{
	public Location(){}
	
	double previousLeft = 0,
			previousRight = 0;
	double leftChange = 0,
			rightChange = 0,
			distanceChange = 0;
	Vector2 location = new Vector2();
	Vector2 change = new Vector2();
	
	public void driveVector(Robot rob, Vector2 vector){
		DriveTrain driveTrain = rob.getSystem(DriveTrain.class);
		driveTrain.rotateToAngle(vector.direction);
		driveTrain.driveStraightDistance(vector.magnitude);
	}
	
	public void driveStraightToTargetLocation(Robot rob, Vector2 target){
		Vector2 vector = target.subtract(location);
		driveVector(rob, vector);
	}
	
	@Override
	public void update(){
		leftChange = DriveTrain.leftDistance - previousLeft;
		rightChange = DriveTrain.rightDistance - previousRight;
		distanceChange = (leftChange + rightChange)/2;
		change = new Vector2(distanceChange, DriveTrain.currentAngle, true);
		location = location.add(change);
		
		previousLeft = DriveTrain.leftDistance;
		previousRight = DriveTrain.rightDistance;
	}
}