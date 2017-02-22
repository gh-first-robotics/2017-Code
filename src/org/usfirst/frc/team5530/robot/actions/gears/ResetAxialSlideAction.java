package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.systems.AxialSlideSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class ResetAxialSlideAction extends Action {
	private Talon slide;
	
	@Override
	protected void init(ResourceScope scope) {
		slide = scope.require(AxialSlideSystem.motor);
	}
	
	@Override
	protected void before() {
		slide.control(ControlMode.POWER);
	}

	@Override
	public void update() {
		boolean isHome = AxialSlideSystem.homeSwitch.value();
		
		if (isHome) {
			slide.set(0);
			slide.setEncoderPosition(0);
			
			AxialSlideSystem.isCalibrated = true;
			System.out.println("Axial home");
			
			this.complete();
		} else {
			slide.set(.4);
		}
	}
	
	@Override
	protected void abort() {
		slide.set(0);
	}
}
