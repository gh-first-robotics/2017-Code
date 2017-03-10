package org.usfirst.frc.team5530.robot.actions;

import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;
import org.usfirst.frc.team5530.robot.actions.gears.ResetAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetLateralSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.RotateGearAction;

import me.mfroehlich.frc.actionloop.actions.lib.ActionGroup;

public class LoadGear2Action extends ActionGroup {
	public LoadGear2Action() {
		super("Load Gear Prt. 2");
		
		add(new ChutePanelAction(false));
		add(new PegGripperAction(Position.OPEN));
		
		next();
		
		add(new ResetAxialSlideAction());
		add(new ResetLateralSlideAction());
		
		next();
		
		add(new RotateGearAction());
	}
}
