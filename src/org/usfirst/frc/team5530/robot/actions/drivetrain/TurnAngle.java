package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import edu.wpi.first.wpilibj.PIDController;
import edu.wpi.first.wpilibj.PIDOutput;
import me.mfroehlich.frc.abstractions.Gyro;
import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class TurnAngle extends Action implements PIDOutput {
	private Talon left;
	private Talon right;
	
	PIDController turnController;
	double rotateToAngleRate;
	
	static final double kP = 0.03;
	static final double kI = 0.00;
	static final double kD = 0.00;
	static final double kF = 0.00;
	
	static final double kToleranceDegrees = 4.0f;
	
	double targetAngle;
	
	
	public TurnAngle(double angle) {
		turnController.setSetpoint(angle);
		targetAngle = angle;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		left = scope.require(DriveTrainSystem.left);
		right = scope.require(DriveTrainSystem.right);
		
		turnController = new PIDController(kP, kI, kD, kF, DriveTrainSystem.gyro.getAHRS(), this);
	    turnController.setInputRange(-180.0f,  180.0f);
	    turnController.setOutputRange(-1.0, 1.0);
	    turnController.setAbsoluteTolerance(kToleranceDegrees);
	    turnController.setContinuous(true);
	}
	
	@Override
	protected void before() {
		left.control(ControlMode.SPEED);
		right.control(ControlMode.SPEED);
		turnController.enable();
	}

	@Override
	public void update() {
		double left = rotateToAngleRate;
		double right = -rotateToAngleRate;
		
		left = Util.clamp(left, -1, 1);
		right = Util.clamp(right, -1, 1);
		
		this.left.set(-left);
		this.right.set(right);
		
		if(Math.abs(DriveTrainSystem.gyro.getAngle()-targetAngle) <= kToleranceDegrees){
			this.left.set(0);
			this.right.set(0);
			turnController.disable();
			complete();
		}
	}
	
	@Override
	protected void abort() {
		left.set(0);
		right.set(0);
		turnController.disable();
	}

	@Override
	public void pidWrite(double output) {
		// TODO Auto-generated method stub
		rotateToAngleRate = output;
	}
}
