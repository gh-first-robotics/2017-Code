package me.mfroehlich.frc.actionloop;

import org.usfirst.frc.team5530.robot.Robot;

import edu.wpi.first.wpilibj.RobotBase;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tInstances;
import edu.wpi.first.wpilibj.hal.FRCNetComm.tResourceType;
import edu.wpi.first.wpilibj.hal.HAL;
import edu.wpi.first.wpilibj.livewindow.LiveWindow;
import me.mfroehlich.frc.abstractions.RobotStateProvider;

public class CompetitionRobot extends RobotBase implements RobotStateProvider {
	@Override
	public State getState() {
		if (isDisabled()) {
			return State.DISABLED;
		} else if (isOperatorControl()) {
			return State.TELEOP;
		} else if (isAutonomous()) {
			return State.AUTONOMOUS;
		} else if (isTest()) {
			return State.TEST;
		} else {
			return State.DISABLED;
		}
	}
	
	@Override
	public final void startCompetition() {
		ActionRobot robot = new Robot(this);
		
		HAL.report(tResourceType.kResourceType_Framework, tInstances.kFramework_Simple);
		
		robot.init();
		
		HAL.observeUserProgramStarting();
		LiveWindow.setEnabled(true);
		
		robot.run();
	}
}
