
package org.usfirst.frc.team5530.robot;

import org.usfirst.frc.team5530.robot.autonomous.AutoProgram;
import org.usfirst.frc.team5530.robot.autonomous.ChillyFries;
import org.usfirst.frc.team5530.robot.autonomous.LowBar;
import org.usfirst.frc.team5530.robot.autonomous.Other;
import org.usfirst.frc.team5530.robot.autonomous.Porticullis;
import org.usfirst.frc.team5530.robot.input.ControlState;
import org.usfirst.frc.team5530.robot.input.Controls;
import org.usfirst.frc.team5530.robot.macros.Macro;
import org.usfirst.frc.team5530.robot.system.DriveTrain;
import org.usfirst.frc.team5530.robot.system.LowArm;
import org.usfirst.frc.team5530.robot.system.RobotSystem;
import org.usfirst.frc.team5530.robot.system.Scaler;
import org.usfirst.frc.team5530.robot.system.Shooter;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.CANTalon;
import edu.wpi.first.wpilibj.CameraServer;
import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.SampleRobot;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.Timer;
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
	private Controls controls;
	private boolean enableScale;
	private boolean reverseDriving;

	private static final String chillyFries = "Cheval de Frise";
	private static final String portcullis = "Portcullis";
	private static final String lowBar = "Low Bar";
	private static final String other = "Default";
	private SendableChooser defenseChooser;

	private static final String[] defenses = { other, lowBar, portcullis, chillyFries };
	private static final String[] positions = { "1", "2", "3", "4", "5" };
	private SendableChooser positionChooser;

	public Robot() {
		Joystick stick1 = new Joystick(0);
		Joystick stick2 = new Joystick(1);
		controls = new Controls(stick1, stick2);
	}

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
			positionChooser.addObject(positions[i], positions[i]);
		SmartDashboard.putData("Starting Position", positionChooser);

		defenseChooser.addDefault(defenses[0], defenses[0]);
		for (int i = 1; i < defenses.length; i++)
			defenseChooser.addObject(defenses[i], defenses[i]);
		SmartDashboard.putData("Starting Defense", defenseChooser);

		camera = new USBCamera("cam0");
		CameraServer.getInstance().setQuality(20);
		CameraServer.getInstance().startAutomaticCapture(camera);

		/*
		 * Accelerometer accel; accel = new BuiltInAccelerometer(); accel = new
		 * BuiltInAccelerometer(Accelerometer.Range.k4G); double xVal =
		 * accel.getX(); double yVal = accel.getY(); double zVal = accel.getZ();
		 */
	}

	/*
	 * private void drive(double distance, double max_speed, double direction,
	 * AutonomousTimer timer) { // distance in feet //
	 * SmartDashboard.putNumber("acceleration", .75); //
	 * SmartDashboard.putNumber("max speed", .75); //
	 * SmartDashboard.putNumber("distance", .75); //
	 * SmartDashboard.putNumber("friction", .05);
	 * 
	 * double friction = 0; // SmartDashboard.getNumber("friction", .05); double
	 * acceleration = 0.75; // SmartDashboard.getNumber("acceleration", // .75);
	 * // max_speed = 0.5; //SmartDashboard.getNumber("max speed", .75); // //
	 * max_speed must be less than 1 - friction // distance =
	 * SmartDashboard.getNumber("distance", .75); // distance /= (12 + 7/12); //
	 * comment out this line before running // test, but leave it in if running
	 * low bar double target_distance = distance; double a_d_distance_units = 1
	 * / 3; double a_d_distance = 1 + 7 / 12; // actual acceleration distance +
	 * // actual deceleration distance // // line 1 of test //in feet double
	 * unit_distance = 12 + 3.5 / 12 - a_d_distance; // distance robot //
	 * travels when // driving 1 at // maximum speed // //line 2 of // test - //
	 * a_d_distance // // feet/unit distance = ((target_distance - a_d_distance)
	 * / unit_distance) + a_d_distance_units; System.out.println(friction);
	 * System.out.println(acceleration); System.out.println(max_speed);
	 * System.out.println(distance); double k = 1; // ft/unit convert between
	 * distance robot travels in 1 // second at highest speed and feet double
	 * time_to_reach_max_speed = max_speed / acceleration; double d1 =
	 * (max_speed * max_speed) / (2 * acceleration);
	 * 
	 * if (d1 > distance / 2) { System.out.println("ERROR: d1 > distance / 2");
	 * // acceleration_distance = target_distance / 2; // max_speed =
	 * Math.sqrt(target_distance * acceleration); // time_to_reach_max_speed =
	 * max_speed / acceleration; // SmartDashboard.putNumber("new max_speed",
	 * max_speed); } if (max_speed > 1 - friction) { System.out.println(
	 * "ERROR: max_speed > 1 - friction"); }
	 * 
	 * double velocity; double total_time = (2 * time_to_reach_max_speed) +
	 * ((distance - (2 * d1)) / max_speed); // double time; long time =
	 * System.currentTimeMillis(); double diff;
	 * 
	 * while ((diff = (System.currentTimeMillis() - time) / 1000.0) <
	 * total_time) { if (diff < time_to_reach_max_speed) { velocity =
	 * acceleration * diff; } else if (diff > time_to_reach_max_speed && diff <
	 * total_time - time_to_reach_max_speed) { velocity = max_speed; } else {
	 * velocity = acceleration * (total_time - diff); }
	 * driveTrain.drive(direction * (velocity + friction) * k);
	 * 
	 * SmartDashboard.putNumber("velocity", velocity);
	 * SmartDashboard.putNumber("time", diff);
	 * 
	 * timer.delay(5); } driveTrain.drive(0);
	 * 
	 * };
	 * 
	 * private void driveA(double distance, double direction, AutonomousTimer
	 * timer) { // direction // = // 1: // turn // left // //direction // = //
	 * -1: // turn // right Accelerometer accel; // accel = new
	 * BuiltInAccelerometer(); accel = new
	 * BuiltInAccelerometer(Accelerometer.Range.k4G); double xVal =
	 * accel.getX(); // double yVal = accel.getY(); double zVal = accel.getZ();
	 * double zValFeet = zVal * 32.2; double zVelocity = 0; double zPosition =
	 * 0; double zChange; double xValFeet = xVal * 32.2; double xVelocity = 0;
	 * double xPosition = 0;
	 * 
	 * double accelerometer_error = 0.01; // double turn_friction =
	 * SmartDashboard.getNumber("turn friction", 0); SmartDashboard.putNumber(
	 * "drive a acceleration", .75); SmartDashboard.putNumber(
	 * "max drive a speed", .75); SmartDashboard.putNumber("min drive a speed",
	 * .2); double acceleration = SmartDashboard.getNumber(
	 * "drive a acceleration", .75); double min_speed =
	 * SmartDashboard.getNumber("min drive a speed", .2); double max_speed =
	 * SmartDashboard.getNumber("max drive a speed", .75);// max_speed // must
	 * // be // less // than // 1 // - // friction
	 * 
	 * System.out.println(acceleration); System.out.println(max_speed);
	 * System.out.println(distance);
	 * 
	 * double distance_traveled = 0; double deceleration_time; double total_time
	 * = 0;
	 * 
	 * double velocity;
	 * 
	 * long time = System.currentTimeMillis(); double diff;
	 * 
	 * double deltatime; long lasttime = System.currentTimeMillis(); while
	 * (distance_traveled < distance) { diff = (System.currentTimeMillis() -
	 * time) / 1000.0; deltatime = (System.currentTimeMillis() - lasttime) /
	 * 1000.0; lasttime = System.currentTimeMillis(); xVal = accel.getX(); //
	 * yVal = accel.getY(); zVal = accel.getZ(); zValFeet = zVal * 32.2;
	 * zVelocity += zValFeet * deltatime; zPosition += zVelocity * deltatime;
	 * zChange = zPosition * (distance_traveled / distance); if (zChange > 1 -
	 * max_speed) { zChange = 1 - max_speed; } if (zChange < max_speed - 1) {
	 * zChange = max_speed - 1; } if (Math.abs(zChange) < accelerometer_error) {
	 * zChange = 0; } xValFeet = xVal * 32.2; xVelocity += xValFeet * deltatime;
	 * xPosition += xVelocity * deltatime; distance_traveled =
	 * Math.abs(xPosition); if (distance_traveled < distance / 2) { velocity =
	 * acceleration * diff; deceleration_time = diff; total_time =
	 * deceleration_time * 2; } else { velocity = acceleration * (total_time -
	 * diff); } if (velocity > max_speed) { velocity = max_speed; } if (velocity
	 * < min_speed) { velocity = min_speed; } driveTrain.tankDrive((direction *
	 * velocity) + zChange, (direction * velocity) - zChange);
	 * 
	 * SmartDashboard.putNumber("velocity", velocity);
	 * SmartDashboard.putNumber("time", diff);
	 * 
	 * timer.delay(5); } driveTrain.drive(0);
	 * 
	 * };
	 * 
	 * private void turn(double radians, double direction, AutonomousTimer
	 * timer) { // direction // = // 1: // turn // left // //direction // = //
	 * -1: // turn // right // currently set up for low bar... Do NOT change
	 * anything about this // SmartDashboard.putNumber("acceleration", .75); //
	 * SmartDashboard.putNumber("max speed", .75); //
	 * SmartDashboard.putNumber("distance", .75); //
	 * SmartDashboard.putNumber("robot_radius", 2); //
	 * SmartDashboard.putNumber("turn_friction", .05); // double robot_radius =
	 * SmartDashboard.getNumber("robot_radius", 0.5);
	 * 
	 * double robot_radius = 0.5; double distance = robot_radius * radians;
	 * double turn_friction = SmartDashboard.getNumber("turn friction", 0);
	 * double acceleration = SmartDashboard.getNumber("turn acceleration", .75);
	 * double max_speed = SmartDashboard.getNumber("max turn speed", .75); //
	 * max_speed // must // be // less // than // 1 // - // friction // radians
	 * = SmartDashboard.getNumber("radians", Math.PI/2);
	 * System.out.println(robot_radius); System.out.println(turn_friction);
	 * System.out.println(acceleration); System.out.println(max_speed);
	 * System.out.println(radians); System.out.println(distance); double k = 1;
	 * // / (14 + 1/6); // ft/unit convert between distance robot // travels in
	 * 1 // second at highest speed and feet double time_to_reach_max_speed =
	 * max_speed / acceleration; double d1 = (max_speed * max_speed) / (2 *
	 * acceleration);
	 * 
	 * if (d1 > distance / 2) { System.out.println(
	 * "ERROR: d1 > distance / 2 turn"); // acceleration_distance =
	 * target_distance / 2; // max_speed = Math.sqrt(target_distance *
	 * acceleration); // time_to_reach_max_speed = max_speed / acceleration; //
	 * SmartDashboard.putNumber("new max_speed", max_speed); } if (max_speed > 1
	 * - turn_friction) { System.out.println("ERROR: max_speed > 1 - friction");
	 * }
	 * 
	 * double velocity; double total_time = (2 * time_to_reach_max_speed) +
	 * ((distance - (2 * d1)) / max_speed); // double time; long time =
	 * System.currentTimeMillis(); double diff;
	 * 
	 * while ((diff = (System.currentTimeMillis() - time) / 1000.0) <
	 * total_time) { if (diff < time_to_reach_max_speed) { velocity =
	 * acceleration * diff; } else if (diff > time_to_reach_max_speed && diff <
	 * total_time - time_to_reach_max_speed) { velocity = max_speed; } else {
	 * velocity = acceleration * (total_time - diff); }
	 * driveTrain.tankDrive(direction * ((velocity + turn_friction) * k),
	 * -direction * ((velocity + turn_friction) * k));
	 * 
	 * SmartDashboard.putNumber("velocity", velocity);
	 * SmartDashboard.putNumber("time", diff);
	 * 
	 * timer.delay(5); } driveTrain.drive(0);
	 * 
	 * };
	 * 
	 * private void turnA(double radians, double direction, AutonomousTimer
	 * timer) { // direction // = // 1: // turn // left // //direction // = //
	 * -1: // turn // right Accelerometer accel; System.out.println("turnA"); //
	 * accel = new BuiltInAccelerometer(); accel = new
	 * BuiltInAccelerometer(Accelerometer.Range.k4G); // double xVal =
	 * accel.getX(); // double yVal = accel.getY(); double zVal = accel.getZ();
	 * double zValFeet = zVal * 32.2; double zVelocity = 0; double zPosition =
	 * 0; // double degrees_traveled; //= Math.acos(zVal); double
	 * accelerometerturnradius = 0.66; // in feet double distance =
	 * accelerometerturnradius * radians; // double test = (8 + 3/8)/12; //
	 * distance = accelerometerturnradius; // double turn_friction =
	 * SmartDashboard.getNumber("turn friction", 0); double acceleration =
	 * SmartDashboard.getNumber("turn acceleration", .75); double max_speed =
	 * SmartDashboard.getNumber("max turn speed", .75);// max_speed // must //
	 * be // less // than // 1 // - // friction double min_speed =
	 * SmartDashboard.getNumber("min turn speed", .2); // radians =
	 * SmartDashboard.getNumber("radians", Math.PI/2); //
	 * System.out.println(robot_radius); // System.out.println(turn_friction);
	 * // System.out.println(acceleration); // System.out.println(max_speed); //
	 * System.out.println(radians); // System.out.println(distance); // double k
	 * = 1; // / (14 + 1/6); // ft/unit convert between distance // robot
	 * travels in 1 // second at highest speed and feet // double
	 * time_to_reach_max_speed = max_speed / acceleration; // double d1 =
	 * (max_speed * max_speed) / (2 * acceleration); double distance_traveled =
	 * 0; double deceleration_time; double total_time = 0; // if (d1 > distance
	 * / 2) { // System.out.println("ERROR: d1 > distance / 2 turnA"); //
	 * acceleration_distance = target_distance / 2; // max_speed =
	 * Math.sqrt(target_distance * acceleration); // time_to_reach_max_speed =
	 * max_speed / acceleration; // SmartDashboard.putNumber("new max_speed",
	 * max_speed); // } // if (max_speed > 1 - turn_friction){ //
	 * System.out.println("ERROR: max_speed > 1 - friction"); // }
	 * 
	 * double velocity; // double total_time = (2 * time_to_reach_max_speed) +
	 * ((distance - (2 * // d1)) / max_speed); // double time; long time =
	 * System.currentTimeMillis(); double diff;
	 * 
	 * double deltatime; System.out.println("distance " + distance);
	 * System.out.println("distance traveled " + distance_traveled); long
	 * lasttime = System.currentTimeMillis(); while (distance_traveled <
	 * distance) { System.out.println("distance_traveled " + distance_traveled);
	 * System.out.println("distance " + distance); diff =
	 * (System.currentTimeMillis() - time) / 1000.0; deltatime =
	 * (System.currentTimeMillis() - lasttime) / 1000.0; lasttime =
	 * System.currentTimeMillis(); // xVal = accel.getX(); // yVal =
	 * accel.getY(); zVal = accel.getZ(); if (Math.abs(zVal) < 0.1) { zVal = 0;
	 * } zValFeet = zVal * 32.2; zVelocity += zValFeet * deltatime; zPosition +=
	 * zVelocity * deltatime; distance_traveled = Math.abs(zPosition); if
	 * (distance_traveled < distance / 2) { velocity = acceleration * diff;
	 * deceleration_time = diff; total_time = deceleration_time * 2; } else {
	 * velocity = acceleration * (total_time - diff); } if (velocity >
	 * max_speed) { velocity = max_speed; } if (velocity < min_speed) { velocity
	 * = min_speed; } driveTrain.tankDrive(direction * velocity, -direction *
	 * velocity);
	 * 
	 * SmartDashboard.putNumber("velocity", velocity);
	 * SmartDashboard.putNumber("zVal", zVal); SmartDashboard.putNumber("time",
	 * diff);
	 * 
	 * timer.delay(5); } driveTrain.drive(0);
	 * 
	 * };
	 */

	@Override
	public void autonomous() {
		start("Autonomous");

		String defense = (String) defenseChooser.getSelected();
		String position = (String) positionChooser.getSelected();

		System.out.println("Running  " + defense + " at position " + position);

		AutoProgram program;
		switch (defense) {
		case chillyFries:
			program = new ChillyFries(position.equals("1"));
			break;
		case portcullis:
			program = new Porticullis();
			break;
		case lowBar:
			program = new LowBar(position.equals("1"));
			break;
		case other:
			program = new Other();
			break;
		default:
			return;
		}

		program.start(this);
	}

	@Override
	public void operatorControl() {
		start("Teleoperation");
		ControlState previous, current = controls.update();
		while (isOperatorControl() && isEnabled()) {
			previous = current;
			current = controls.update();

			// tick(previous, current);

			for (RobotSystem system : systems) {
				system.update();
			}

			Timer.delay(0.005);
		}
	}

	@Override
	public void disabled() {
		start("Disabled");
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

	/*
	 * private void tick(ControlState last, ControlState state) {
	 * driveTrain.arcadeDrive(state.getStick(0), reverseDriving); if
	 * (enableScale) scaler.move(state.getStick(1));
	 * 
	 * if (state.isPressed(InputButton.Raise_Arm)) lowArm.raise(); else if
	 * (state.isPressed(InputButton.Lower_Arm)) lowArm.lower(); else
	 * lowArm.stop();
	 * 
	 * if (state.isNewlyPressed(InputButton.Reverse_Steering, last))
	 * reverseDriving = !reverseDriving;
	 * 
	 * if (state.isNewlyPressed(InputButton.Intake, last))
	 * shooter.toggleIntake();
	 * 
	 * if (state.isNewlyPressed(InputButton.Shoot_Max, last))
	 * shooter.shootRaw(1);
	 * 
	 * if (state.isNewlyPressed(InputButton.Shoot_5, last)) shooter.shoot(5 *
	 * 12);
	 * 
	 * if (state.isNewlyPressed(InputButton.Shoot_10, last)) shooter.shoot(10 *
	 * 12);
	 * 
	 * if (state.isNewlyPressed(InputButton.Shoot_Low, last))
	 * shooter.shootLow();
	 * 
	 * if (state.isNewlyPressed(InputButton.Toggle_Scaler, last)) enableScale =
	 * !enableScale;
	 * 
	 * if (state.isNewlyPressed(InputButton.Lock_Scaler, last)) scaler.lock();
	 * 
	 * if (state.isNewlyPressed(InputButton.Unlock_Scaler, last))
	 * scaler.unlock();
	 * 
	 * if (state.isNewlyPressed(InputButton.Shoot_Finish, last))
	 * shooter.launch(); }
	 */
	private static void start(String mode) {
		System.out.println("#    " + mode + "    #");
	}
}
