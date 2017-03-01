package me.mfroehlich.frc.abstractions;

import com.kauailabs.navx.frc.AHRS;


public interface Gyro {
	public AHRS getAHRS();
	
	public double getAngle();
	
	public void resetAngle();
	
	public static Gyro create() {
		return EnvironmentManager.current.createGyro();
	}
}
