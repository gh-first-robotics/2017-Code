package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.actions.LoadGear2Action;
import org.usfirst.frc.team5530.robot.actions.UnloadGearAction;
import org.usfirst.frc.team5530.robot.actions.climber.RopeGripperAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveDistanceAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.TurnAction;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import me.mfroehlich.frc.actionloop.Controller;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.lib.PrintAction;

public class AutonomousController extends Controller {
	private SendableChooser<Action> chooser = new SendableChooser<>();
	
	@Override
	protected void init() {
		chooser.addDefault("Do nothing", new PrintAction("Doing nothing"));
		
		chooser.addObject("Drive forward", new DriveDistanceAction(82));
		
		chooser.addObject("Drive forward and place gear", Action.inSequence("Autonomous",
			Action.inParallel("Autonomous align and move",
				new RopeGripperAction(true),
				new LoadGear2Action(),
				new DriveDistanceAction(82)
			),
			new UnloadGearAction()
		));
		
		chooser.addObject("Drive forward, turn -60, drive forward, place gear (Right)", Action.inSequence("Autonomous",
			Action.inParallel("Autonomous align and drive", 
				new RopeGripperAction(true),
				new LoadGear2Action(),
				new DriveDistanceAction(82.5)
			),
			new TurnAction(-60),
			new DriveDistanceAction(48.5),
			new UnloadGearAction()
		));
		
		chooser.addObject("Drive forward, turn 60, drive forward, place gear (Left)", Action.inSequence("Autonomous",
			Action.inParallel("Autonomous align and drive", 
				new RopeGripperAction(true),
				new LoadGear2Action(),
				new DriveDistanceAction(91.5)
			),
			new TurnAction(60),
			new DriveDistanceAction(25.5),
			new UnloadGearAction()
		));
		
		SmartDashboard.putData("Autonomous Sequence", chooser);
	}

	@Override
	public void start() {
//		execute(Action.inSequence("Autonomous",
//			Action.inParallel("Autonomous align and move",
//				new RopeGripperAction(true),
//				new LoadGear2Action(),
//				new DriveDistanceAction(74)
//			),
//			new UnloadGearAction()
//		));
		Action selected = chooser.getSelected();
		
		if (selected != null) {
			execute(selected);
		}
	}

	@Override
	public void tick() { }

	@Override
	public void stop() { }
}
