package org.usfirst.frc.team5530.robot.actions;

import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetLateralSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.RotateGearAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;

import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.SequentialActionSet;

public class LoadGear2Action extends SequentialActionSet {
	public LoadGear2Action() {
		add(new ChutePanelAction(false));
		add(new PegGripperAction(Position.OPEN));
		
		add(Action.inParallel(
			new ResetAxialSlideAction(),
			new ResetLateralSlideAction()
		));
		
		add(new RotateGearAction());
	}
}
