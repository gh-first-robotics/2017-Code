package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.PIDController;
import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class TurnAction2 extends Action {
	private Talon left;
	private Talon right;
	
	private double targetAngle;
	private double rate;
	
	private PIDController pid;
	
	public TurnAction2(double angle) {
		super("Turn to " + angle + " degree");
		targetAngle = DriveTrainSystem.gyro.getAngle() + angle;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		left = scope.require(DriveTrainSystem.left);
		right = scope.require(DriveTrainSystem.right);
		
		pid = new PIDController(0.03, 0, 0, (AHRS) DriveTrainSystem.gyro.getAHRS(), v -> this.rate = v);
		pid.setInputRange(-180, 180);
		pid.setOutputRange(-1, 1);
		pid.setAbsoluteTolerance(4);
		pid.setContinuous(true);
	}
	
	@Override
	protected void before() {
//		left.control(ControlMode.SPEED);
//		right.control(ControlMode.SPEED);
				
		pid.reset();
	}

	@Override
	protected void update() {
		System.out.println(DriveTrainSystem.gyro.getAngle() + " " + targetAngle);
		
		if (pid.onTarget()) {
			left.set(0);
			right.set(0);
			this.complete();
		} else {
			left.set(rate);
			right.set(rate);
		}
	}
	
	@Override
	protected void after() {
		left.set(0);
		right.set(0);
		pid.disable();
	}

	@Override
	protected void abort() {
		left.set(0);
		right.set(0);
	}
}
