package me.mfroehlich.frc.abstractions.live;

import com.kauailabs.navx.frc.AHRS;

import edu.wpi.first.wpilibj.DriverStation;
import edu.wpi.first.wpilibj.SPI;

import me.mfroehlich.frc.abstractions.Gyro;


class LiveGyro implements Gyro {
	private AHRS ahrs;
	
	public LiveGyro() {
		try {
	          /* Communicate w/navX-MXP via the MXP SPI Bus.                                     */
	          /* Alternatively:  I2C.Port.kMXP, SerialPort.Port.kMXP or SerialPort.Port.kUSB     */
	          /* See http://navx-mxp.kauailabs.com/guidance/selecting-an-interface/ for details. */
	          ahrs = new AHRS(SPI.Port.kMXP); 
	      } catch (RuntimeException ex ) {
	          DriverStation.reportError("Error instantiating navX-MXP:  " + ex.getMessage(), true);
	      }
	}

	@Override
	public double getAngle() {
		return ahrs.getAngle();
	}

	@Override
	public void resetAngle() {
		ahrs.reset();
	}

	@Override
	public AHRS getAHRS() {
		// TODO Auto-generated method stub
		return ahrs;
	}
	
	
	
}
