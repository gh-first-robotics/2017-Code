package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class DriveDistance2Action extends Action {
	private Talon left;
	private Talon right;
	
	private double distance;
	private double speed;
	double k = 1;
	double error = 5;
	double leftTarget = 0,
			rightTarget = 0;
	
	public DriveDistance2Action(double distance, double speed) {
		//distance and speed must have the same sign
		this.distance = k * distance;
		this.speed = k * speed;
		leftTarget = distance + left.getEncoderPosition();
		rightTarget = distance + right.getEncoderPosition();
	}
	
	@Override
	protected void init(ResourceScope scope) {
		left = scope.require(DriveTrainSystem.left);
		right = scope.require(DriveTrainSystem.right);
	}
	
	@Override
	protected void before() {
		left.control(ControlMode.SPEED);
		right.control(ControlMode.SPEED);
	}

	@Override
	public void update() {
		if (Math.abs(left.getEncoderPosition()-leftTarget)<error && (Math.abs(right.getEncoderPosition()-rightTarget)<error)){
			left.set(0);
			right.set(0);
			complete();
		}
		else{
			left.set(speed);
			right.set(speed);
		}
	}
	
	@Override
	protected void abort() {
		left.control(ControlMode.POWER);
		right.control(ControlMode.POWER);
		left.set(0);
		right.set(0);
	}
}
