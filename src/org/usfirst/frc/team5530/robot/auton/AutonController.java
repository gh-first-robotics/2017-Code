package org.usfirst.frc.team5530.robot.auton;

import org.usfirst.frc.team5530.robot.actions.LoadGear2Action;
import org.usfirst.frc.team5530.robot.actions.UnloadGearAction;
import org.usfirst.frc.team5530.robot.actions.climber.RopeGripperAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveDistanceAction;

import me.mfroehlich.frc.actionloop.Controller;
import me.mfroehlich.frc.actionloop.actions.Action;

public class AutonController extends Controller {
	@Override
	protected void init() {
		
	}

	@Override
	public void start() {
//		execute(new DriveDistance2Action(100));
		execute(Action.inSequence(
			Action.inParallel(
				new RopeGripperAction(true),
				new LoadGear2Action(),
				new DriveDistanceAction(74)
			),
			new UnloadGearAction()
		));
	}

	@Override
	public void tick() {
		
	}

	@Override
	public void stop() {
		
	}
}
