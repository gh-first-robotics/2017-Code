package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.actions.LoadGear2Action;
import org.usfirst.frc.team5530.robot.actions.climber.RopeGripperAction;

import me.mfroehlich.frc.actionloop.actions.lib.ActionGroup;

public class AutonomousInitAction extends ActionGroup {
	protected AutonomousInitAction() {
		super("Autonomous init");
		
		add(new RopeGripperAction(true));
		
		add(new LoadGear2Action());
	}

}
