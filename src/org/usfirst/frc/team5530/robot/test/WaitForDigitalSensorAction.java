package org.usfirst.frc.team5530.robot.test;

import me.mfroehlich.frc.abstractions.DigitalSensor;
import me.mfroehlich.frc.eventloop.actions.Action;
import me.mfroehlich.frc.eventloop.actions.ResourceScope;

public class WaitForDigitalSensorAction extends Action {
	private DigitalSensor sensor;
	
	public WaitForDigitalSensorAction(DigitalSensor sensor) {
		this.sensor = sensor;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		listen(this.sensor.changed());
	}

	@Override
	protected void update() {
		System.out.println("Waiting updated " + this.sensor.value());
		if (this.sensor.value()) {
			this.complete();
		}
	}

	@Override
	protected void abort() { }
}
