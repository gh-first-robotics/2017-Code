package me.mfroehlich.frc.controls;

import java.util.LinkedList;
import java.util.List;

import org.usfirst.frc.team5530.robot.teleop.ControlButton;

import me.mfroehlich.frc.abstractions.Controls;
import me.mfroehlich.frc.actionloop.Controller;
import me.mfroehlich.frc.actionloop.actions.Action;

public class ButtonMap {
	private List<Mapping> onPressed = new LinkedList<>();
	private List<Mapping> whilePressed = new LinkedList<>();
	
	private Controls controls;
	private ControlsState state, lastState;
	
	private Controller executor;
	
	public ButtonMap(Controls controls, Controller executor) {
		this.controls = controls;
		this.executor = executor;
	}
	
	public Button map(int stick, int button, String name) {
		return new Button(stick, button, name, this);
	}
	
	void onPressed(ControlButton control, Action action) {
		onPressed.add(new Mapping(control, action));
	}
	
	void whilePressed(ControlButton control, Action action) {
		whilePressed.add(new Mapping(control, action));
	}
	
	public ControlsState update() {
		lastState = state;
		state = controls.getState();
		
		for (Mapping mapping : onPressed) {
			if (state.isNewlyPressed(mapping.button, lastState)) {
				executor.execute(mapping.action);
			}
		}
		
		for (Mapping mapping : whilePressed) {
			if (state.isNewlyPressed(mapping.button, lastState)) {
				executor.execute(mapping.action);
			} else if (state.isNewlyReleased(mapping.button, lastState)) {
				mapping.action.cancel();
			}
		}
		
		return state;
	}
	
	private static class Mapping {
		public final ControlButton button;
		public final Action action;
		
		public Mapping(ControlButton button, Action action) {
			this.button = button;
			this.action = action;
		}
	}
}
