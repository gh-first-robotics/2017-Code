package org.usfirst.frc.team5530.robot.test;

import me.mfroehlich.frc.abstractions.DigitalSensor;
import me.mfroehlich.frc.actionloop.actions.Action;

public class WaitForDigitalSensorAction extends Action {
	private DigitalSensor sensor;
	boolean targetValue;
	
	public WaitForDigitalSensorAction(DigitalSensor sensor, boolean targetValue) {
		this.sensor = sensor;
		this.targetValue = targetValue;
	}

	@Override
	protected void update() {
		if (this.sensor.value() == targetValue) {
			this.complete();
		}
	}

	@Override
	protected void abort() { }
}
