package me.mfroehlich.frc.actionloop.actions;

import java.util.ArrayList;
import java.util.List;

class RaceActionSet extends Action {
	private List<Action> actions = new ArrayList<>();
	private boolean cancel;
	
	public RaceActionSet(boolean cancel, Action[] group) {
		this.cancel = cancel;
		for (Action action : group) {
			this.actions.add(action);
		}
	}

	@Override
	protected void before() {
		for (Action c : actions) {
			this.child(c);
		}
	}
	
	@Override
	public void update() {
		boolean complete = false;
		
		for (Action action : actions) {
			if (!action.isRunning()) {
				complete = true;
				break;
			}
		}
		
		if (!complete) return;
		
		if (this.cancel) {
			for (Action action : actions) {
				action.cancel();
			}
		}

		this.complete();
	}
	
	@Override
	protected void abort() {
		for (Action action : actions) {
			action.cancel();
		}
	}
}
