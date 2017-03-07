package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.systems.PegInterfaceSystem;

import me.mfroehlich.frc.abstractions.Servo;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class PegGripperAction extends Action {
	private Servo gripper;
	private Position open;
	
	public PegGripperAction(Position open) {
		super(open.name() + " peg gripper");
		this.open = open;
	}
	
	@Override
	protected void init(ResourceScope scope) {
		this.gripper = scope.require(PegInterfaceSystem.gripper);
	}

	@Override
	public void update() {
		switch (open) {
		case OPEN:
			gripper.set(.02);
			break;
			
		case HALF:
			gripper.set(.5);
			break;
			
		case CLOSED:
			gripper.set(1);
			break;
		}
		
		this.complete();
	}
	
	@Override
	protected void abort() { }
	
	public enum Position {
		OPEN,
		HALF,
		CLOSED,
	}
}
