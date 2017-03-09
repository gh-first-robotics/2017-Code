package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class TurnAction extends Action {
	private Talon left;
	private Talon right;
	
	private double targetAngle;
	
	public TurnAction(double angle) {
		super("Turn to " + angle + " degree");
		targetAngle = DriveTrainSystem.gyro.getAngle() + angle;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		left = scope.require(DriveTrainSystem.left);
		right = scope.require(DriveTrainSystem.right);
	}
	
	@Override
	protected void before() {
		left.control(ControlMode.POWER);
		right.control(ControlMode.POWER);
	}

	@Override
	protected void update() {
		System.out.println(DriveTrainSystem.gyro.getAngle() + " " + targetAngle);
		
		double error = targetAngle - DriveTrainSystem.gyro.getAngle();
		
		if (Math.abs(error) < 5) {
			left.set(0);
			right.set(0);
			this.complete();
			return;
		}
		
		double rampDown = Math.abs(error * .02);
		double maxSpeed = .5;
		
		double speed = Util.min(maxSpeed, rampDown) * Math.signum(error);
		
		left.set(speed);
		right.set(speed);
	}
	
	@Override
	protected void after() {
		left.set(0);
		right.set(0);
	}

	@Override
	protected void abort() {
		left.set(0);
		right.set(0);
	}
}
