package me.mfroehlich.frc.abstractions;

public interface Servo {
	public void set(double value);
	public double get();
	
	public static Servo create(int channel) {
		return EnvironmentManager.current.createServo(channel);
	}
}
