package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.SequentialActionSet;

public class UnloadGear extends SequentialActionSet {	
	
	public UnloadGear() {
		add(new AlignSlideWithPegAction());
		add(Action.inParallal(
					new ChutePanelAction(false),
					new PegGripperAction(Position.CLOSED)
				));
		add (new RotateGearAction());
		add(new PositionAxialSlideAction(4.5, true));
		add(new PegGripperAction(Position.OPEN));
		add(new PositionAxialSlideAction(8.5));
				
	}
	
}
