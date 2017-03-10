package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class DriveAccurateAction extends Action {
	private static final int allowableError = 100;
	private static final double speedLimit = 500;     // rpm?
	private static final double acceleration = 500;   // rpm? per second
	private static final double deceleration = .5; // rpm? per encoder tick
	
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
		
		int sign = inverted ? -1 : 1;
		int error = sign * talon.getEncoderPosition() - distance;
		
		if (Math.abs(error) < allowableError) {
			talon.set(0);
			return true;
		}
		
		double rampDown = Math.abs(error) * deceleration;
		double value = Util.min(rampUp, speedLimit, rampDown);
		
		talon.set(sign * value);
		
		return false;
	}
	
	@Override
	protected void before() {
		left.control(ControlMode.SPEED);
		right.control(ControlMode.SPEED);
		
		left.setEncoderPosition(0);
		right.setEncoderPosition(0);
		
		startTime = System.currentTimeMillis();
	}
	
	@Override
	protected void update() {
		boolean done = true;
		
		done &= update(left, true);
		done &= update(right, false);
		
		System.out.println((-left.getEncoderPosition() - distance) + ", " + (right.getEncoderPosition() - distance));
		
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
