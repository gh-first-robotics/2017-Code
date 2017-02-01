package org.usfirst.frc.team5530.robot.system;

import org.usfirst.frc.team5530.robot.VisionProcessing;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
//TODO: should we change all units to meters and seconds, or should we leave them as inches and seconds
public class DriveTrain implements RobotSystem {
	static double wheel_radius = 2;
	static double gear_ratio = 1;
	//static double max_rpm = 1500; //highest rpm of what encoder is measuring
/*inches*/	static double speedRatio= 2 * Math.PI * wheel_radius * gear_ratio; //get this number by having the robot drive, 
									 // then dividing the actual distance the robot drove
									//  by the distance the encoder measured that the robot drove
								   //   or 2 * pi * radius * gear_ratio   ???
	private CANTalon[] talons;
	public Ultrasonic ultrasonic = new Ultrasonic(0,1);
	public static boolean autoDrive = false;
	static double max_robot_speed= 10; //fastest speed the robot can drive at
	public int
		r1 = 0,
		r2 = 1,
		l1 = 2,
		l2 = 3;
	
/*	public static double
		wheel_radius,
		gear_ratio;
*/
	int allowableError = 10;
	public DriveTrain() {
		talons = new CANTalon[] { new CANTalon(r1), new CANTalon(r2), new CANTalon(l1), new CANTalon(l2) };
		driveTrainInit();
	}
	
	 public void driveTrainInit(){
		 System.out.println("driveTrainInit called");
	//	 talons[l2].reverseOutput(true);
	//	 talons[r2].reverseOutput(true);
		talons[l2].changeControlMode(TalonControlMode.Follower);
		talons[l2].set(l1);
		talons[r2].changeControlMode(TalonControlMode.Follower);
		talons[r2].set(r1);
		talons[l1].setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talons[l1].reverseSensor(false);
		talons[l1].configEncoderCodesPerRev(4);
		talons[r1].setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talons[r1].reverseSensor(false);
		talons[r1].configEncoderCodesPerRev(4);
		talons[l1].setPID(0.5, 0.001, 0.0);
		talons[l1].setPosition(0);
		talons[r1].setPID(0.5, 0.001, 0.0);
		talons[r1].setPosition(0);
		talons[l1].setAllowableClosedLoopErr(allowableError);
		talons[r1].setAllowableClosedLoopErr(allowableError);
	}

	/**
	 * Updates the drive train based on the position of the joystick
	 * 
	 * @param stick
	 *            the joystick used to control the drive train
	 * @param reverse
	 *            true to reverse driving
	 */
	 public static boolean stickMoved = false;
	public void arcadeDrive(Vector2 stick, boolean reverse) {
		double left = -stick.y - stick.x;
		double right = -stick.y + stick.x;
if (left == 0 && right ==0){stickMoved = false;}
else {stickMoved = true;}
if (!autoDrive || stickMoved){
	autoDrive = false;
		if (reverse) {
			tankDrive(right, left);
		} else {
			tankDrive(left, right);
		}
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
		talons[l1].changeControlMode(TalonControlMode.PercentVbus);
		talons[r1].changeControlMode(TalonControlMode.PercentVbus);
		lSpeed = clamp(lSpeed, -1, 1);
		rSpeed = clamp(rSpeed, -1, 1);
		System.out.println("l speed: "+lSpeed);
		System.out.println("r speed: "+ rSpeed);
		talons[l1].set(rSpeed);	
		talons[r1].set(-lSpeed);
	}
	
	public void driveStraight(double lSpeed, double rSpeed){
		autoDrive=true;
		talons[l1].changeControlMode(TalonControlMode.Speed);
		talons[r1].changeControlMode(TalonControlMode.Speed);
		talons[l1].set(lSpeed/speedRatio);	
		talons[r1].set(rSpeed/speedRatio);
	}
	
	public void driveStraightDistance(double distance/*, double speed*/){
		autoDrive=true;
		talons[l1].changeControlMode(TalonControlMode.Position);
		talons[r1].changeControlMode(TalonControlMode.Position);
		//talons[0].pushMotionProfileTrajectory();
		talons[l1].setSetpoint(talons[l1].getPosition() + distance/speedRatio);
		talons[r1].setSetpoint(talons[r1].getPosition() + distance/speedRatio);
	}
	
	public void findSpeedRatio(){}
	
	
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
	double max_speed = .4;
	double min_speed = 0.15;
	double max_turn_speed = 0.1;
//	double min_turn_speed = 0.2;
	double distance_speed_ratio = 1/10;
	double distance_turn_speed_ratio = 0.1;
	double errorDfromT = 17; //make this minimum distance that target can fully be seen from. This number was previously 10
	public void startDriveToTarget(){
		if (driveToTarget){
			driveToTarget = false;
			tankDrive(0,0);
			autoDrive=false;
		}
		else{
			driveToTarget = true;
			autoDrive=true;
		}
	}
	public void driveToTarget(){ //called repeatedly if driveToTarget==true
		autoDrive=true;
		drive_speed = Math.min(1, Math.max(min_speed, VisionProcessing.distanceToTarget * distance_speed_ratio));
		turn_speed = (VisionProcessing.center0/Math.abs(VisionProcessing.center0)) * Math.min(max_turn_speed, Math.abs(distance_turn_speed_ratio * VisionProcessing.center0)); // - means turn left
		System.out.println("WARNING: turn speed"+turn_speed);
		tankDrive(drive_speed - turn_speed, drive_speed + turn_speed);
	//	tankDrive(0.1, 0.1);
		if (VisionProcessing.distanceToTarget<errorDfromT){
			tankDrive(0, 0);
			driveToTarget = false;
		}
		if(!VisionProcessing.targetsFound){
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
	
	NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport"); //is this necessary?
	double centerX;
	public void update() { 
		System.out.println("Ultrasonic range in inches "+ ultrasonic.getRangeInches());
		
		SmartDashboard.putNumber("left getEncPosition", talons[l1].getEncPosition());
		SmartDashboard.putNumber("right getEncPosition", talons[r1].getEncPosition());
		SmartDashboard.putNumber("left getEncVelocity", talons[l1].getEncVelocity());
		SmartDashboard.putNumber("right getEncVelocity", talons[r1].getEncVelocity());
		SmartDashboard.putNumber("left getPosition", talons[l1].getPosition());
		SmartDashboard.putNumber("right getPosition", talons[r1].getPosition());
		SmartDashboard.putNumber("left getSpeed", talons[l1].getSpeed());
		SmartDashboard.putNumber("right getSpeed", talons[r1].getSpeed());
		
		if (driveToTarget){
			driveToTarget();
		}
		
		
		
		System.out.println("Drivetrain update is being called. Going to print information about the robot turning towards the target here.");
		if (turnTowardsTarget){
			System.out.println("robot is turning towards target");
			double[] centerXs = table.getNumberArray("centerX", new double[0]);
			double[] widths = table.getNumberArray("width", new double[0]);
			double[] heights = table.getNumberArray("height", new double[0]);
			System.out.println("index of best target: " + VisionProcessing.bestTargetIndex);//Robot.bestTarget(widths, heights)
			if (widths.length>1){ //if targets found
				centerX = (centerXs[VisionProcessing.bestTargetIndex] + centerXs[VisionProcessing.secondBestTargetIndex])/2;
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
