package org.usfirst.frc.team5530.robot.systems;

import me.mfroehlich.frc.abstractions.DigitalSensor;
import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.actionloop.actions.Resource;

public class LateralSlideSystem {
	public static final double ticksPerInch = 560.992955437,
							   maximumTicks = 7 * ticksPerInch;
	
	private static Talon motorValue = Talon.create(5);
	
	public static boolean isCalibrated = false;
	public static final DigitalSensor homeSwitch = DigitalSensor.create(0);
	
	public static final Resource<Talon> motor = new Resource<>(motorValue);
}
