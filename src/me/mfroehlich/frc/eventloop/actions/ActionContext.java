package me.mfroehlich.frc.eventloop.actions;

import java.util.HashSet;
import java.util.Set;

import me.mfroehlich.frc.eventloop.actions.Action.State;
import me.mfroehlich.frc.eventloop.events.Callback;

public class ActionContext {
	private Object scheduleMutex = new Object();
	
	private Set<Action> scheduled_active = new HashSet<>();
	private Set<Action> scheduled_buffer = new HashSet<>();
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
		schedule(action);
	}
	
	/**
	 * Must be called consistently during execution of this context. This is the main event loop.
	 */
	public void tick() {
		synchronized (scheduleMutex) {
			Set<Action> swap = scheduled_buffer;
			scheduled_buffer = scheduled_active;
			scheduled_active = swap;
		}
		
		for (Action action : scheduled_active) {
			switch (action.state) {
			case STARTING:
				action.log("starting");
				if (action.scope.lock()) {
					executing.add(action);
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
				action.complete();
				break;
				
			default:
				System.err.println("Invalid action state: " + action.state);
				break;
			}
		}
		
		scheduled_active.clear();
	}
	
	/**
	 * Cancels all currently executing actions.
	 */
	public void cancelAll() {
		for (Action action : executing) {
			action.cancel();
		}
	}
	
	private void schedule(Action update) {
		synchronized (scheduleMutex) {
			scheduled_buffer.add(update);
		}
	}
	
	public class UpdateNotifier implements Callback {
		private Action action;
		
		public UpdateNotifier(Action action) {
			this.action = action;
		}
		
		public void invoke() {
			if (action.state == State.IDLE || action.state == State.UNINITIALIZED)
				return;
			schedule(action);
		}
	}
}
