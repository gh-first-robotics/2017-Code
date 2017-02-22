package me.mfroehlich.frc.actionloop.actions;

import me.mfroehlich.frc.eventloop.events.Event;

public abstract class Action {
	public enum State {
		UNINITIALIZED,
		IDLE,
		STARTING,
		RUNNING,
		STOPPING,
		ABORTING
	}
	
	public Event onCompleted = new Event();
	
	State state = State.UNINITIALIZED;
	ResourceScope scope = new ResourceScope(this);
	
	private ActionContext context;
	
	/**
	 * Cancels this action.
	 */
	public final void cancel() {
		switch (state) {
		case STARTING:
			state = State.IDLE;
			return;
			
		case RUNNING:
			state = State.ABORTING;
			return;
			
		default: return;
		}
	}
	
	public final boolean isRunning() {
		return state == State.STARTING || state == State.RUNNING;
	}
	
	/**
	 * Initializes this action. This must only be called once, before it is executed
	 * @param context the execution context to initialize in
	 */
	
	final void initialize(ActionContext context) {
		this.context = context;
		
		this.init(scope);
		this.state = State.IDLE;
	}

	/**
	 * Called as a part of action initialization before the first execution
	 * @param scope the action's scope, used for requiring resources
	 */
	protected void init(ResourceScope scope) { }
	
	/**
	 * Called once per execution, before any updates
	 */
	protected void before() { }
	
	/**
	 * Called when necessary during the action execution
	 */
	protected abstract void update();
	
	/**
	 * Called if the action is cancelled, perform any unexpected cleanup here
	 */
	protected abstract void abort();
	
	/**
	 * Called after the action is completed, perform any necessary cleanup here
	 */
	protected void after() { }
	
	/**
	 * Mark this action as complete.
	 */
	protected final void complete() {
		if (state != State.RUNNING && state != State.ABORTING) {
			throw new Error("Attempted to complete an action while not running");
		}
		
		state = State.STOPPING;
	}

	/**
	 * Execute a sub-action
	 * @param action the action
	 */
	protected final void child(Action action) {
		if (state != State.RUNNING) {
			throw new Error("Attempted to start child action while not running");
		}
		
		context.execute(action);
	}
	
	/**
	 * Creates a composite action that executes many actions at the same time
	 * @param actions the actions to compose
	 * @return the composed action
	 */
	public static Action inParallal(Action... actions) {
		return new ParallelActionSet(actions);
	}

	/**
	 * Creates a composite action that executes many actions, waiting for each to complete before starting the next
	 * @param actions the actions to compose
	 * @return the composed action
	 */
	public static Action inSequence(Action... actions) {
		return new SequentialActionSet(actions);
	}
	
	/**
	 * Creates a composite action that executes many actions at the same time and finishes once the first action completes
	 * @param cancel true if the still-executing actions should be cancelled when the first finishes
	 * @param actions the actions to compose
	 * @return the composed action
	 */
	public static Action inRace(boolean cancel, Action... actions) {
		return new RaceActionSet(cancel, actions);
	}

	public void log(String string) {
//		System.out.println(getClass().getSimpleName() + ": " + string);
	}
}
