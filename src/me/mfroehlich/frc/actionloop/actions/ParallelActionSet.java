package me.mfroehlich.frc.actionloop.actions;

import java.util.ArrayList;
import java.util.List;

import me.mfroehlich.frc.actionloop.actions.lib.PrintAction;

class ParallelActionSet extends Action {
	private List<Action> actions = new ArrayList<>();
	
	ParallelActionSet(String name, Action[] group) {
		this.add(new PrintAction("Starting action sequence: " + name));
		for (Action action : group) {
			this.add(action);
		}
		this.add(new PrintAction("Completed action sequence: " + name));
	}
	
	protected void add(Action c) {
		actions.add(c);
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
