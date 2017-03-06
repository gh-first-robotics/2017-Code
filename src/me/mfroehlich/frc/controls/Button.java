package me.mfroehlich.frc.controls;

import me.mfroehlich.frc.actionloop.actions.Action;

public class Button {
	public final int stick, button;
	private String name;
	
	private ButtonMap owner;
	
	Button(int stick, int button, String name, ButtonMap owner) {
		this.stick = stick;
		this.button = button;
		this.name = name;
		this.owner = owner;
	}
	
	public boolean isNewlyPressed() {
		return owner.isNewlyPressed(this);
	}
	
	public boolean isNewlyReleased() {
		return owner.isNewlyReleased(this);
	}
	
	public void bind(Binding binding, Action action) {
		switch (binding) {
		case ON_PRESS:
			owner.onPressed(this, action);
			break;
			
		case WHILE_PRESSED:
			owner.whilePressed(this, action);
			break;
		}
	}
	
	@Override
	public String toString() {
		return name + " (" + this.stick + ", " + this.button + ")";
	}
	
	public enum Binding {
		ON_PRESS,
		WHILE_PRESSED
	}
}
