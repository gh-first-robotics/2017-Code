package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.systems.LateralSlideSystem;
import org.usfirst.frc.team5530.robot.systems.PegInterfaceSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.eventloop.EventLoopRobot;
import me.mfroehlich.frc.eventloop.actions.Action;
import me.mfroehlich.frc.eventloop.actions.ResourceScope;

public class AlignSlideWithPegAction extends Action {
	private Talon slide;
	
	private boolean hasPreDetected;
	
	@Override
	public void init(ResourceScope scope) {
		this.slide = scope.require(LateralSlideSystem.motor);
		
//		listen(PegInterfaceSystem.limitSwitch.onChange);
//		listen(PegInterfaceSystem.breakBeam.onChange);
		listen(EventLoopRobot.tick);
	}
	
	@Override
	protected void before() {
		hasPreDetected = false;
	}
	
	@Override
	public void update() {
		boolean detected = PegInterfaceSystem.limitSwitch.value();
		boolean preDetected = PegInterfaceSystem.breakBeam.value();
		
		if (detected) {
			slide.set(0);
			this.complete();
		} else if (preDetected || hasPreDetected) {
			slide.set(-.7);
			hasPreDetected = true;
		} else {
			slide.set(-1);
		}
	}
	
	@Override
	protected void abort() {
		slide.set(0);
	}
}
