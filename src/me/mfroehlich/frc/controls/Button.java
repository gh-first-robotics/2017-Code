package me.mfroehlich.frc.controls;

import org.usfirst.frc.team5530.robot.teleop.ControlButton;

import me.mfroehlich.frc.eventloop.actions.Action;

public class Button {
	private ControlButton control;
	private String name;
	
	private ButtonMap owner;
	
	Button(int stick, int button, String name, ButtonMap owner) {
		this.control = new ControlButton(stick, button);
		this.name = name;
		this.owner = owner;
	}
	
	public void bind(Binding binding, Action action) {
		switch (binding) {
		case ON_PRESS:
			owner.onPressed(control, action);
			break;
			
		case WHILE_PRESSED:
			owner.whilePressed(control, action);
			break;
		}
	}
	
	@Override
	public String toString() {
		return name + " (" + control.stick + ", " + control.button + ")";
	}
	
	public enum Binding {
		ON_PRESS,
		WHILE_PRESSED
	}
}
