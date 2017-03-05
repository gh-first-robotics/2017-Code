package me.mfroehlich.frc.actionloop.actions;

import java.util.ArrayList;
import java.util.List;

public class ParallelActionSet extends Action {
	private List<Action> actions = new ArrayList<>();
	
	protected ParallelActionSet() { }
	
	ParallelActionSet(Action[] group) {
		for (Action action : group) {
			this.add(action);
		}
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
