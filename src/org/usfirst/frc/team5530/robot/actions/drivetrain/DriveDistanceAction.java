package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class DriveDistanceAction extends Action {
	private Talon left;
	private Talon right;
	
	private double distance;
	double k = (40 * 5/3) / (2 * Math.PI *2); //copied from old code
	double error = 5;
	double leftTarget = 0,
			rightTarget = 0;
	
	public DriveDistanceAction(double distance) {
		this.distance = k * distance;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		left = scope.require(DriveTrainSystem.left);
		right = scope.require(DriveTrainSystem.right);
	}
	
	@Override
	protected void before() {
		left.control(ControlMode.POSITION);
		right.control(ControlMode.POSITION);
		
		leftTarget = -distance + left.getPosition();
		left.set(leftTarget);
		
		rightTarget = distance + right.getPosition();
		right.set(rightTarget);
	}

	@Override
	public void update() {
		if (Math.abs(left.getPosition()-leftTarget)<error && (Math.abs(right.getPosition()-rightTarget)<error)){
			left.control(ControlMode.POWER);
			right.control(ControlMode.POWER);
			left.set(0);
			right.set(0);
			complete();
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
