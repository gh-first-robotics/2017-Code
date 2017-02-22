package org.usfirst.frc.team5530.robot.test;

import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.lib.AliasAction;
import me.mfroehlich.frc.actionloop.actions.lib.PrintAction;

public class TestAction extends AliasAction {
	public TestAction(String message, Action action) {
		super(Action.inSequence(
			new PrintAction(message),
			action,
			new PrintAction("done")
		));
	}
}
