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
	
	public AlignSlideWithPegAction() {
		super("Align slide with peg");
	}
	
	@Override
	public void init(ResourceScope scope) {
		this.slide = scope.require(LateralSlideSystem.motor);
		
		detected = PegInterfaceSystem.limitSwitch.observe(true);
		preDetected = PegInterfaceSystem.breakBeam.observe(true);
	}
	
	@Override
	protected void before() {
		if (!LateralSlideSystem.isCalibrated) {
			System.err.println("Lateral slide system is not calibrated. Aborting peg alignment...");
			this.complete();
			return;
		}
		
		detected.reset();
		preDetected.reset();
	}
	
	@Override
	public void update() {
		if (!LateralSlideSystem.isCalibrated) {
			return;
		}
		
		boolean maximum = slide.getEncoderPosition() >= LateralSlideSystem.maximumTicks;
		
		if (maximum) {
			slide.set(0);
			this.cancel();
			System.out.println("Reached end of X");
			return;
		}
		
		if (detected.value()) {
			slide.set(0);
			this.complete();
			System.out.println("Reached limit switch");
			return;
		}
		
		if (preDetected.value()) {
			slide.set(-.7);
			System.out.println("Reached break beam");
			return;
		}

		System.out.println("Searching...");
		slide.set(-1);
	}
	
	@Override
	protected void abort() {
		slide.set(0);
	}
}
