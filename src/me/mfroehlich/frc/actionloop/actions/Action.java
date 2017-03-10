package me.mfroehlich.frc.actionloop.actions;

import me.mfroehlich.frc.events.Event;

public abstract class Action {
	public enum State {
		UNINITIALIZED,
		IDLE,
		STARTING,
		RUNNING,
		STOPPING,
		ABORTING
	}
	
	public final Event stateChanged = new Event();
	public final Event onCompleted = new Event();
	public final String name;
	
	private State state = State.UNINITIALIZED;
	ResourceScope scope = new ResourceScope(this);
	
	private ActionContext context;
	
	protected Action(String name) {
		this.name = name;
	}
	
	/**
	 * Cancels this action.
	 */
	public final void cancel() {
		switch (state) {
		case STARTING:
			state = State.IDLE;
			stateChanged.emit();
			return;
			
		case RUNNING:
			state = State.ABORTING;
			stateChanged.emit();
			return;
			
		default: return;
		}
	}
	
	public final boolean isRunning() {
		return state == State.STARTING || state == State.RUNNING;
	}
	
	public State getState() { return this.state; }
	public void setState(State value) {
		this.state = value;
		this.stateChanged.emit();
	}
	
	/**
	 * Initializes this action. This must only be called once, before it is executed
	 * @param context the execution context to initialize in
	 */
	
	final void initialize(ActionContext context) {
		this.context = context;
		
		this.init(scope);
		this.state = State.IDLE;
		stateChanged.emit();
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
		if (state != State.RUNNING && state != State.ABORTING && state != State.STOPPING) {
			throw new Error("Attempted to complete an action while not running");
		}
		
		state = State.STOPPING;
		stateChanged.emit();
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
	public static Action inParallel(String name, Action... actions) {
		return new ParallelActionSet(name, actions);
	}

	/**
	 * Creates a composite action that executes many actions, waiting for each to complete before starting the next
	 * @param actions the actions to compose
	 * @return the composed action
	 */
	public static Action inSequence(String name, Action... actions) {
		return new SequentialActionSet(name, actions);
	}
	
	/**
	 * Creates a composite action that executes many actions at the same time and finishes once the first action completes
	 * @param cancel true if the still-executing actions should be cancelled when the first finishes
	 * @param actions the actions to compose
	 * @return the composed action
	 */
	public static Action inRace(String name, boolean cancel, Action... actions) {
		return new RaceActionSet(name, cancel, actions);
	}
	
	public void log(String string) {
//		System.out.println(getName() + ": " + string);
	}
}
