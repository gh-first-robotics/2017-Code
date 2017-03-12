package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.actions.UnloadGearAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveDistanceAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.TurnAction;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import me.mfroehlich.frc.abstractions.Environment;
import me.mfroehlich.frc.abstractions.live.LiveEnvironment;
import me.mfroehlich.frc.actionloop.Controller;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.lib.PrintAction;

public class AutonomousController extends Controller {
	private SendableChooser<Action> chooser = new SendableChooser<>();
	
	@Override
	protected void init() {
		chooser.addDefault("Do nothing", new PrintAction("Doing nothing"));
		
		chooser.addObject("Drive forward", Action.inParallel("Autonomous",
			new AutonomousInitAction(),
			new DriveDistanceAction(82)
		));
		
		chooser.addObject("Drive forward and place gear", Action.inSequence("Autonomous",
			Action.inParallel("Autonomous init and move",
				new AutonomousInitAction(),
				new DriveDistanceAction(82)
			),
			new UnloadGearAction()
		));
		
		chooser.addObject("Drive forward, turn -60, drive forward, place gear (Right)", Action.inSequence("Autonomous",
			Action.inParallel("Autonomous init and move", 
				new AutonomousInitAction(),
				new DriveDistanceAction(82.5)
			),
			new TurnAction(-60),
			new DriveDistanceAction(48.5),
			new UnloadGearAction()
		));
		
		chooser.addObject("Drive forward, turn 60, drive forward, place gear (Left)", Action.inSequence("Autonomous",
			Action.inParallel("Autonomous init and move", 
				new AutonomousInitAction(),
				new DriveDistanceAction(91.5)
			),
			new TurnAction(60),
			new DriveDistanceAction(25.5),
			new UnloadGearAction()
		));
		
		if (Environment.is(LiveEnvironment.class)) {
			SmartDashboard.putData("Autonomous Sequence", chooser);
		}
	}

	@Override
	public void start() {
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
