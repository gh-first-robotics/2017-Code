package org.usfirst.frc.team5530.robot;

import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveDistanceAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveToLocation;
import org.usfirst.frc.team5530.robot.actions.drivetrain.TurnToAngleAction;
import org.usfirst.frc.team5530.robot.actions.gears.UnloadGear;
import org.usfirst.frc.team5530.robot.auton.AutonController;
import org.usfirst.frc.team5530.robot.systems.GearChuteSystem;
import org.usfirst.frc.team5530.robot.teleop.TeleopController;
import org.usfirst.frc.team5530.robot.teleop.Vector2;
import org.usfirst.frc.team5530.robot.test.TestController;
import org.usfirst.frc.team5530.robot.test.WaitForDigitalSensorAction;

import me.mfroehlich.frc.abstractions.RobotStateProvider;
import me.mfroehlich.frc.abstractions.RobotStateProvider.State;
import me.mfroehlich.frc.actionloop.ActionRobot;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.lib.DelayAction;

public class Robot extends ActionRobot {
	public Robot(RobotStateProvider provider) {
		super(provider);
	}

	@Override
	public void init() {
		setController(State.TEST, new TestController());
		setController(State.TELEOP, new TeleopController());
		setController(State.AUTONOMOUS, new AutonController());
	}
	
	public static Action DriveVector(Vector2 vector) {
		return Action.inSequence(
			new TurnToAngleAction(vector.direction),
			new DriveDistanceAction(vector.magnitude)
			);
	}
	public static Action DriveToLocation(Vector2 target) {
		Vector2 vector = target.subtract(FindLocation.location);
		return Action.inSequence(
			new TurnToAngleAction(vector.direction),
			new DriveDistanceAction(vector.magnitude)
			);
	}
	
	//Auton
		//Left
	public static Action Left(){
		//114.25
		Vector2 firstPoint = new Vector2(-1,2);
		Vector2 placeGearAt = new Vector2(-.5,3);
		double backUpFromGearPlacement = 1;
		Vector2 waypoint1 = new Vector2(-1,5);
		Vector2 endAt = new Vector2(7,5);
		return Action.inSequence(
				DriveToLocation(firstPoint),
				DriveToLocation(placeGearAt),
				new UnloadGear(),
				new WaitForDigitalSensorAction(GearChuteSystem.gearSensor, false),
				new DelayAction(2000),
				new DriveDistanceAction(-1*backUpFromGearPlacement),
				DriveToLocation(waypoint1),
				DriveToLocation(endAt)				
				);
	}
	
	public static Action Right(){
		Vector2 firstPoint = new Vector2(1,2);
		Vector2 placeGearAt = new Vector2(.5,3);
		double backUpFromGearPlacement = 1;
		Vector2 waypoint1 = new Vector2(1,5);
		Vector2 endAt = new Vector2(7,5);
		return Action.inSequence(
				DriveToLocation(firstPoint),
				DriveToLocation(placeGearAt),
				new UnloadGear(),
				new WaitForDigitalSensorAction(GearChuteSystem.gearSensor, false),
				new DelayAction(2000),
				new DriveDistanceAction(-1*backUpFromGearPlacement),
				DriveToLocation(waypoint1),
				DriveToLocation(endAt)				
				);
	}
	
	public static Action Center(){
		Vector2 firstPoint = new Vector2(0,114.25);
		Vector2 placeGearAt = new Vector2(0,3);
		double backUpFromGearPlacement = 1;
		Vector2 waypoint1 = new Vector2(3,5);
		Vector2 endAt = new Vector2(7,5);
		return Action.inSequence(
				DriveToLocation(firstPoint),
				//new DriveToLocation(placeGearAt),
				new UnloadGear(),
				new WaitForDigitalSensorAction(GearChuteSystem.gearSensor, false),
				new DelayAction(2000)//,
				//new DriveDistanceAction(-1*backUpFromGearPlacement),
				//DriveToLocation(waypoint1),
				//DriveToLocation(endAt)				
				);
	}
}
