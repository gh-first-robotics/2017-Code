package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class DriveVector extends Action {
	private Talon left;
	private Talon right;
	
	Vector2 vector;
	
	public DriveVector(Vector2 vector) {
		this.vector = vector;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		
	}
	
	@Override
	protected void before() {
		Action.inSequence(
				new TurnToAngleAction(vector.direction),
				new DriveDistanceAction(vector.magnitude)
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
