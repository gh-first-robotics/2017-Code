package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveAccurateAction;

import me.mfroehlich.frc.actionloop.Controller;

public class AutonomousController extends Controller {
	@Override
	protected void init() {
		
	}

	@Override
	public void start() {
		execute(new DriveAccurateAction(74));
//		execute(new DriveDistance2Action(100));
//		execute(Action.inSequence("Autonomous",
//			Action.inParallel("Autonomous align and move",
//				new RopeGripperAction(true),
//				new LoadGear2Action(),
//				new DriveDistanceAction(74)
//			),
//			new UnloadGearAction()
//		));
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void stop() {
		
	}
}
