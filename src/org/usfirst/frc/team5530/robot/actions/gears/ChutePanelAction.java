package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.systems.GearChuteSystem;

import me.mfroehlich.frc.abstractions.Servo;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class ChutePanelAction extends Action {
	private Servo chute;
	private boolean open;
	
	public ChutePanelAction(boolean open) {
		this.open = open;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		this.chute = scope.require(GearChuteSystem.panel);
	}

	@Override
	public void update() {
		if (this.open) {
			chute.set(.175);
		} else {
			chute.set(1);
		}
		
		this.complete();
	}
	
	@Override
	protected void abort() { }
}
