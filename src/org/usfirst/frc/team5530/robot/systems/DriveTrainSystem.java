package org.usfirst.frc.team5530.robot.systems;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Gyro;
import me.mfroehlich.frc.actionloop.actions.Resource;

public class DriveTrainSystem {
	private static Talon leftValue = Talon.create(1);
	private static Talon left2 = Talon.create(0);
	
	private static Talon rightValue = Talon.create(2);
	private static Talon right2 = Talon.create(3);
	
	public static Gyro gyro = Gyro.create();
	
	public static final Resource<Talon> left = new Resource<>(leftValue);
	public static final Resource<Talon> right = new Resource<>(rightValue);
	
	static {
		left2.follow(leftValue);
		right2.follow(rightValue);
	}
}
