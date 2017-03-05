package org.usfirst.frc.team5530.robot.actions.gears;

import org.usfirst.frc.team5530.robot.FindLocation;
import org.usfirst.frc.team5530.robot.Util;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.Talon.ControlMode;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class UnloadGear extends Action {
	
	
	public UnloadGear() {
		
	}
	
	@Override
	protected void init(ResourceScope scope) {
		
	}
	
	@Override
	protected void before() {
		Action.inSequence(
				new AlignSlideWithPegAction(),
				Action.inParallal(
					new ChutePanelAction(false),
					new PegGripperAction(Position.CLOSED)
				),
				new RotateGearAction(),
				
				new PositionAxialSlideAction(4.5, true),
				new PegGripperAction(Position.OPEN),
				new PositionAxialSlideAction(8.5)
				);
	}

	@Override
	public void update() {
		
			complete();	
	}
	
	@Override
	protected void abort() {
		
	}
}
