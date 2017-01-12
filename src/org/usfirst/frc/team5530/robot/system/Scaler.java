package org.usfirst.frc.team5530.robot.system;

import org.usfirst.frc.team5530.robot.teleop.Vector2;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Servo;

public class Scaler implements RobotSystem {
	private CANTalon talon;
	private Servo lock;

	public Scaler(CANTalon talon, Servo lock) {
		this.talon = talon;
		this.lock = lock;
	}

	/**
	 * Sets the speed of the scaler extensions based on a joystick
	 * 
	 * @param stick
	 *            the stick to control the scaler
	 */
	public void move(Vector2 stick) {
		talon.set(stick.y);
	}

	/**
	 * Locks the scaler
	 */
	public void lock() {
		lock.setAngle(180);
	}

	/**
	 * Unlocks the scaler
	 */
	public void unlock() {
		lock.setAngle(-15);
	}

	public void update() {}
}
