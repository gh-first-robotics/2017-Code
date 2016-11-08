package org.usfirst.frc.team5530.robot.teleop;

import java.util.Collection;
import java.util.HashMap;

public class ControlState {
	HashMap<InputButton, Boolean> buttons = new HashMap<>();
	Vector2[] sticks;

	public ControlState(Vector2[] sticks, HashMap<InputButton, Boolean> buttons) {
		this.buttons = buttons;
		this.sticks = sticks;
	}

	public ControlState(int size) {
		sticks = new Vector2[size];
	}

	/**
	 * Gets the input state of a button in this control state
	 *
	 * @param button
	 *            the button to check
	 * @return true if the button is pressed
	 */
	public boolean isPressed(InputButton button) {
		return buttons.get(button);
	}

	/**
	 * Checks whether a button was pressed between two input states
	 *
	 * @param button
	 *            the button to check the status of
	 * @param previous
	 *            the control state to compare to
	 * @return true if the button is pressed in this state but not the
	 *         previous
	 */
	public boolean isNewlyPressed(InputButton button, ControlState previous) {
		return isPressed(button) && !previous.isPressed(button);
	}

	/**
	 * Gets the number of sticks in this control state
	 *
	 * @return the number of sticks
	 */
	public int getStickCount() {
		return sticks.length;
	}

	/**
	 * Gets the input state of a joystick in this control state
	 *
	 * @param index
	 *            the joystick index to check
	 * @return the X, Y coordinates of the joystick [-1, 1]
	 */
	public Vector2 getStick(int index) {
		return sticks[index];
	}

	/**
	 * Gets all the buttons states in this input states
	 *
	 * @return all the registered buttons
	 */
	public Collection<InputButton> getButtons() {
		return buttons.keySet();
	}
}
