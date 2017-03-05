//package me.mfroehlich.frc.actionloop;
//
//import edu.wpi.first.wpilibj.SampleRobot;
//import me.mfroehlich.frc.abstractions.RobotStateProvider;
//
//public class CompetitionRobot2 extends SampleRobot implements RobotStateProvider {
//	private ActionRobot robot;
//	
//	@Override
//	public State getState() {
//		if (isDisabled()) {
//			return State.DISABLED;
//		} else if (isOperatorControl()) {
//			return State.TELEOP;
//		} else if (isAutonomous()) {
//			return State.AUTONOMOUS;
//		} else if (isTest()) {
//			return State.TEST;
//		} else {
//			return State.DISABLED;
//		}
//	}
//	
//	@Override
//	protected void robotInit() {
//		robot.init();
//	}
//	
//	@Override
//	public void robotMain() {
//		robot.run();
//	}
//}
