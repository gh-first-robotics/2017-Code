package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class DriveDistance2Action extends Action {
	private Talon left;
	private Talon right;
	
	private int distance;
	
	public DriveDistance2Action(int encoderTicks) {
		super("Drive2 " + encoderTicks + " encoder ticks?");
		this.distance = encoderTicks;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		this.left = scope.require(DriveTrainSystem.left);
		this.right = scope.require(DriveTrainSystem.right);
	}
	
	@Override
	protected void before() {
		left.control(ControlMode.POSITION);
		right.control(ControlMode.POSITION);
		
		left.setEncoderPosition(0);
		right.setEncoderPosition(0);
		
		left.set(distance);
		right.set(distance);
	}

	@Override
	protected void update() {
		int diffL = distance - this.left.getEncoderPosition();
		int diffR = distance - this.right.getEncoderPosition();
		
		System.out.println(this.distance + " " + this.left.getEncoderPosition() + " " + this.right.getEncoderPosition());
		System.out.println(diffL + " " + diffR);
		
//		if (diffL < 100 || diffR < 100) {
//			this.complete();
//		}
	}

	@Override
	protected void abort() {
		left.control(ControlMode.POWER);
		right.control(ControlMode.POWER);
		
		left.set(0);
		right.set(0);
		// TODO Auto-generated method stub
		
	}
}
