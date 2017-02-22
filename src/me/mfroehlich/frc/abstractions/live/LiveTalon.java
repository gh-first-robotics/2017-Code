package me.mfroehlich.frc.abstractions.live;

import com.ctre.CANTalon;
import com.ctre.CANTalon.TalonControlMode;

import me.mfroehlich.frc.abstractions.Talon;

class LiveTalon implements Talon {
	private CANTalon talon;
	
	public LiveTalon(int channel) {
		talon = new CANTalon(channel);
		talon.configEncoderCodesPerRev(1);
	}

	@Override
	public void control(ControlMode mode) {
		switch (mode) {
		case POWER:
			talon.changeControlMode(TalonControlMode.PercentVbus);
			break;
			
		case SPEED:
			talon.changeControlMode(TalonControlMode.Speed);
			break;
			
		case POSITION:
			talon.changeControlMode(TalonControlMode.Position);
			break;
		}
	}

	@Override
	public void follow(Talon other) {
		LiveTalon live = (LiveTalon) other;
		talon.changeControlMode(TalonControlMode.Follower);
		talon.set(live.talon.getDeviceID());
	}

	@Override
	public void set(double value) {
		talon.set(value);
	}

	@Override
	public int getEncoderPosition() {
		return talon.getEncPosition();
	}
	
	@Override
	public void setEncoderPosition(int value) {
		talon.setEncPosition(0);
	}
}
