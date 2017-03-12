package org.usfirst.frc.team5530.robot;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

import org.usfirst.frc.team5530.robot.autonomous.AutonomousController;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.TeleopController;
import org.usfirst.frc.team5530.robot.test.TestController;

import me.mfroehlich.frc.abstractions.RobotStateProvider;
import me.mfroehlich.frc.abstractions.RobotStateProvider.State;
import me.mfroehlich.frc.actionloop.ActionRobot;

public class Robot extends ActionRobot {
	public Robot(RobotStateProvider provider) {
		super(provider);
	}

	@Override
	public void init() {
		this.tick.listen(DriveTrainSystem.position::tick);
		
		setController(State.TEST, new TestController());
		setController(State.OPERATOR, new TeleopController());
		setController(State.AUTONOMOUS, new AutonomousController());
	}
	
	public static void log(String name, String value) {
		try {
			Path path = Paths.get("/home/lvuser/logs/" + name + ".log");
			Files.write(path, value.getBytes());
		} catch (IOException x) {
			x.printStackTrace();
		}
	}
}
