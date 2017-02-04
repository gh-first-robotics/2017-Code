package org.usfirst.frc.team5530.robot.system;

import org.usfirst.frc.team5530.robot.VisionProcessing;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import com.ctre.CANTalon;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;

public class DriveTrain implements RobotSystem, PIDOutput {
	AHRS ahrs;
	PIDController turnController;
	double rotateToAngleRate;
	
	static final double kP = 0.03; //might be unnecessary
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kF = 0.00;
	
	static final double kToleranceDegrees = 4.0f;
	 
	private CANTalon[] talons;
	public Ultrasonic ultrasonic = new Ultrasonic(0,1);
	public static boolean autoDrive = false;
	public DriveTrain(CANTalon l1, CANTalon l2, CANTalon r1, CANTalon r2) {
		talons = new CANTalon[] { l1, l2, r1, r2 };
		try {
	          /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */
	          /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
	          /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
	          ahrs = new AHRS(SPI.Port.kMXP); 
	      } catch (RuntimeException ex ) {
	          DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
	      }
	      turnController = new PIDController(kP, kI, kD, kF, ahrs, this);
	      turnController.setInputRange(-180.0f,  180.0f);
	      turnController.setOutputRange(-1.0, 1.0);
	      turnController.setAbsoluteTolerance(kToleranceDegrees);
	      turnController.setContinuous(true);
	      
	      /* Add the PID Controller to the Test-mode dashboard, allowing manual  */
	      /* tuning of the Turn Controller's P, I and D coefficients.            */
	      /* Typically, only the P value needs to be modified.                   */
	     
	  }
		public void resetAngle(){
			ahrs.reset();
		}
		
		boolean rotateToAngle = false;
		public void rotateToAngle(double angle){
			turnController.setSetpoint(angle);
            rotateToAngle = true;
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
		System.out.println("l speed: "+lSpeed);
		System.out.println("r speed: "+ rSpeed);
		talons[0].set(-lSpeed /*0.5*/);
		talons[1].set(-lSpeed/*0.5*/);
		talons[2].set(rSpeed * 0.8);
		talons[3].set(rSpeed * 0.8);
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
		
		double currentRotationRate;
        if ( rotateToAngle ) {
            turnController.enable();
            currentRotationRate = rotateToAngleRate;
            tankDrive(rotateToAngleRate, -rotateToAngleRate); 
            // the - might have to be on the left instead of the right
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

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		rotateToAngleRate = output;
	}
}
