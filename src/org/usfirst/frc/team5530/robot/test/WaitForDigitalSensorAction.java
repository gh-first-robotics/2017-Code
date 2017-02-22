package org.usfirst.frc.team5530.robot.test;

import me.mfroehlich.frc.abstractions.DigitalSensor;
import me.mfroehlich.frc.actionloop.actions.Action;

public class WaitForDigitalSensorAction extends Action {
	private DigitalSensor sensor;
	
	public WaitForDigitalSensorAction(DigitalSensor sensor) {
		this.sensor = sensor;
	}

	@Override
	protected void update() {
		if (this.sensor.value()) {
			this.complete();
		}
	}

	@Override
	protected void abort() { }
}
