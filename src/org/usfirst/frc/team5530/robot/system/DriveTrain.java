package org.usfirst.frc.team5530.robot.system;

import org.usfirst.frc.team5530.robot.teleop.Vector2;
import org.usfirst.frc.team5530.robot.Robot;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.Ultrasonic;

public class DriveTrain implements RobotSystem {
	private CANTalon[] talons;
	public Ultrasonic ultrasonic = new Ultrasonic(0,1);
	
	public DriveTrain(CANTalon l1, CANTalon l2, CANTalon r1, CANTalon r2) {
		talons = new CANTalon[] { l1, l2, r1, r2 };
	}

	/**
	 * Updates the drive train based on the position of the joystick
	 * 
	 * @param stick
	 *            the joystick used to control the drive train
	 * @param reverse
	 *            true to reverse driving
	 */
	public void arcadeDrive(Vector2 stick, boolean reverse) {
		double left = -stick.y - stick.x;
		double right = -stick.y + stick.x;

		if (reverse) {
			tankDrive(right, left);
		} else {
			tankDrive(left, right);
		}
	}

	/**
	 * Drive in a straight line
	 * 
	 * @param speed
	 *            the speed to drive at [-1, 1]
	 */
	public void drive(double speed) {
		tankDrive(speed, speed);
	}

	/**
	 * Sets the speed of the drive train
	 * 
	 * @param lSpeed
	 *            the speed for the left wheel [-1, 1]
	 * @param rSpeed
	 *            the speed for the right wheels [-1, 1]
	 */
	public void tankDrive(double lSpeed, double rSpeed) {
		lSpeed = clamp(lSpeed, -1, 1);
		rSpeed = clamp(rSpeed, -1, 1);
		talons[0].set(-lSpeed);
		talons[1].set(-lSpeed);
		talons[2].set(rSpeed);
		talons[3].set(rSpeed);
	}
	
	boolean turnTowardsTarget = false;
	int targetDirection = 0;
	double speed = 1;
	static double error = 5;
	static double center = 160; // is this number correct?
	public void turnTowardsTarget(){
		turnTowardsTarget = true;
	}

	boolean driveToTarget = false;
	double drive_speed;
	double turn_speed;
	double max_speed = 1;
	double min_speed = 0.2;
	double max_turn_speed = 0.7;
//	double min_turn_speed = 0.2;
	double distance_speed_ratio = 1/10;
	double distance_turn_speed_ratio = 1/50;
	double errorDfromT = 17; //make this minimum distance that target can fully be seen from. This number was previously 10
	public void startDriveToTarget(){
		if (driveToTarget){
			driveToTarget = false;
			tankDrive(0,0);
		}
		else{
			driveToTarget = true;
		}
	}
	public void driveToTarget(){ //called repeatedly if driveToTarget==true
		drive_speed = Math.min(1, Math.max(min_speed, Robot.distanceToTarget * distance_speed_ratio));
		turn_speed = (Robot.center0/Math.abs(Robot.center0)) * Math.min(max_turn_speed, Math.abs(distance_turn_speed_ratio * Robot.center0)); // - means turn left
		tankDrive(drive_speed + turn_speed, drive_speed - turn_speed);
		if (Robot.distanceToTarget<errorDfromT){
			tankDrive(0, 0);
			driveToTarget = false;
		}
		if(!Robot.targetsFound){
			startDriveToTarget();
		}
	}
	
	/**
	 * Clamps a value between a minimum and a maximum value
	 * 
	 * @param val
	 *            the value to clamp
	 * @param min
	 *            the minimum value
	 * @param max
	 *            the maximum value
	 * @return the clamped value [min, max]
	 */
	private static double clamp(double val, double min, double max) {
		return Math.min(max, Math.max(val, val));
	}
	
	//TODO: find a way to get the bestTargetIndex from Robot.java instead of copying the bestTarget() function
	static double bestWHratio = 1;
/*	int bestTargetIndex;
	int bestTarget(double[] widths, double[] heights*//*, double[] areas*//*){
		if (widths.length == 0){
			return -1;
		}
		else{
			bestTargetIndex=0;
			for (int i=0; i<widths.length; i++){
				if (Math.abs(widths[i]/heights[i] - bestWHratio)<Math.abs(widths[bestTargetIndex]/heights[bestTargetIndex] - bestWHratio)){
					bestTargetIndex=i;
				}
				//add something to include area
			}
			return bestTargetIndex;
		}
	}*/
	
	NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport"); //is this necessary?
	double centerX;
	public void update() { 
		System.out.println("Ultrasonic range in inches "+ ultrasonic.getRangeInches());
		
		if (driveToTarget){
			driveToTarget();
		}
		
		
		
		System.out.println("Drivetrain update is being called. Going to print information about the robot turning towards the target here.");
		if (turnTowardsTarget){
			System.out.println("robot is turning towards target");
			double[] centerXs = table.getNumberArray("centerX", new double[0]);
			double[] widths = table.getNumberArray("width", new double[0]);
			double[] heights = table.getNumberArray("height", new double[0]);
			System.out.println("index of best target: " + Robot.bestTargetIndex);//Robot.bestTarget(widths, heights)
			if (widths.length>1){ //if targets found
				centerX = (centerXs[Robot.bestTargetIndex] + centerXs[Robot.secondBestTargetIndex])/2;
			}
			else{ //if no targets found
				centerX = center; //don't move
				System.out.println("no targets found");
			}
			System.out.println("center X of best target: " + centerX);
			if (centerX - center > error){ //turn right, X of target is on right side of screen
				targetDirection=1;				
			}
			else if (centerX - center < -error){ //turn left, X of target is on left side of screen
				targetDirection=-1;				
			}
			else{//don't turn, already within error of target
				targetDirection = 0;
				turnTowardsTarget = false;
			}
			System.out.println("target direction: " + targetDirection);
			tankDrive(targetDirection * speed, -targetDirection*speed);
			
		}
	}
}
