package me.mfroehlich.frc.abstractions;

import me.mfroehlich.frc.eventloop.events.EventSource;

public interface DigitalSensor {
	/**
	 * Gets the changed event that is triggered when the value of the senosr changes
	 * @return the event
	 */
	public EventSource changed();
	
	/**
	 * Gets the state of the break beam
	 * @return returns true if the beam is broken, false if not
	 */
	public boolean value();

	/**
	 * Prints the value of the sensor every second for debugging purposes
	 */
	public void debug();
	
	public static DigitalSensor create(int channel) {
		return EnvironmentManager.current.createDigitalSensor(channel);
	}
}
