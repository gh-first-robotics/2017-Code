
package org.usfirst.frc.team5530.robot;

import org.usfirst.frc.team5530.robot.AutonomousSchedule.AutonomousTimer;
import org.usfirst.frc.team5530.robot.Controls.ControlState;
import org.usfirst.frc.team5530.robot.system.DriveTrain;
import org.usfirst.frc.team5530.robot.system.IterativeSystem;
import org.usfirst.frc.team5530.robot.system.LowArm;
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
	private IterativeSystem[] systems;
	private DriveTrain driveTrain;
	private Shooter shooter;
	private LowArm lowArm;
	private Scaler scaler;

	private USBCamera camera;
	private Controls controls;
	private boolean enableScale;
	private boolean reverseDriving;

	private static final String chillyFries = "Cheval de Frise";
	private static final String portcullis = "Portcullis";
	private static final String lowBar = "Low Bar";
	private static final String other = "Default";
	private static final String test = "Test";
	private static final String test_turn = "Test Turn";
	private SendableChooser defenseChooser;

	private static final String[] defenses = { other, lowBar, portcullis, chillyFries, test, test_turn };
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
		driveTrain = new DriveTrain(new CANTalon(8), new CANTalon(1), new CANTalon(2), new CANTalon(3));
		// Intake, Switch1, Switch2, Shooter1, Shooter2
		shooter = new Shooter(new CANTalon(6), new DigitalInput(9), new DigitalInput(8), new CANTalon(4),
				new CANTalon(5));
		// Arm, ArmAngle
		lowArm = new LowArm(new CANTalon(7), new AnalogInput(1));
		// Scaler
		scaler = new Scaler(new CANTalon(0), new Servo(0));

		systems = new IterativeSystem[] { shooter, lowArm };

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
	}

	private void drive(double distance, double max_speed, AutonomousTimer timer) {
		//distance in feet
//		SmartDashboard.putNumber("acceleration", .75);
//		SmartDashboard.putNumber("max speed", .75);
//		SmartDashboard.putNumber("distance", .75);
//		SmartDashboard.putNumber("friction", .05);
		double friction = 0; //SmartDashboard.getNumber("friction", .05);
		double acceleration = 0.75; //SmartDashboard.getNumber("acceleration", .75);
//		max_speed = 0.5; //SmartDashboard.getNumber("max speed", .75); // max_speed must be less than 1 - friction
		//distance = SmartDashboard.getNumber("distance", .75);
		//distance /= (12 + 7/12); // comment out this line before running test, but leave it in if running low bar
		  double target_distance = distance;
		  double a_d_distance_units = 1/3;
		  double a_d_distance = 1 + 7/12; // actual acceleration distance + actual deceleration distance // line 1 of test //in feet
		  double unit_distance = 12 + 3.5/12 - a_d_distance; // distance robot travels when driving 1 at maximum speed //line 2 of test - a_d_distance // feet/unit 
		 distance = ((target_distance - a_d_distance)/unit_distance) + a_d_distance_units;
		System.out.println(friction);
		System.out.println(acceleration);
		System.out.println(max_speed);
		System.out.println(distance);
		double k = 1; // ft/unit convert between distance robot travels in 1
						// second at highest speed and feet
		double time_to_reach_max_speed = max_speed / acceleration;
		double d1 = (max_speed * max_speed) / (2 * acceleration);
		
		if (d1 > distance / 2) {
			System.out.println("ERROR: d1 > distance / 2");
			// acceleration_distance = target_distance / 2;
			// max_speed = Math.sqrt(target_distance * acceleration);
			// time_to_reach_max_speed = max_speed / acceleration;
			// SmartDashboard.putNumber("new max_speed", max_speed);
		}
		if (max_speed > 1 - friction){
			System.out.println("ERROR: max_speed > 1 - friction");
		}

		double velocity;
		double total_time = (2 * time_to_reach_max_speed) + ((distance - (2 * d1)) / max_speed);
		// double time;
		long time = System.currentTimeMillis();
		double diff;
		

		while ((diff = (System.currentTimeMillis() - time) / 1000.0) < total_time) {
			if (diff < time_to_reach_max_speed) {
				velocity = acceleration * diff;
			} else if (diff > time_to_reach_max_speed && diff < total_time - time_to_reach_max_speed) {
				velocity = max_speed;
			} else {
				velocity = acceleration * (total_time - diff);
			}
			driveTrain.drive((velocity + friction) * k);

			SmartDashboard.putNumber("velocity", velocity);
			SmartDashboard.putNumber("time", diff);

//			timer.delay(5);
		}
		driveTrain.drive(0);

	};
	
	private void turn(double radians, double direction, AutonomousTimer timer) { //direction = 1: turn left //direction = -1: turn right
//currently set up for low bar... Do NOT change anything about this
		//		SmartDashboard.putNumber("acceleration", .75);
//		SmartDashboard.putNumber("max speed", .75);
//		SmartDashboard.putNumber("distance", .75);
//		SmartDashboard.putNumber("robot_radius", 2);
//		SmartDashboard.putNumber("turn_friction", .05);
//		double robot_radius = SmartDashboard.getNumber("robot_radius", 0.5);
		double robot_radius = 0.5;
		double distance = robot_radius * radians;
		double turn_friction = SmartDashboard.getNumber("turn friction", .05);
		double acceleration = SmartDashboard.getNumber("turn acceleration", .75);
		double max_speed = SmartDashboard.getNumber("max turn speed", .75); // max_speed must be less than 1 - friction
		radians = SmartDashboard.getNumber("radians", Math.PI/2);
		System.out.println(robot_radius);
		System.out.println(turn_friction);
		System.out.println(acceleration);
		System.out.println(max_speed);
		System.out.println(radians);
		System.out.println(distance);
		double k = 1; // / (14 + 1/6); // ft/unit convert between distance robot travels in 1
						// second at highest speed and feet
		double time_to_reach_max_speed = max_speed / acceleration;
		double d1 = (max_speed * max_speed) / (2 * acceleration);
		
		if (d1 > distance / 2) {
			System.out.println("ERROR: d1 > distance / 2");
			// acceleration_distance = target_distance / 2;
			// max_speed = Math.sqrt(target_distance * acceleration);
			// time_to_reach_max_speed = max_speed / acceleration;
			// SmartDashboard.putNumber("new max_speed", max_speed);
		}
		if (max_speed > 1 - turn_friction){
			System.out.println("ERROR: max_speed > 1 - friction");
		}

		double velocity;
		double total_time = (2 * time_to_reach_max_speed) + ((distance - (2 * d1)) / max_speed);
		// double time;
		long time = System.currentTimeMillis();
		double diff;
		

		while ((diff = (System.currentTimeMillis() - time) / 1000.0) < total_time) {
			if (diff < time_to_reach_max_speed) {
				velocity = acceleration * diff;
			} else if (diff > time_to_reach_max_speed && diff < total_time - time_to_reach_max_speed) {
				velocity = max_speed;
			} else {
				velocity = acceleration * (total_time - diff);
			}
			driveTrain.tankDrive(direction * ((velocity + turn_friction) * k), -direction * ((velocity + turn_friction) * k));

			SmartDashboard.putNumber("velocity", velocity);
			SmartDashboard.putNumber("time", diff);

			timer.delay(5);
		}
		driveTrain.drive(0);

	};

	@Override
	public void autonomous() {
		start("Autonomous");

		String defense = (String) defenseChooser.getSelected();
		String position = (String) positionChooser.getSelected();

		System.out.println("Running  " + defense + " at position " + position);

		AutonomousSchedule schedule;

		switch (defense) {
		case chillyFries:
			schedule = new AutonomousSchedule(timer -> {
				driveTrain.drive(.3);
				timer.delay(1500);
				driveTrain.drive(0);
				lowArm.lower();
				timer.delay(1800);
				driveTrain.drive(.3);
				timer.delay(1200);
				driveTrain.drive(0);
			});
			break;
		case portcullis:
			schedule = new AutonomousSchedule(timer -> {
				lowArm.lower();
				timer.delay(1800);
				driveTrain.drive(.3);
				timer.delay(3000);
				driveTrain.drive(.1);
				lowArm.raise();
				timer.delay(1800);
				driveTrain.drive(.5);
				timer.delay(1200);
				driveTrain.drive(0);
				lowArm.lower();
			});
			break;
		case lowBar:
			schedule = new AutonomousSchedule(timer -> {
				/*
				lowArm.lower();
				timer.delay(1800);
				//drive((20 - 1/6), 0.5, timer);
				drive((20 + 1/6), 0.5, timer);
				turn(Math.PI/24, -1, timer); //pi/12 == 90 degrees?? // change to radians/=2 in turn function
				*/
				shooter.shoot(5 * 12);
				//drive((6 + 7/12), 0.5, timer);
				drive((5), 0.5, timer); /*
				lowArm.raise();
				timer.delay(800);
				lowArm.stop();
				//drive(1, 0.5, timer);
				drive(1 + 7/12, 0.5, timer);
				//shooter.shoot(5 * 12); */
				shooter.launch();
				//shooter.launching = true;
				/*lowArm.lower();
				timer.delay(1800);
				driveTrain.drive(.3);
				timer.delay(4200);
				driveTrain.drive(0);*/
			});
			break;
		case other:
			schedule = new AutonomousSchedule(timer -> {
				driveTrain.drive(.85);
				timer.delay(2000);
				driveTrain.drive(0);
			});
			break;
		case test:
			schedule = new AutonomousSchedule(timer -> {
				//drive(1/3, 0.5, timer);
				drive((1 + 1/3), 0.5, timer);
			});
			break;
		case test_turn:
			schedule = new AutonomousSchedule(timer -> {
				//lowArm.lower();
				//timer.delay(1800);
				drive((20 - 1/6), 0.5, timer);
				turn(Math.PI/24, -1, timer); //pi/12 == 90 degrees?? // change to radians/=2 in turn function
				drive((6 + 7/12), 0.5, timer);
			});
			break;
		default:
			return;
		}

		schedule.execute(5, systems);
	}

	@Override
	public void operatorControl() {
		start("Teleoperation");
		ControlState previous, current = controls.update();
		while (isOperatorControl() && isEnabled()) {
			previous = current;
			current = controls.update();

			tick(previous, current);

			for (IterativeSystem system : systems)
				system.update();

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

	private void tick(ControlState last, ControlState state) {
		driveTrain.arcadeDrive(state.getStick(0), reverseDriving);
		if (enableScale)
			scaler.move(state.getStick(1));

		if (state.isPressed(InputButton.Raise_Arm))
			lowArm.raise();
		else if (state.isPressed(InputButton.Lower_Arm))
			lowArm.lower();
		else
			lowArm.stop();

		if (state.isNewlyPressed(InputButton.Reverse_Steering, last))
			reverseDriving = !reverseDriving;

		if (state.isNewlyPressed(InputButton.Intake, last))
			shooter.toggleIntake();

		if (state.isNewlyPressed(InputButton.Shoot_Max, last))
			shooter.shootRaw(1);

		if (state.isNewlyPressed(InputButton.Shoot_5, last))
			shooter.shoot(5 * 12);

		if (state.isNewlyPressed(InputButton.Shoot_10, last))
			shooter.shoot(10 * 12);

		if (state.isNewlyPressed(InputButton.Shoot_Low, last))
			shooter.shootLow();

		if (state.isNewlyPressed(InputButton.Toggle_Scaler, last))
			enableScale = !enableScale;

		if (state.isNewlyPressed(InputButton.Lock_Scaler, last))
			scaler.lock();

		if (state.isNewlyPressed(InputButton.Unlock_Scaler, last))
			scaler.unlock();
		
		if (state.isNewlyPressed(InputButton.Shoot_Finish, last))
			shooter.launch();
	}

	private static void start(String mode) {
		System.out.println("#    " + mode + "    #");
	}
}
