package me.mfroehlich.frc.abstractions.live;

import me.mfroehlich.frc.abstractions.Servo;

class LiveServo implements Servo {
	private edu.wpi.first.wpilibj.Servo servo;
	
	public LiveServo(int channel) {
		servo = new edu.wpi.first.wpilibj.Servo(channel);
	}
	
	@Override
	public void set(double value) {
		servo.set(value);
	}
	
	@Override
	public double get() {
		return servo.get();
	}
}
