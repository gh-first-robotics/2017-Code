package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class DriveDistanceAction extends Action {
	private static final double max_speed = 120;
	private static final double speed_limit = .4;
	private static final double acceleration = .5;
	
	private static final double
			rampTime = speed_limit / acceleration,
			rampDistance = rampTime * speed_limit * max_speed * .5;
	
	
	private Talon left;
	private Talon right;
	
	private double distance;
	
	private double totalTime;
	private double mainTime;
	private double mainDistance;
	
	
	private long startTime;
	
	public DriveDistanceAction(double distance) {
		this.distance = distance;
		
		mainDistance = this.distance - (2 * rampDistance);
		mainTime = mainDistance / (speed_limit * max_speed);
		
		totalTime = rampTime * 2 + mainTime;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		this.left = scope.require(DriveTrainSystem.left);
		this.right = scope.require(DriveTrainSystem.right);
	}
	
	@Override
	protected void before() {
		left.control(ControlMode.POWER);
		right.control(ControlMode.POWER);
		
		startTime = System.currentTimeMillis();
	}

	@Override
	protected void update() {
		double time = System.currentTimeMillis() - startTime;
		time = time / 1000;
		
		double speed;
		if (time < rampTime) {
			speed = acceleration * time;
		} else if (time < rampTime + mainTime) {
			speed = speed_limit;
		} else if (time < totalTime) {
			speed = acceleration * (totalTime - time);
		} else {
			speed = 0;
			this.complete();
		}
		
		left.set(-speed);
		right.set(speed);
		
		System.out.println(speed + " " + time + " " + rampTime + " " + totalTime);
		System.out.println(this.left.getEncoderPosition() + " " + this.right.getEncoderPosition());
	}

	@Override
	protected void abort() {
		left.set(0);
		right.set(0);
	}
}
