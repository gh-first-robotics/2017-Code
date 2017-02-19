package org.usfirst.frc.team5530.robot.system;

import org.usfirst.frc.team5530.robot.VisionProcessing;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Ultrasonic;
import edu.wpi.first.wpilibj.networktables.NetworkTable;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import edu.wpi.first.wpilibj.SPI;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
		

//TODO: should we change all units to meters and seconds, or should we leave them as inches and seconds
public class DriveTrain implements RobotSystem, PIDOutput {
	
	AHRS ahrs;
	PIDController turnController;
	double rotateToAngleRate;
	
	static final double kP = 0.03; //might be unnecessary
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kF = 0.00;
	
	static final double kToleranceDegrees = 4.0f;
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
	static double max_robot_speed= 120; //fastest speed the robot can drive at
	public int
		l1 = 2,
		l2 = 3,
		r1 = 0,
		r2 = 1;
	

	
	int allowableError = 5;
	public DriveTrain() {
		talons = new CANTalon[] { new CANTalon(l1), new CANTalon(l2), new CANTalon(r1), new CANTalon(r2) };
		driveTrainInit();
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
	}
	
	 public void driveTrainInit(){
		 
		talons[0].reverseSensor(false);
		talons[0].setInverted(false);
		talons[2].setInverted(true);
		//talons[2].reverseSensor(true);
		
		 autoDrive = false;
		 System.out.println("driveTrainInit called");
		talons[1].changeControlMode(TalonControlMode.Follower);
		talons[1].set(l1);
		talons[3].changeControlMode(TalonControlMode.Follower);
		talons[3].set(r1);
		talons[0].setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talons[0].configEncoderCodesPerRev(4);
		talons[2].setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talons[2].configEncoderCodesPerRev(4);
		talons[0].setPID(0.1, 0.00001, 0.0);
		talons[0].setPosition(0);
		talons[2].setPID(0.1, 0.00001, 0.0);
		talons[2].setPosition(0);
		talons[0].setAllowableClosedLoopErr(allowableError);
		talons[2].setAllowableClosedLoopErr(allowableError);
	}


	/**
	 * Updates the drive train based on the position of the joystick
	 * 
	 * @param stick
	 *            the joystick used to control the drive train
	 * @param reverse
	 *            true to reverse driving
	 */
	public void arcadeDrive(Vector2 stick, boolean speedMode) {
		double left = -stick.y + stick.x;
		double right = -stick.y - stick.x;
		
		if (!autoDrive || Math.abs(left) > .02 || Math.abs(right) > .02){
			System.out.println(left);
			System.out.println(right);
			autoDrive=false;
			
			if (speedMode) {
				tankDriveSpeed(left, right);
			} else {
				tankDrive(left, right);
			}
		}
		else{
			System.out.println("autodriving");
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
		talons[0].changeControlMode(TalonControlMode.PercentVbus);
		talons[2].changeControlMode(TalonControlMode.PercentVbus);
		lSpeed = clamp(lSpeed, -1, 1);
		rSpeed = clamp(rSpeed, -1, 1);
	//	System.out.println("l speed: "+lSpeed);
	//	System.out.println("r speed: "+ rSpeed);
		
		talons[0].set(lSpeed);	
		talons[2].set(rSpeed);
		
	}
	
	double multiply_distance_by = 40 * 5/3;
	public void tankDriveSpeed(double lSpeed, double rSpeed){
		talons[0].changeControlMode(TalonControlMode.Speed);
		talons[2].changeControlMode(TalonControlMode.Speed);
		lSpeed = clamp(lSpeed, -1, 1);
		rSpeed = clamp(rSpeed, -1, 1);
		
		talons[0].set(lSpeed * multiply_distance_by * max_robot_speed/speedRatio);	
		talons[2].set(-rSpeed * multiply_distance_by * max_robot_speed/speedRatio);
	}
	
	
	public void driveStraight(double lSpeed, double rSpeed){
		autoDrive=true;
		talons[0].changeControlMode(TalonControlMode.Speed);
		talons[2].changeControlMode(TalonControlMode.Speed);
		talons[0].set(lSpeed/speedRatio);	
		talons[2].set(rSpeed/speedRatio);
	}
	
	public void driveStraightDistance(double distance/*, double speed*/){
		autoDrive=true;
		talons[0].changeControlMode(TalonControlMode.Position);
		talons[2].changeControlMode(TalonControlMode.Position);
		//talons[0].pushMotionProfileTrajectory();
		talons[2].setSetpoint(talons[2].getPosition() - multiply_distance_by*distance/speedRatio);
		talons[0].setSetpoint(talons[0].getPosition() + multiply_distance_by*distance/speedRatio);
		
	}
	
	public void findSpeedRatio(){}
	
	
	public void resetAngle(){
		ahrs.reset();
	}
	
	boolean rotateToAngle = false;
	public void rotateToAngle(double angle){
		turnController.setSetpoint(angle);
        rotateToAngle = true;
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
	
	NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport"); //is this necessary?
	double centerX;
	public static double leftDistance = 0,
						 rightDistance = 0,
						 currentAngle = 0;
	public void update() { 
	//	System.out.println("Ultrasonic range in inches "+ ultrasonic.getRangeInches());
		
		leftDistance = talons[0].getPosition();
		rightDistance = talons[2].getPosition();
		currentAngle = ahrs.getAngle();
		
		SmartDashboard.putNumber("test get bus voltage", talons[0].getBusVoltage());
		SmartDashboard.putNumber("left getEncPosition", talons[0].getEncPosition());
		SmartDashboard.putNumber("right getEncPosition", talons[2].getEncPosition());
		SmartDashboard.putNumber("left getEncVelocity", talons[0].getEncVelocity());
		SmartDashboard.putNumber("right getEncVelocity", talons[2].getEncVelocity());
		SmartDashboard.putNumber("left getPosition", talons[0].getPosition());
		SmartDashboard.putNumber("right getPosition", talons[2].getPosition());
		SmartDashboard.putNumber("left getSpeed", talons[0].getSpeed());
		SmartDashboard.putNumber("right getSpeed", talons[2].getSpeed());
		
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
		
	//	System.out.println("Drivetrain update is being called. Going to print information about the robot turning towards the target here.");
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
