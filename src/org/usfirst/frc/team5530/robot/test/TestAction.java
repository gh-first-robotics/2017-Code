package org.usfirst.frc.team5530.robot.test;

import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.SequentialActionSet;
import me.mfroehlich.frc.actionloop.actions.lib.PrintAction;

public class TestAction extends SequentialActionSet {
	public TestAction(String message, Action action) {
		add(new PrintAction(message));
		
		add(action);
		
		add(new PrintAction("done"));
	}
}