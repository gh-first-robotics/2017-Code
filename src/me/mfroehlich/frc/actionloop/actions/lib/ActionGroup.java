package me.mfroehlich.frc.actionloop.actions.lib;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

import me.mfroehlich.frc.actionloop.actions.Action;

public abstract class ActionGroup extends Action {
	private List<Queue<Action>> entries = new ArrayList<>();
	
	private Queue<Queue<Action>> queue;
	private Queue<Action> current;
	
	
	protected ActionGroup(String name) {
		super(name);
		this.next();
	}
	
	/**
	 * Adds an action to the current step of the sequence
	 * @param action the action to add
	 */
	protected void add(Action action) {
		entries.get(entries.size() - 1).add(action);
	}
	
	/**
	 * Advances to the next step to add actions to
	 */
	protected void next() {
		entries.add(new LinkedList<>());
	}
	
	@Override
	protected void before() {
		System.out.println("Starting action group: " + this.name);
		queue = new LinkedList<>(entries);
	}
	
	private boolean isCurrentComplete() {
		if (current == null)
			return true;
		
		for (Action a : current) {
			if (a.isRunning()) {
				return false;
			}
		}
		
		return true;
	}
	
	@Override
	protected void update() {
		if (!isCurrentComplete()) {
			return;
		}
		
		current = queue.poll();
		
		if (current == null) {
			this.complete();
		} else {
			for (Action a : current) {
				this.child(a);
			}
		}
	}

	@Override
	protected void abort() { }
	
	@Override
	protected void after() {
		System.out.println("Completed action group: " + this.name);
	}
}
