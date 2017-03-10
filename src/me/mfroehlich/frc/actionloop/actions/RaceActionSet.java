package me.mfroehlich.frc.actionloop.actions;

import java.util.ArrayList;
import java.util.List;

import me.mfroehlich.frc.actionloop.actions.lib.PrintAction;

class RaceActionSet extends Action {
	private List<Action> actions = new ArrayList<>();
	private boolean cancel;
	
	RaceActionSet(String name, boolean cancel, Action[] group) {
		super(name);
		
		this.actions.add(new PrintAction("Starting action race: " + this.name));
		
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
			} else {
				System.out.println("Race completed: " + action.name);
			}
		}
		
		if (!complete) return;
		
		if (this.cancel) {
			for (Action action : actions) {
				action.cancel();
			}
		}

		System.out.println("Completed action race: " + this.name);
		this.complete();
	}
	
	@Override
	protected void abort() {
		for (Action action : actions) {
			action.cancel();
		}
	}
}
