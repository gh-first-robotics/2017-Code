package me.mfroehlich.frc.abstractions.live;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;
import me.mfroehlich.frc.abstractions.Gyro;

class LiveGyro implements Gyro {
	private AHRS ahrs;
	
	public LiveGyro() {
		try {
			ahrs = new AHRS(SPI.Port.kMXP);
		}catch (RuntimeException e) {
			DriverStation.reportError(e.getMessage(), false);
		}
	}
	
	public AHRS getAHRS() {
		return this.ahrs;
	}

	@Override
	public double getAngle() {
		return ahrs.getAngle();
	}

	@Override
	public void resetAngle() {
		ahrs.reset();
	}
}
