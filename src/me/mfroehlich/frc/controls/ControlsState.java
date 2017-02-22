package me.mfroehlich.frc.controls;

import java.util.Set;

import org.usfirst.frc.team5530.robot.teleop.ControlButton;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

public class ControlsState {
	private Set<ControlButton> buttons;
	private Vector2[] sticks;
	
	public ControlsState(Vector2[] sticks, Set<ControlButton> buttons) {
		this.sticks = sticks;
		this.buttons = buttons;
	}
	
	public boolean isPressed(ControlButton button) {
		return buttons.contains(button);
	}
	
	public boolean isNewlyPressed(ControlButton button, ControlsState old) {
		return isPressed(button) && (old == null || !old.isPressed(button));
	}
	
	public boolean isNewlyReleased(ControlButton button, ControlsState old) {
		return !isPressed(button) && old != null && old.isPressed(button);
	}
	
	public Vector2 getStick(int number) {
		return sticks[number - 1];
	}
}
