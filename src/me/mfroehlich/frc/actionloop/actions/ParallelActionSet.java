package me.mfroehlich.frc.actionloop.actions;

import java.util.ArrayList;
import java.util.List;

import me.mfroehlich.frc.actionloop.actions.lib.PrintAction;

class ParallelActionSet extends Action {
	private List<Action> actions = new ArrayList<>();
	
	ParallelActionSet(String name, Action[] group) {
		super(name);
		
		this.add(new PrintAction("Starting parallel action: " + this.name));
		
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
		
		System.out.println("Completed parallel action: " + this.name);
		this.complete();
	}
	
	@Override
	protected void abort() {
		for (Action c : actions) {
			c.cancel();
		}
	}
}
