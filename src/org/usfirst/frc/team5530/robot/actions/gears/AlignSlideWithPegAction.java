package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.systems.LateralSlideSystem;
import org.usfirst.frc.team5530.robot.systems.PegInterfaceSystem;

import me.mfroehlich.frc.abstractions.DigitalSensor.Observer;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;
import me.mfroehlich.frc.abstractions.Talon;

public class AlignSlideWithPegAction extends Action {
	private Talon slide;
	private Observer detected;
	private Observer preDetected;
	
	@Override
	public void init(ResourceScope scope) {
		this.slide = scope.require(LateralSlideSystem.motor);
		
		detected = PegInterfaceSystem.limitSwitch.observe(true);
		preDetected = PegInterfaceSystem.breakBeam.observe(true);
	}
	
	@Override
	protected void before() {
		detected.reset();
		preDetected.reset();
	}
	
	@Override
	public void update() {
		boolean maximum = slide.getEncoderPosition() >= LateralSlideSystem.maximumTicks;
		
		if (maximum) {
			slide.set(0);
			this.cancel();
			return;
		}
		
		if (detected.value()) {
			slide.set(0);
			this.complete();
			return;
		}
		
		if (preDetected.value()) {
			slide.set(-.7);
			return;
		}
		
		slide.set(-1);
	}
	
	@Override
	protected void abort() {
		slide.set(0);
	}
}
