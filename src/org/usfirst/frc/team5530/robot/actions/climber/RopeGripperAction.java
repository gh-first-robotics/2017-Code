package org.usfirst.frc.team5530.robot.actions.climber;

import org.usfirst.frc.team5530.robot.systems.ClimberSystem;

import me.mfroehlich.frc.abstractions.Servo;
import me.mfroehlich.frc.eventloop.actions.Action;
import me.mfroehlich.frc.eventloop.actions.ResourceScope;

public class RopeGripperAction extends Action {
	private Servo top, bottom;
	private boolean open;
	
	public RopeGripperAction(boolean open) {
		this.open = open;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		this.top = scope.require(ClimberSystem.topGripper);
		this.bottom = scope.require(ClimberSystem.bottomGripper);
	}

	@Override
	public void update() {
		if (this.open) {
			top.set(.5);
			bottom.set(.5);
		} else {
			top.set(0);
			bottom.set(0);
		}
		
		this.complete();
	}
	
	@Override
	protected void abort() { }

}
