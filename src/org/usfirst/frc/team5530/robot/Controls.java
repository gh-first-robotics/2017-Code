package org.usfirst.frc.team5530.robot;

import java.util.Collection;
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

  public static class ControlState {
    private HashMap<InputButton, Boolean> buttons = new HashMap<>();
    private Vector2[] sticks;

    public ControlState(Vector2[] sticks, HashMap<InputButton, Boolean> buttons) {
      this.buttons = buttons;
      this.sticks = sticks;
    }

    private ControlState(int size) {
      sticks = new Vector2[size];
    }

    /**
     * Gets the input state of a button in this control state
     *
     * @param button the button to check
     * @return true if the button is pressed
     */
    public boolean isPressed(InputButton button) {
      return buttons.get(button);
    }

    /**
     * Checks whether a button was pressed between two input states
     *
     * @param button   the button to check the status of
     * @param previous the control state to compare to
     * @return true if the button is pressed in this state but not the
     * previous
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
     * @param index the joystick index to check
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
}
