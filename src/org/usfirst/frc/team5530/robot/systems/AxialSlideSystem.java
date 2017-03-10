package org.usfirst.frc.team5530.robot.systems;

import me.mfroehlich.frc.abstractions.DigitalSensor;
import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.EncoderType;
import me.mfroehlich.frc.actionloop.actions.Resource;

public class AxialSlideSystem {
	public static final double ticksPerInch = 560.992955437;
	public static final int maximumTicks = (int) (8.5 * ticksPerInch);
	
	private static Talon motorValue = Talon.create(6, 1, EncoderType.QUADRATURE);
	
	public static boolean isCalibrated = false;
	public static final DigitalSensor homeSwitch = DigitalSensor.create(1);
	
	public static final Resource<Talon> motor = new Resource<>(motorValue);
}
