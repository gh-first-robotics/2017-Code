package me.mfroehlich.frc.actionloop.actions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import me.mfroehlich.frc.actionloop.actions.Action.State;

public class ActionContext {
	private Object mutex = new Object();
	private Set<Action> executing = new HashSet<>();
	
	/**
	 * Start execution of an action.
	 * @param action the action
	 */
	public void execute(Action action) {
		if (action.state == State.UNINITIALIZED) {
			action.initialize(this);
		}
		
		if (action.state != State.IDLE) {
			System.err.println("Attempted to start action in invalid state: " + action.state);
			return;
		}
		
		action.state = State.STARTING;
		
		synchronized (mutex) {
			executing.add(action);
		}
	}
	
	/**
	 * Must be called consistently during execution of this context. This is the main event loop.
	 */
	public void tick() {
		List<Action> todo;
		synchronized (mutex) {
			todo = new LinkedList<>(executing);
		}
		
		for (Action action : todo) {
			switch (action.state) {
			case STARTING:
				action.log("starting");
				if (action.scope.lock()) {
					action.state = State.RUNNING;
					
					action.before();
					action.update();
					action.log("started");
				}
				break;
				
			case RUNNING:
				action.update();
				break;
				
			case STOPPING:
				action.log("stopping");
				action.after();
				action.scope.release();
				action.state = State.IDLE;
				action.onCompleted.emit();
				executing.remove(action);
				action.log("stopped");
				break;
				
			case ABORTING:
				action.log("aborting");
				action.abort();
				action.scope.release();
				action.state = State.IDLE;
				action.onCompleted.emit();
				executing.remove(action);
				action.log("stopped");
				break;
				
			case IDLE:
				executing.remove(action);
				break;
				
			default:
				System.err.println("Invalid action state: " + action.state);
				break;
			}
		}
	}
	
	/**
	 * Cancels all currently executing actions.
	 */
	public void cancelAll() {
		for (Action action : executing) {
			action.cancel();
		}
	}
}
