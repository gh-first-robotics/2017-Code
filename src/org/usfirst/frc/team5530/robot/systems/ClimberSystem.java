package org.usfirst.frc.team5530.robot.systems;

import me.mfroehlich.frc.abstractions.Servo;
import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.eventloop.actions.Resource;

public class ClimberSystem {
	private static Talon motorValue = Talon.create(7);
	private static Servo topGripperValue = Servo.create(3);
	private static Servo bottomGripperValue = Servo.create(2);
	
	public static final Resource<Talon> motor = new Resource<>(motorValue);
	public static final Resource<Servo> topGripper = new Resource<>(topGripperValue);
	public static final Resource<Servo> bottomGripper = new Resource<>(bottomGripperValue);
}
