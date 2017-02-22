package me.mfroehlich.frc.actionloop.actions;

import java.util.ArrayList;
import java.util.List;

class ParallelActionSet extends Action {
	private List<Action> actions = new ArrayList<>();
	
	public ParallelActionSet(Action[] group) {
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
		for (Action c : actions) {
			if (c.isRunning()) {
				return;
			}
		}

		this.complete();
	}
	
	@Override
	protected void abort() {
		for (Action c : actions) {
			c.cancel();
		}
	}
}
