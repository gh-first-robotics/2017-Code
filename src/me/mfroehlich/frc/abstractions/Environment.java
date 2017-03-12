package me.mfroehlich.frc.abstractions;

import me.mfroehlich.frc.abstractions.Talon.EncoderType;

public interface Environment {
	Gyro createGyro();
	Servo createServo(int channel);
	
	Talon createTalon(int channel, int ticksPerRevolution, EncoderType type);
	
	DigitalSensor createDigitalSensor(int channel);
	
	Controls createControls(int[] stickPorts);
	
	public static void set(Environment impl) {
		EnvironmentManager.current = impl;
	}
	
	public static boolean is(Class<? extends Environment> check) {
		return check.isInstance(EnvironmentManager.current);
	}
}
