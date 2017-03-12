package me.mfroehlich.frc.actionloop.actions;

import java.util.ArrayList;
import java.util.List;

class RaceActionSet extends Action {
	private List<Action> actions = new ArrayList<>();
	private boolean cancel;
	
	RaceActionSet(String name, boolean cancel, Action[] group) {
		super(name);
		
		this.cancel = cancel;
		for (Action action : group) {
			this.actions.add(action);
		}
	}

	@Override
	protected void before() {
		System.out.println("Starting action race: " + this.name);
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
