package me.mfroehlich.frc.actionloop.actions;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.mfroehlich.frc.actionloop.actions.lib.PrintAction;

class SequentialActionSet extends Action {
	private List<Action> actions = new ArrayList<>();
	
	private Queue<Action> queue;
	private Action current;
	
	SequentialActionSet(String name, Action[] commands) {
		super(name);
		
		add(new PrintAction("Starting action sequence: " + this.name));
		
		for (Action c : commands) {
			add(c);
		}
		
		add(new PrintAction("Completed action sequence: " + this.name));
	}
	
	protected void add(Action c) {
		actions.add(c);
	}

	@Override
	protected void before() {
		queue = new LinkedList<>(actions);
		current = null;
	}

	@Override
	public void update() {
		if (current != null && current.getState() == State.ABORTING) {
			this.cancel();
			return;
		} else if (current != null && current.isRunning()) {
			return;
		}
		
		current = queue.poll();
		
		if (current == null) {
			this.complete();
		} else {
			this.child(current);
		}
	}
	
	@Override
	protected void abort() {
		if (current != null)
			current.cancel();
	}
}
