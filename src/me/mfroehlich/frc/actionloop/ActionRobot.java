package me.mfroehlich.frc.actionloop;

import java.util.HashMap;
import java.util.Map;

import me.mfroehlich.frc.abstractions.RobotStateProvider;
import me.mfroehlich.frc.abstractions.RobotStateProvider.State;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ActionContext;

public abstract class ActionRobot {
	private Map<State, Controller> controllers = new HashMap<>();
	private Controller currentController;
	private State currentState;
	
	private ActionContext actions = new ActionContext();
	private RobotStateProvider provider;
	
	protected ActionRobot(RobotStateProvider provider) {
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
		// Tick the current controller if there is one
		if (currentController != null) {
			currentController.tick();
		}
		// Tick the action loop to actually run the robot
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
