package me.mfroehlich.frc.abstractions;

public interface RobotStateProvider {
	State getState();
	
	public enum State {
		DISABLED,
		
		AUTONOMOUS,
		TELEOP,
		TEST
	}
}
