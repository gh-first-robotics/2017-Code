package me.mfroehlich.frc.abstractions;

public interface Talon {
	public void control(ControlMode mode);
	
	public void follow(Talon other);
	
	public void set(double value);
	
	public int getEncoderPosition();
	public void setEncoderPosition(int value);
	
	public enum ControlMode {
		POWER,
		SPEED,
		POSITION,
	}
	
	public static Talon create(int channel) {
		return EnvironmentManager.current.createTalon(channel);
	}
}
