package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.systems.LateralSlideSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.eventloop.actions.Action;
import me.mfroehlich.frc.eventloop.actions.ResourceScope;

public class ResetLateralSlideAction extends Action {
	private Talon slide;
	
	@Override
	protected void init(ResourceScope scope) {
		slide = scope.require(LateralSlideSystem.motor);

		listen(LateralSlideSystem.homeSwitch.changed());
	}
	
	@Override
	protected void before() {
		slide.control(ControlMode.POWER);
	}

	@Override
	public void update() {
		boolean isHome = LateralSlideSystem.homeSwitch.value();

		if (isHome) {
			slide.set(0);
			slide.setEncoderPosition(0);
			LateralSlideSystem.isCalibrated = true;
			System.out.println("Lateral home");
			this.complete();
		} else {
			System.out.println("Lateral not home");
			slide.set(.4);
		}
	}
	
	@Override
	protected void abort() {
		slide.set(0);
	}
}
