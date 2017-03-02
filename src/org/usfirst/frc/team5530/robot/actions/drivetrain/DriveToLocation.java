package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.FindLocation;
import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class DriveToLocation extends Action {
	private Talon left;
	private Talon right;
	
	Vector2 vector;
	
	public DriveToLocation(Vector2 target) {
		this.vector = target.subtract(FindLocation.location);
	}
	
	@Override
	protected void init(ResourceScope scope) {
		
	}
	
	@Override
	protected void before() {
		Action.inSequence(
				new DriveVector(vector)
				);
	}

	@Override
	public void update() {
		
			complete();	
	}
	
	@Override
	protected void abort() {
		left.control(ControlMode.POWER);
		right.control(ControlMode.POWER);
		left.set(0);
		right.set(0);
	}
}
