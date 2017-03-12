package me.mfroehlich.frc.actionloop.actions;

import java.util.HashSet;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;

import me.mfroehlich.frc.actionloop.actions.Action.State;
import me.mfroehlich.frc.events.Event;

public class ActionContext {
	private Object mutex = new Object();
	private Set<Action> executing = new HashSet<>();
	
	public final Event executingChanged = new Event();
	public List<Action> getExecuting() {
		synchronized (mutex) {
			return new LinkedList<>(executing);
		}
	}
	
	/**
	 * Start execution of an action.
	 * @param action the action
	 */
	public void execute(Action action) {
		if (action.getState() == State.UNINITIALIZED) {
			action.initialize(this);
		}
		
		if (action.getState() != State.IDLE) {
			System.err.println("Attempted to start action in invalid state: " + action.getState());
			return;
		}
		
		action.setState(State.STARTING);
		
		synchronized (mutex) {
			executing.add(action);
		}
		executingChanged.emit();
	}
	
	/**
	 * Must be called consistently during execution of this context. This is the main event loop.
	 */
	public void tick() {
		List<Action> todo = getExecuting();
		
		for (Action action : todo) {
			switch (action.getState()) {
			case STARTING:
				action.log("starting");
				if (action.scope.lock()) {
					action.setState(State.RUNNING);
					
					action.before();
					
					if (action.isRunning()) {
						action.update();
						action.log("started");
					}
				} else {
					System.err.println(action.name + " Failed to acquire lock");
				}
				break;
				
			case RUNNING:
				action.update();
				break;
				
			case STOPPING:
				action.log("stopping");
				action.after();
				action.scope.release();
				action.setState(State.IDLE);
				action.onCompleted.emit();
				executing.remove(action);
				executingChanged.emit();
				action.log("stopped");
				break;
				
			case ABORTING:
				action.log("aborting");
				action.abort();
				action.scope.release();
				action.setState(State.IDLE);
				action.onCompleted.emit();
				executing.remove(action);
				executingChanged.emit();
				action.log("aborted");
				break;
				
			case IDLE:
				executing.remove(action);
				executingChanged.emit();
				break;
				
			default:
				System.err.println("Invalid action state: " + action.getState());
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
