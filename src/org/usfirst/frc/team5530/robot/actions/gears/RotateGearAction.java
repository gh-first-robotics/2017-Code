package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.systems.GearChuteSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.eventloop.actions.Action;
import me.mfroehlich.frc.eventloop.actions.ResourceScope;

public class RotateGearAction extends Action {
	private Talon rotator;
	
	@Override
	protected void init(ResourceScope scope) {
		rotator = scope.require(GearChuteSystem.rotator);
		
		listen(GearChuteSystem.gearSensor.changed());
		listen(GearChuteSystem.leftSensor.changed());
		listen(GearChuteSystem.rightSensor.changed());
		listen(GearChuteSystem.middleSensor.changed());
	}

	@Override
	public void update() {
		boolean hasGear = GearChuteSystem.gearSensor.value();
		
		if (!hasGear) {
			rotator.set(0);
			return;
		}
		
		boolean left = GearChuteSystem.leftSensor.value();
		boolean right = GearChuteSystem.rightSensor.value();
		boolean middle = GearChuteSystem.middleSensor.value();
		
		if (!left && middle && !right) {
			rotator.set(0);
			this.complete();
		} else {
			rotator.set(1);
		}
	}
	
	@Override
	protected void abort() {
		rotator.set(0);
	}
}
