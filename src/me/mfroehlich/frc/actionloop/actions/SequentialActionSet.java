package me.mfroehlich.frc.actionloop.actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

class SequentialActionSet extends Action {
	private List<Action> actions = new ArrayList<>();
	private Queue<Action> queue;
	private Action current;
	
	public SequentialActionSet(Action[] commands) {
		for (Action c : commands) {
			add(c);
		}
	}
	
	protected void add(Action c) {
		actions.add(c);
	}

	@Override
	protected void before() {
		queue = new LinkedList<>(actions);
	}

	@Override
	public void update() {
		if (current != null && current.state == State.ABORTING) {
			this.cancel();
			return;
		} else if (current != null && current.isRunning()) {
			return;
		}
		
		current = queue.poll();
		
		if (current == null) {
			complete();
		} else {
			this.child(current);
		}
	}
	
	@Override
	protected void abort() {
		current.cancel();
	}
}