package org.usfirst.frc.team5530.robot.actions.auton;

import org.usfirst.frc.team5530.robot.FindLocation;
import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveDistanceAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveToLocation;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveVector;
import org.usfirst.frc.team5530.robot.actions.gears.UnloadGear;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class Left extends Action {
	private Talon left;
	private Talon right;
	
	Vector2 firstPoint = new Vector2(-1,2);
	Vector2 placeGearAt = new Vector2(-.5,3);
	double backUpFromGearPlacement = 1;
	Vector2 waypoint1 = new Vector2(-1,5);
	Vector2 endAt = new Vector2(7,5);
	
	public Left() {
		
	}
	
	@Override
	protected void init(ResourceScope scope) {
		
	}
	
	@Override
	protected void before() {
		Action.inSequence(
				new DriveToLocation(firstPoint),
				new DriveToLocation(placeGearAt),
				new UnloadGear(),
				new DriveDistanceAction(-1*backUpFromGearPlacement),
				new DriveToLocation(waypoint1),
				new DriveToLocation(endAt)				
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
