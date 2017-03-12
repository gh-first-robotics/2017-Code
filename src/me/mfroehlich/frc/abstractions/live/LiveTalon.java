package me.mfroehlich.frc.abstractions.live;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;

import edu.wpi.first.wpilibj.Timer;
import me.mfroehlich.frc.abstractions.Talon;

class LiveTalon implements Talon {
	private CANTalon talon;
	
	public LiveTalon(int channel, int ticksPerRevolution, EncoderType type) {
		talon = new CANTalon(channel);
		
		talon.setPID(0.1, 0, 0);
		
		this.feedback(ticksPerRevolution, type);
		
		talon.configNominalOutputVoltage(0, -0);
		talon.configPeakOutputVoltage(12, -12);
	}
	
	private void feedback(int ticksPerRevolution, EncoderType type) {
		switch (type) {
		case NONE: break;
			
		case QUADRATURE:
			talon.configEncoderCodesPerRev(ticksPerRevolution);
			talon.setFeedbackDevice(FeedbackDevice.QuadEncoder);
			break;
			
		case MAG_RELATIVE:
			talon.configEncoderCodesPerRev(ticksPerRevolution);
			talon.setFeedbackDevice(FeedbackDevice.CtreMagEncoder_Relative);
			break;
		}
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
		talon.setEncPosition(value);
	}
	
	@Override
	public void debug() {
		new Thread(() -> {
			while (true) {
				System.out.println(talon.getDeviceID() + ": " + talon.getEncVelocity());
				
				Timer.delay(.5);
			}
		}).start();
	}
}
