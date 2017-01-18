//TODO: Update GRIP!!!!
package org.usfirst.frc.team5530.robot;

import org.usfirst.frc.team5530.robot.autonomous.AutoProgram;
import org.usfirst.frc.team5530.robot.autonomous.ChillyFries;
import org.usfirst.frc.team5530.robot.autonomous.LowBar;
import org.usfirst.frc.team5530.robot.autonomous.Other;
import org.usfirst.frc.team5530.robot.autonomous.Porticullis;
import org.usfirst.frc.team5530.robot.macros.Macro;
import org.usfirst.frc.team5530.robot.system.DriveTrain;
import org.usfirst.frc.team5530.robot.system.LowArm;
import org.usfirst.frc.team5530.robot.system.RobotSystem;
import org.usfirst.frc.team5530.robot.system.Scaler;
import org.usfirst.frc.team5530.robot.system.Shooter;
import org.usfirst.frc.team5530.robot.teleop.Operator;

import edu.wpi.first.wpilibj.AnalogInput;
import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
import edu.wpi.first.wpilibj.networktables.NetworkTable;
import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import edu.wpi.first.wpilibj.vision.USBCamera;

/**
 * This is a demo program showing the use of the RobotDrive class. The
 * SampleRobot class is the base of a robot application that will automatically
 * call your Autonomous and OperatorControl methods at the right time as
 * controlled by the switches on the driver station or the field controls.
 * <p>
 * The VM is configured to automatically run this class, and to call the
 * functions corresponding to each mode, as described in the SampleRobot
 * documentation. If you change the name of this class or the package after
 * creating this project, you must also update the manifest file in the resource
 * directory.
 * <p>
 * WARNING: While it may look like a good choice to use for your code if you're
 * inexperienced, don't. Unless you know what you are doing, complex code will
 * be much more difficult under this system. Use IterativeRobot or Command-Based
 * instead if you're new.
 */
public class Robot extends SampleRobot {
	private RobotSystem[] systems;

	private USBCamera camera;
	private Operator teleop;
	
	private static final AutoProgram[] autons = { new ChillyFries(), new Porticullis(), new LowBar(), new Other() };
	private SendableChooser defenseChooser;

	private static final String[] positions = { "1", "2", "3", "4", "5" };
	private SendableChooser positionChooser;
	
	@Override
	public void robotInit() {
		// Left, Left, Right, Right
		RobotSystem driveTrain = new DriveTrain(new CANTalon(8), new CANTalon(1), new CANTalon(2), new CANTalon(3));
		// Intake, Switch1, Switch2, Shooter1, Shooter2
		RobotSystem shooter = new Shooter(new CANTalon(6), new DigitalInput(9), new DigitalInput(8), new CANTalon(4), new CANTalon(5));
		// Arm, ArmAngle
		RobotSystem lowArm = new LowArm(new CANTalon(7), new AnalogInput(1));
		// Scaler
		RobotSystem scaler = new Scaler(new CANTalon(0), new Servo(0));

		systems = new RobotSystem[] { driveTrain, shooter, lowArm, scaler };

		defenseChooser = new SendableChooser();
		positionChooser = new SendableChooser();

		positionChooser.addDefault(positions[0], positions[0]);
		for (int i = 1; i < positions.length; i++)
			positionChooser.addObject(positions[i], i);
		SmartDashboard.putData("Starting Position", positionChooser);

		defenseChooser.addDefault(autons[0].getName(), autons[0]);
		for (int i = 1; i < autons.length; i++)
			defenseChooser.addObject(autons[i].getName(), i);
		SmartDashboard.putData("Starting Defense", defenseChooser);

		camera = new USBCamera("cam0");
		//CameraServer.getInstance().setQuality(20);
		CameraServer.getInstance().startAutomaticCapture();


		Joystick stick1 = new Joystick(0);
		Joystick stick2 = new Joystick(1);

		teleop = new Operator(this, stick1, stick2);

		/*
		 * Accelerometer accel; accel = new BuiltInAccelerometer(); accel = new
		 * BuiltInAccelerometer(Accelerometer.Range.k4G); double xVal =
		 * accel.getX(); double yVal = accel.getY(); double zVal = accel.getZ();
		 */
	}

