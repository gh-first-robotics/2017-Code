package me.mfroehlich.frc.eventloop.actions.lib;

import me.mfroehlich.frc.eventloop.actions.Action;
import me.mfroehlich.frc.eventloop.actions.ResourceScope;

public abstract class AliasAction extends Action {
	private Action action;
	
	protected AliasAction(Action action) {
		this.action = action;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		this.listen(action.onCompleted);
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
