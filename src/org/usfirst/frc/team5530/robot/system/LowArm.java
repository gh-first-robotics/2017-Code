package org.usfirst.frc.team5530.robot.system;

import edu.wpi.first.wpilibj.AnalogInput;
import edu.wpi.first.wpilibj.AnalogPotentiometer;
import com.ctre.CANTalon;

public class LowArm implements RobotSystem {
	private CANTalon talon;
	private AnalogPotentiometer potentiometer;
	private int control;

	public LowArm(CANTalon talon, AnalogInput input) {
		this.talon = talon;
		potentiometer = new AnalogPotentiometer(input, 360, 30);
	}

	/**
	 * Sets the arm to raise
	 */
	public void raise() {
		this.control = 1;
	}
	
	/**
	 * Sets the arm to lower
	 */
	public void lower() {
		this.control = -1;
	}
	
	/**
	 * Stops the arm's motion
	 */
	public void stop() {
		this.control = 0;
	}

	/**
	 * Called repeatedly to set the speed of the motor based on the current option
	 */
	@Override
	public void update() {
		double angle = potentiometer.get();

		double speed = 0;
		if (control > 0 && angle < 294)
			speed = -.65;
		else if (control < 0 && angle > 52)
			speed = .65;
		else
			stop();

		talon.set(speed);
	}
}
