package me.mfroehlich.frc.abstractions;

public interface Talon {
	public void control(ControlMode mode);
	
	public void follow(Talon other);
	
	public void set(double value);
	
	public int getEncoderPosition();
	public void setEncoderPosition(int value);
	
	public void debug();
	
	public enum ControlMode {
		POWER,
		SPEED,
		POSITION,
	}
	
	public enum EncoderType {
		NONE,
		QUADRATURE,
		MAG_RELATIVE,
	}
	
	public static Talon create(int channel) {
		return create(channel, 0, EncoderType.NONE);
	}
	
	public static Talon create(int channel, int ticksPerRevolution, EncoderType encoder) {
		return EnvironmentManager.current.createTalon(channel, ticksPerRevolution, encoder);
	}
}
