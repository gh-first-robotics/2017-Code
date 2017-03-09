package org.usfirst.frc.team5530.robot;

import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveDistanceAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveToLocation;
import org.usfirst.frc.team5530.robot.actions.drivetrain.TurnToAngleAction;
import org.usfirst.frc.team5530.robot.actions.gears.UnloadGear;
import org.usfirst.frc.team5530.robot.auton.AutonController;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
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
		DriveTrainSystem.init();
		setController(State.TEST, new TestController());
		setController(State.TELEOP, new TeleopController());
		setController(State.AUTONOMOUS, new AutonController());
	}
}
