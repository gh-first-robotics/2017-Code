package org.usfirst.frc.team5530.robot.systems;

import me.mfroehlich.frc.abstractions.Gyro;
import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.EncoderType;
import me.mfroehlich.frc.actionloop.actions.Resource;
import me.mfroehlich.frc.actionloop.test.PositionTracker;

public class DriveTrainSystem {
	// 1024 / 4PI   (ticks/rev) / (inches/rev)
	public static final double ticksPerInch = 81.4873308631;
	
	private static Talon leftValue = Talon.create(1, 1024, EncoderType.QUADRATURE);
	private static Talon left2 = Talon.create(0);
	
	private static Talon rightValue = Talon.create(2, 1024, EncoderType.QUADRATURE);
	private static Talon right2 = Talon.create(3);
	
	public static Gyro gyro = Gyro.create();
	public static PositionTracker position = new PositionTracker(leftValue, rightValue);
	
	public static final Resource<Talon> left = new Resource<>(leftValue);
	public static final Resource<Talon> right = new Resource<>(rightValue);
	
	static {
		left2.follow(leftValue);
		right2.follow(rightValue);
	}
}
