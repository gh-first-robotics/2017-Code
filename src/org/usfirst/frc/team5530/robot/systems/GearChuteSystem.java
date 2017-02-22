package org.usfirst.frc.team5530.robot.systems;

import me.mfroehlich.frc.abstractions.DigitalSensor;
import me.mfroehlich.frc.abstractions.Servo;
import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.actionloop.actions.Resource;

public class GearChuteSystem {
	private static Talon rotatorValue = Talon.create(4);
	private static Servo panelValue = Servo.create(4);
	
	public static final DigitalSensor leftSensor = DigitalSensor.create(3);
	public static final DigitalSensor middleSensor = DigitalSensor.create(4);
	public static final DigitalSensor rightSensor = DigitalSensor.create(5);
	
	public static final DigitalSensor gearSensor = DigitalSensor.create(6);
	
	public static final Resource<Talon> rotator = new Resource<>(rotatorValue);
	public static final Resource<Servo> panel = new Resource<>(panelValue);
}
