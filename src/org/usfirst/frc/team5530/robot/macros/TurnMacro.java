package org.usfirst.frc.team5530.robot.macros;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.system.DriveTrain;

public class TurnMacro implements Macro {
	/** The height of the trapezoid */
	private static double speedLimit = .25;
	/** The max speed in feet/seconds of the robot */
	private static double maxSpeed = 12;

	private double angle;

	/**
	 * Construct a macro that will turn the robot. Clockwise is positive
	 * 
	 * @param degrees
	 *            the angle to turn the robot in degrees
	 */
	public TurnMacro(double degrees) {
		this.angle = (degrees / 180) * Math.PI;
	}

	@Override
	public void run(Robot rob) {
		DriveTrain drive = rob.getSystem(DriveTrain.class);
		
		int direction = 1;
		if (angle < 0) {
			angle = -angle;
			direction = -1;
		}
		
		double radius = 0.5;
		double distance = radius * this.angle;
		double acceleration = 0.75;

		double triangle_time = speedLimit / acceleration;
		double triangle_distance = triangle_time * speedLimit * maxSpeed * 0.5;

		double rectangle_distance = distance - (triangle_distance * 2);
		double rectangle_time = rectangle_distance / (speedLimit * maxSpeed);
		double total_time = triangle_time * 2 + rectangle_time;
		
		double speed;

		long diff, start = System.currentTimeMillis();
		while ((diff = System.currentTimeMillis() - start) < triangle_time * 1000) {
			speed = direction * acceleration * diff;
			drive.tankDrive(speed, -speed);
			rob.sleep(5);
		}

		speed = direction * speedLimit;
		drive.tankDrive(speed, -speed);
		rob.sleep((int) (rectangle_time * 1000));

		while ((diff = System.currentTimeMillis() - start) < total_time) {
			speed = direction * acceleration * (total_time - diff);
			drive.tankDrive(speed, -speed);
			rob.sleep(5);
		}
	}
	/* // direction
																					// =
																					// 1:
																					// turn
																					// left
																					// //direction
																					// =
																					// -1:
																					// turn
																					// right
		// currently set up for low bar... Do NOT change anything about this
		// SmartDashboard.putNumber("acceleration", .75);
		// SmartDashboard.putNumber("max speed", .75);
		// SmartDashboard.putNumber("distance", .75);
		// SmartDashboard.putNumber("robot_radius", 2);
		// SmartDashboard.putNumber("turn_friction", .05);
		// double robot_radius = SmartDashboard.getNumber("robot_radius", 0.5);

		double robot_radius = 0.5;
		double distance = robot_radius * radians;
		double turn_friction = SmartDashboard.getNumber("turn friction", 0);
		double acceleration = SmartDashboard.getNumber("turn acceleration", .75);
		double max_speed = SmartDashboard.getNumber("max turn speed", .75); // max_speed
																			// must
																			// be
																			// less
																			// than
																			// 1
																			// -
																			// friction
		// radians = SmartDashboard.getNumber("radians", Math.PI/2);
		System.out.println(robot_radius);
		System.out.println(turn_friction);
		System.out.println(acceleration);
		System.out.println(max_speed);
		System.out.println(radians);
		System.out.println(distance);
		double k = 1; // / (14 + 1/6); // ft/unit convert between distance robot
						// travels in 1
						// second at highest speed and feet
		double time_to_reach_max_speed = max_speed / acceleration;
		double d1 = (max_speed * max_speed) / (2 * acceleration);

		if (d1 > distance / 2) {
			System.out.println("ERROR: d1 > distance / 2 turn");
			// acceleration_distance = target_distance / 2;
			// max_speed = Math.sqrt(target_distance * acceleration);
			// time_to_reach_max_speed = max_speed / acceleration;
			// SmartDashboard.putNumber("new max_speed", max_speed);
		}
		if (max_speed > 1 - turn_friction) {
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
		driveTrain.drive(0);*/
}
