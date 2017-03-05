package org.usfirst.frc.team5530.robot.actions.auton;

import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveDistanceAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveToLocation;
import org.usfirst.frc.team5530.robot.actions.gears.UnloadGear;
import org.usfirst.frc.team5530.robot.systems.GearChuteSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;
import org.usfirst.frc.team5530.robot.test.WaitForDigitalSensorAction;
import me.mfroehlich.frc.actionloop.actions.SequentialActionSet;
import me.mfroehlich.frc.actionloop.actions.lib.DelayAction;

public class Right extends SequentialActionSet {
	
	Vector2 firstPoint = new Vector2(1,2);
	Vector2 placeGearAt = new Vector2(.5,3);
	double backUpFromGearPlacement = 1;
	Vector2 waypoint1 = new Vector2(1,5);
	Vector2 endAt = new Vector2(7,5);
	
	public Right() {
		add(new DriveToLocation(firstPoint));
		add(new DriveToLocation(placeGearAt));
		add(new UnloadGear());
		add(new WaitForDigitalSensorAction(GearChuteSystem.gearSensor, false));
		add(new DelayAction(2000));
		add(new DriveDistanceAction(-1*backUpFromGearPlacement));
		add(new DriveToLocation(waypoint1));
		add(new DriveToLocation(endAt));	
	}
}
