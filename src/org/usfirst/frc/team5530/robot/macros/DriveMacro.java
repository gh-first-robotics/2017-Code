package org.usfirst.frc.team5530.robot.macros;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.system.DriveTrain;

public class DriveMacro implements Macro {
	/** The height of the trapezoid */
	private static double speedLimit = .5;
	/** The max speed in feet/seconds of the robot */
	private static double maxSpeed = 12;

	private double distance;

	/**
	 * Construct a macro that will drive the robot a certain distance. A negative distance will drive backwards.
	 * 
	 * @param distance
	 *            the distance to drive the robot in feet
	 */
	public DriveMacro(double distance) {
		this.distance = distance;
	}

	@Override
	public void run(Robot rob) {
		DriveTrain drive = rob.getSystem(DriveTrain.class);
		
		int direction = 1;
		if (distance < 0) {
			distance = -distance;
			direction = -1;
		}

		double acceleration = 0.75;
		double triangle_time = speedLimit / acceleration;
		double triangle_distance = triangle_time * speedLimit * maxSpeed * 0.5;

		double rectangle_distance = distance - (triangle_distance * 2);
		double rectangle_time = rectangle_distance / (speedLimit * maxSpeed);
		double total_time = triangle_time * 2 + rectangle_time;

		long diff, start = System.currentTimeMillis();
		while ((diff = System.currentTimeMillis() - start) < triangle_time * 1000) {
			drive.drive(direction * acceleration * diff);
			rob.sleep(5);
		}

		drive.drive(direction * speedLimit);
		rob.sleep((int) (rectangle_time * 1000));

		while ((diff = System.currentTimeMillis() - start) < total_time) {
			drive.drive(direction * acceleration * (total_time - diff));
			rob.sleep(5);
		}
	}
}
