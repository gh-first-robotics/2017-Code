package org.usfirst.frc.team5530.robot.actions;

import org.usfirst.frc.team5530.robot.actions.gears.AlignSlideWithPegAction;
import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.PositionAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.RotateGearAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;

import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.SequentialActionSet;
import me.mfroehlich.frc.actionloop.actions.lib.DelayAction;

public class UnloadGearAction extends SequentialActionSet {
	public UnloadGearAction() {
		
		add(new AlignSlideWithPegAction());
		
		add(Action.inParallel(
			new ChutePanelAction(false),
			new PegGripperAction(Position.CLOSED)
		));
		
		add(Action.inParallel(
			new DelayAction(500),
			new RotateGearAction()
		));
		
		add(new PositionAxialSlideAction(5, true));
		add(new PegGripperAction(Position.OPEN));
		add(new PositionAxialSlideAction(8.5));
	}
	
	@Override
	protected void after() {
		super.after();
		
		System.out.println("Unload complete");
	}
}
