package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.systems.AxialSlideSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.eventloop.EventLoopRobot;
import me.mfroehlich.frc.eventloop.actions.Action;
import me.mfroehlich.frc.eventloop.actions.ResourceScope;

public class PositionAxialSlideAction extends Action {
	private Talon slide;
	private int position;
	
	public PositionAxialSlideAction(double pos) {
		this.position = (int) Math.floor(pos * AxialSlideSystem.ticksPerInch);
	}
	
	@Override
	protected void init(ResourceScope scope) {
		slide = scope.require(AxialSlideSystem.motor);
		
		listen(EventLoopRobot.tick);
	}
	
	@Override
	protected void before() {
		if (!AxialSlideSystem.isCalibrated) {
			System.err.println("Axial slide is not calibrated. Aborting positioning...");
			this.complete();
			return;
		}
		
		slide.control(ControlMode.POWER);
	}
	
	@Override
	protected void update() {
		int current = slide.getEncoderPosition();
		int delta = position - current;
		int sign = delta > 0 ? -1 : 1;
		double value;
		
		System.out.println("Moving y: " + position + " " + current + " " + delta);
		
		if (Math.abs(delta) < 20) {
			slide.set(0);
			this.complete();
			return;
		} else if (Math.abs(delta) < 350) {
			value = .2;
		} else {
			value = 1;
		}
		
		slide.set(sign * value);
	}
	
	@Override
	protected void after() { }

	@Override
	protected void abort() {
		slide.set(0);
	}
}
