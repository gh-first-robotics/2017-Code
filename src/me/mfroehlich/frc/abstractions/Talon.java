package me.mfroehlich.frc.abstractions;

import com.ctre.CANTalon.FeedbackDevice;

public interface Talon {
	public void control(ControlMode mode);
	
	public void follow(Talon other);
	
	public void set(double value);
	
	public int getEncoderPosition();
	public void setEncoderPosition(int value);
	
	public void setSetpoint(double value);
	public void setFeedbackDevice(FeedbackDevice device);
	public void configEncoderCodesPerRev(int codesPerRev);
	public void setPID(double P, double I, double D);
	public double getPosition();
	public void setPosition(double position);
	public void setAllowableClosedLoopErr(int err);
	
	public enum ControlMode {
		POWER,
		SPEED,
		POSITION,
	}
	
	public static Talon create(int channel) {
		return EnvironmentManager.current.createTalon(channel);
	}
}
