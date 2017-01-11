package org.usfirst.frc.team5530.robot.teleop;

import java.util.HashMap;
import java.util.Map.Entry;

import edu.wpi.first.wpilibj.Joystick;
import edu.wpi.first.wpilibj.buttons.JoystickButton;

public class Controls {
	private HashMap<InputButton, JoystickButton> buttons = new HashMap<>();
	private Joystick[] sticks;
	private ControlState current;

	public Controls(Joystick... sticks) {
		this.sticks = sticks;
		for (InputButton button : InputButton.values()) {
			buttons.put(button, new JoystickButton(sticks[button.stick], button.button));
		}
	}

	/**
	 * Gets the most recently updated control state
	 *
	 * @return the current control state
	 */
	public ControlState state() {
		return current;
	}

	/**
	 * Reads a new control state from the input
	 *
	 * @return the new control state
	 */
	public ControlState update() {
		current = new ControlState(sticks.length);
		for (Entry<InputButton, JoystickButton> pair : buttons.entrySet()) {
			current.buttons.put(pair.getKey(), pair.getValue().get());
		}
		for (int i = 0; i < sticks.length; i++) {
			current.sticks[i] = new Vector2(sticks[i].getX(), sticks[i].getY());
		}
		return state();
	}
}
