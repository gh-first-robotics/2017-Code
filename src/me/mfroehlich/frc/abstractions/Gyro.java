package me.mfroehlich.frc.abstractions;

public interface Gyro {
	public Object getAHRS();
	public double getAngle();
	public void resetAngle();
	
	public static Gyro create() {
		return EnvironmentManager.current.createGyro();
	}
}
