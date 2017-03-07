package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class ManualDriveAction extends Action {
	private Talon left;
	private Talon right;
	
	private Vector2 value = new Vector2();
	private int multiplier = 1;
	
	public ManualDriveAction() {
		super("Manual drive");
	}
	
	public void control(Vector2 v) {
		this.value = v;
	}
	
	public void reverse() {
		multiplier = -multiplier;
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
	public void update() {
		double left = -value.y - value.x;
		double right = -value.y + value.x;
		
		left = multiplier * Util.clamp(left, -1, 1);
		right = multiplier * Util.clamp(right, -1, 1);
		
		this.left.set(-left);
		this.right.set(right);
		
//		System.out.println(this.left.getEncoderPosition() + " " + this.right.getEncoderPosition());
	}
	
	@Override
	protected void abort() {
		left.set(0);
		right.set(0);
	}
}
