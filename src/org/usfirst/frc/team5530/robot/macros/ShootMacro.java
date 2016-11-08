package org.usfirst.frc.team5530.robot.macros;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.system.Shooter;

public class ShootMacro implements Macro {
	private double distance;
	
	/**
	 * Constructs a new macro that will cause the robot to shoot
	 * 
	 * @param distance
	 *            the distance to shoot in inches
	 */
	public ShootMacro(double distance) {
		this.distance = distance;
	}

	@Override
	public void run(Robot rob) {
		Shooter shooter = rob.getSystem(Shooter.class);
		
		double root = Math.sqrt(2 * gravity * (distance * Math.tan(launch_angle) - target_height));
		double speed = (distance * gravity) / (Math.cos(launch_angle) * root);
		double rps = speed / (2 * Math.PI * wheel_radius);
		
		double ratio = (rps * magic_number) / maximum_rps;
		
		shooter.shootRaw(ratio);
	}

	private static final double gravity = 32 * 12, // ft/s^2//in
			// maximum speed for shooter wheels
			maximum_rps = 16000 / 180,
			// angle of launch above horizontal
			launch_angle = Math.PI / 3,
			// magic multiplier
			magic_number = 2.62,
			// radius of wheels
			wheel_radius = 2,
			// height of target above ground
			target_height = 7 * 12 + 1; // -robot_height
}
