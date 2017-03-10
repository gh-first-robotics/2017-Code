package org.usfirst.frc.team5530.robot.actions;

import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;
import org.usfirst.frc.team5530.robot.actions.gears.PositionAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.PositionLateralSlideAction;

import me.mfroehlich.frc.actionloop.actions.lib.ActionGroup;

public class LoadGear1Action extends ActionGroup {
	public LoadGear1Action() {
		super("Load Gear Prt. 1");
		
		add(new PegGripperAction(Position.HALF));
		
		next();
		
		add(new ChutePanelAction(true));
		
		next();
		
		add(new PositionAxialSlideAction(6.5));
		add(new PositionLateralSlideAction(3));
	}
}
