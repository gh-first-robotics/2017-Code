package me.mfroehlich.frc.actionloop.actions.lib;

import me.mfroehlich.frc.actionloop.actions.Action;

public abstract class AliasAction extends Action {
	private Action action;
	
	protected AliasAction(Action action) {
		this.action = action;
	}
	
	@Override
	protected void before() {
		this.child(action);
	}

	@Override
	protected void update() {
		if (this.action.isRunning())
			return;
		
		this.complete();
	}

	@Override
	protected void abort() {
		this.action.cancel();
	}
}
