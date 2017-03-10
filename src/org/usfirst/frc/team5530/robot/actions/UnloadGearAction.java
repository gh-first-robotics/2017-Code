package org.usfirst.frc.team5530.robot.actions;

import org.usfirst.frc.team5530.robot.actions.gears.AlignSlideWithPegAction;
import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;
import org.usfirst.frc.team5530.robot.actions.gears.PositionAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.RotateGearAction;

import me.mfroehlich.frc.actionloop.actions.lib.ActionGroup;
import me.mfroehlich.frc.actionloop.actions.lib.DelayAction;

public class UnloadGearAction extends ActionGroup {
	public UnloadGearAction() {
		super("Unload Gear");
		
		add(new AlignSlideWithPegAction());
		
		next();
		
		add(new ChutePanelAction(false));
		add(new PegGripperAction(Position.CLOSED));
		
		next();
		
		add(new DelayAction(500));
		add(new RotateGearAction());

		next();
		
		add(new PositionAxialSlideAction(5, true));
		
		next();
		
		add(new PegGripperAction(Position.OPEN));
		
		next();
		
		add(new PositionAxialSlideAction(8.5));
	}
}
