package org.usfirst.frc.team5530.robot.actions.auton;

import org.usfirst.frc.team5530.robot.FindLocation;
import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveDistanceAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveToLocation;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveVector;
import org.usfirst.frc.team5530.robot.actions.gears.UnloadGear;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.systems.GearChuteSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;
import org.usfirst.frc.team5530.robot.test.WaitForDigitalSensorAction;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;
import me.mfroehlich.frc.actionloop.actions.lib.DelayAction;

public class Left extends Action {
	//114.25
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
				new WaitForDigitalSensorAction(GearChuteSystem.gearSensor, false),
				new DelayAction(2000),
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
		
	}
}
