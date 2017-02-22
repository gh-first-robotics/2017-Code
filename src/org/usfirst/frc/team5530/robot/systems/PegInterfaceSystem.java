package org.usfirst.frc.team5530.robot.systems;

import me.mfroehlich.frc.abstractions.DigitalSensor;
import me.mfroehlich.frc.abstractions.Servo;
import me.mfroehlich.frc.eventloop.actions.Resource;

public class PegInterfaceSystem {
	private static Servo gripperValue = Servo.create(1);
	
	public static final DigitalSensor limitSwitch = DigitalSensor.create(2);
	public static final DigitalSensor breakBeam = DigitalSensor.create(7);
	
	public static final Resource<Servo> gripper = new Resource<>(gripperValue);
}
