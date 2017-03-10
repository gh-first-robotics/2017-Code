package me.mfroehlich.frc.abstractions.live;

import me.mfroehlich.frc.abstractions.Controls;
import me.mfroehlich.frc.abstractions.DigitalSensor;
import me.mfroehlich.frc.abstractions.Environment;
import me.mfroehlich.frc.abstractions.Gyro;
import me.mfroehlich.frc.abstractions.Servo;
import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.EncoderType;

public class LiveEnvironment implements Environment {
	@Override
	public Servo createServo(int channel) {
		return new LiveServo(channel);
	}
	
	@Override
	public Talon createTalon(int channel, int ticksPerRevolution, EncoderType encoder) {
		return new LiveTalon(channel, ticksPerRevolution, encoder);
	}

	@Override
	public DigitalSensor createDigitalSensor(int channel) {
		return new LiveDigitalSensor(channel);
	}
	
	@Override
	public Controls createControls(int[] stickPorts) {
		return new LiveControls(stickPorts);
	}
	
	@Override
	public Gyro createGyro() {
		return new LiveGyro();
	}
}
