package org.usfirst.frc.team5530.robot.actions;

import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetLateralSlideAction;

import me.mfroehlich.frc.actionloop.actions.lib.ActionGroup;

public class QuickResetAction extends ActionGroup {
	public QuickResetAction() {
		super("Quick Reset");

		add(new ResetAxialSlideAction());
		add(new ResetLateralSlideAction());
		
		next();
		
		add(new ChutePanelAction(true));
	}
}