	@Override
	public void autonomous() {
		start("Autonomous");

		AutoProgram program = (AutoProgram) defenseChooser.getSelected();
		int position = (int) positionChooser.getSelected();

		System.out.println("Running  " + program.getName() + " at position " + position);
		program.run(this, Integer.parseInt((String) positionChooser.getSelected()));
	}

	@Override
	public void operatorControl() {
		start("Teleoperation");
		while (isOperatorControl() && isEnabled()) {
			teleop.tick();
			printTargetInformation();
			sleep(5);
		}
	}
	public static double bestWHratio = 1; //figure out what this is
	//largest area is best???
	public int bestTargetIndex;
	public static int secondBestTargetIndex;
	double screenCenter = 50;
	int bestTarget(double[] widths, double[] heights/*, double[] areas*/){
		if (widths.length == 0){
			return -1;
		}
		else{
			bestTargetIndex=0;
			secondBestTargetIndex=0;
			for (int i=0; i<widths.length; i++){
				if (Math.abs(widths[i]/heights[i] - bestWHratio)<Math.abs(widths[bestTargetIndex]/heights[bestTargetIndex] - bestWHratio)){
					secondBestTargetIndex=bestTargetIndex;
					bestTargetIndex=i;
				}
				//add something to include area
			}
			return bestTargetIndex;
		}
	}
	public int getbestTargetIndex(){
		return bestTargetIndex;
	}
	static double k = 21*40; //is this the correct number?
	public static double distanceToTarget(double width){ //TODO: change to use height in calculations as well
		return k/width;
	}
	public static double center0;
	public static double distanceToTarget;
	NetworkTable table = NetworkTable.getTable("GRIP/myContoursReport");
	public void printTargetInformation(){		
		double[] areas = table.getNumberArray("area", new double[0]);
		double[] widths = table.getNumberArray("width", new double[0]);
		double[] heights = table.getNumberArray("height", new double[0]);
		double[] centerXs = table.getNumberArray("centerX", new double[0]);
		double[] centerYs = table.getNumberArray("centerY", new double[0]);
		double[] solidities = table.getNumberArray("solidity", new double[0]);
		System.out.println(table.isConnected()); //prints true
		System.out.println(areas.length); //prints number of targets found
		if (areas.length != 0){
			System.out.println("index of best target: "+bestTarget(widths, heights));
			System.out.println("width: "+widths[bestTargetIndex]);
			table.putNumber("bestTargetIndex", bestTargetIndex);
			distanceToTarget = distanceToTarget(widths[bestTargetIndex]);
			System.out.println("distance to target: "+distanceToTarget(widths[bestTargetIndex]));
			table.putNumber("indexOfBestTargetdistanceToTarget", distanceToTarget(widths[bestTargetIndex]));
			center0= centerXs[bestTargetIndex] - screenCenter;
			System.out.println("center X of best target: "+centerXs[bestTargetIndex]);
		}
	}
	
	@Override
	public void disabled() {
		start("Disabled");				
		while (isDisabled()) {
			printTargetInformation();
			Timer.delay(.5);
		}
	}

	@Override
	public void test() {
		start("Test");
	}

	@SuppressWarnings("unchecked")
	public <T extends RobotSystem> T getSystem(Class<T> clazz) {
		for (RobotSystem sys : systems) {
			if (sys.getClass().equals(clazz)) {
				return (T) sys;
			}
		}
		throw new IllegalArgumentException("Found no system of type " + clazz.getName());
	}

	public void execute(Macro m) {
		m.run(this);
	}

	public void sleep(int millis) {
		long end = System.currentTimeMillis() + millis;
		while (System.currentTimeMillis() < end) {
			for (RobotSystem system : systems) {
				system.update();
			}

			if (System.currentTimeMillis() < end)
				Timer.delay(5 / 1000.0);
		}
	}

	private static void start(String mode) {
		System.out.println("#    " + mode + "    #");
	}
}
