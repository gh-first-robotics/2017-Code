package me.mfroehlich.frc.abstractions;

public interface Environment {
	Servo createServo(int channel);
	Talon createTalon(int channel);
	Gyro  createGyro();
	DigitalSensor createDigitalSensor(int channel);
	
	Controls createControls(int[] stickPorts);
	
	public static void set(Environment impl) {
		EnvironmentManager.current = impl;
	}
	
}
