package me.mfroehlich.frc.eventloop;

import java.util.HashMap;
import java.util.Map;

import me.mfroehlich.frc.abstractions.RobotStateProvider;
import me.mfroehlich.frc.abstractions.RobotStateProvider.State;
import me.mfroehlich.frc.eventloop.actions.Action;
import me.mfroehlich.frc.eventloop.actions.ActionContext;
import me.mfroehlich.frc.eventloop.events.Event;

public abstract class EventLoopRobot {
	public static Event tick = new Event();
	
	private Map<State, Controller> controllers = new HashMap<>();
	private Controller currentController;
	private State currentState;
	
	private ActionContext actions = new ActionContext();
	private RobotStateProvider provider;
	
	protected EventLoopRobot(RobotStateProvider provider) {
		this.provider = provider;
	}
	
	public abstract void init();

	protected void setController(State state, Controller controller) {
		controllers.put(state, controller);
		controller.initialize(this);
	}
	
	public final void execute(Action action) {
		if (currentState == State.DISABLED) return;
		
		actions.execute(action);
	}
	
	public void run() {
		while (true) {
			long start = System.currentTimeMillis();
			
			this.update();
			
			long diff = System.currentTimeMillis() - start;
			
			if (diff >= 5) {
//				System.out.println("Long tick: " + diff);
				continue;
			}
			
			try {
				Thread.sleep(5 - diff);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
		}
	}
	
	private void update() {
		// First, emit the tick event. Any actions that listen for it are then queued
		tick.emit();
		actions.tick();
		
		// Translate the boolean flags into a state
		State state = provider.getState();
		
		// If there is no change of state, we are done
		if (state == currentState) {
			return;
		}
		
		// Stop the current controller and actions
		if (currentController != null) {
			currentController.stop();
			actions.cancelAll();
		}
		
		currentState = state;
		currentController = controllers.get(state);
		
		// Start the new controller, if it is implemented
		if (currentController != null) {
			currentController.start();
		}
	}
}
