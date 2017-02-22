package org.usfirst.frc.team5530.robot;

import org.usfirst.frc.team5530.robot.teleop.TeleopController;
import org.usfirst.frc.team5530.robot.test.TestController;

import me.mfroehlich.frc.abstractions.RobotStateProvider;
import me.mfroehlich.frc.abstractions.RobotStateProvider.State;
import me.mfroehlich.frc.eventloop.EventLoopRobot;

public class Robot extends EventLoopRobot {
	public Robot(RobotStateProvider provider) {
		super(provider);
	}

	@Override
	public void init() {
		setController(State.TEST, new TestController());
		setController(State.TELEOP, new TeleopController());
	}
}
