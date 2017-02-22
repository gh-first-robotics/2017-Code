package me.mfroehlich.frc.abstractions;

public interface DigitalSensor {
	/**
	 * Gets the state of the break beam
	 * @return returns true if the beam is broken, false if not
	 */
	public boolean value();
	
	public Observer observe(boolean target);
	
	/**
	 * Prints the value of the sensor every second for debugging purposes
	 */
	public void debug();
	
	public static DigitalSensor create(int channel) {
		return EnvironmentManager.current.createDigitalSensor(channel);
	}
	
	public interface Observer {
		boolean value();
		void reset();
	}
}
