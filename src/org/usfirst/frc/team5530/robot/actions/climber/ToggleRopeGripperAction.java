package org.usfirst.frc.team5530.robot.actions.climber;

import org.usfirst.frc.team5530.robot.systems.ClimberSystem;

import me.mfroehlich.frc.abstractions.Servo;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class ToggleRopeGripperAction extends Action {
	private Servo top, bottom;
	
	public ToggleRopeGripperAction() {
		super("Toggle rope gripper");
	}
	
	@Override
	protected void init(ResourceScope scope) {
		this.top = scope.require(ClimberSystem.topGripper);
		this.bottom = scope.require(ClimberSystem.bottomGripper);
	}

	@Override
	public void update() {
		if (top.get() == 0) {
			top.set(.85);
			bottom.set(1);
		} else {
			top.set(0);
			bottom.set(0);
		}
		
		this.complete();
	}
	
	@Override
	protected void abort() { }

}
