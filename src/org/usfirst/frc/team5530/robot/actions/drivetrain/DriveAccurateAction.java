package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class DriveAccurateAction extends Action {
	private static final int allowableError = 100;
	private static final double speedLimit = 0.3;     // power
	private static final double acceleration = 0.5;   // power per second
	private static final double deceleration = .0003; // power per encoder tick
	
	private Talon left;
	private Talon right;
	
	private int distance;
	
	private long startTime;
	
	public DriveAccurateAction(double inches) {
		super("Drive accurately " + inches + " inches");
		this.distance = (int) (DriveTrainSystem.ticksPerInch * inches);
	}
	
	@Override
	protected void init(ResourceScope scope) {
		left = scope.require(DriveTrainSystem.left);
		right = scope.require(DriveTrainSystem.right);
		
	}
	
	private boolean update(Talon talon, boolean inverted) {
		long millis = System.currentTimeMillis() - startTime;
		double rampUp = (millis / 1000.0) * acceleration;
		
		int factor = inverted ? -1 : 1;
		int error = Math.abs(factor * talon.getEncoderPosition() - distance);
		
		if (error < allowableError) {
			talon.set(0);
			return true;
		}
		
		double rampDown = error * deceleration;
		double value = Util.min(rampUp, speedLimit, rampDown);
		
		double invert = talon.getEncoderPosition() > distance ? -1 : 1;
		
		talon.set(invert * factor * value);
		
		System.out.println(inverted + ": " + error);
		return false;
	}
	
	@Override
	protected void before() {
		left.control(ControlMode.POWER);
		right.control(ControlMode.POWER);
		
		left.setEncoderPosition(0);
		right.setEncoderPosition(0);
		
		startTime = System.currentTimeMillis();
	}
	
	@Override
	protected void update() {
		boolean done = true;
		
		done &= update(left, true);
		done &= update(right, false);
		
		if (done) {
			this.complete();
		}
	}
	
	@Override
	protected void abort() {
		left.set(0);
		right.set(0);
	}
}
