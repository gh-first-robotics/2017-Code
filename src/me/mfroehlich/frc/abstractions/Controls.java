package me.mfroehlich.frc.abstractions;

import me.mfroehlich.frc.controls.ControlsState;

public interface Controls {
	/**
	 * Polls the joysticks for the current state 
	 * @return the current state
	 */
	public ControlsState getState();

	public static Controls create(int... stickPorts) {
		return EnvironmentManager.current.createControls(stickPorts);
	}
}
