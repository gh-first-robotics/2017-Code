package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.systems.PegInterfaceSystem;

import me.mfroehlich.frc.abstractions.Servo;
import me.mfroehlich.frc.eventloop.actions.Action;
import me.mfroehlich.frc.eventloop.actions.ResourceScope;

public class PegGripperAction extends Action {
	private Servo gripper;
	private Position open;
	
	public PegGripperAction(Position open) {
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
			gripper.set(0);
			break;
			
		case HALF:
			gripper.set(.5);
			break;
			
		case CLOSED:
			gripper.set(1);
			break;
		}
		
		System.out.println("Peg gripper: " + gripper.get());
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
