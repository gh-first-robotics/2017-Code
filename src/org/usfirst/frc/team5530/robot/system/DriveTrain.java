package org.usfirst.frc.team5530.robot.system;

import org.usfirst.frc.team5530.robot.Vector2;

import edu.wpi.first.wpilibj.CANTalon;

public class DriveTrain {
	private CANTalon[] talons;

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
}
