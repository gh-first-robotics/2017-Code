package org.usfirst.frc.team5530.robot.system;

import org.usfirst.frc.team5530.robot.teleop.Operator;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import com.ctre.CANTalon;
import edu.wpi.first.wpilibj.Servo;

public class Scaler implements RobotSystem {
	private CANTalon talon;
	private Servo top;
	private Servo bottom;
	double  speed = .3,
			topClosedAngle = 0,
			bottomClosedAngle = 0,
			topOpenAngle = 90,
			bottomOpenAngle = 90;

	public Scaler(CANTalon talon, Servo top, Servo bottom) {
		this.talon = talon;
		this.top = top;
		this.bottom = bottom;
	}

	/**
	 * Sets the speed of the scaler extensions based on a joystick
	 * 
	 * @param stick
	 *            the stick to control the scaler
	 */
	
	

	/**
	 * Locks the scaler
	 */
	public void close() {
		top.setAngle(topClosedAngle);
		bottom.setAngle(bottomClosedAngle);
	}

	/**
	 * Unlocks the scaler
	 */
	public void openBottom() {
		bottom.setAngle(bottomOpenAngle);
	}
	
	public void openAll() {
		bottom.setAngle(bottomOpenAngle);
		top.setAngle(topOpenAngle);
	}

	public void update() {
		if(Operator.moveScaler)
			talon.set(speed);
		
	}
}
