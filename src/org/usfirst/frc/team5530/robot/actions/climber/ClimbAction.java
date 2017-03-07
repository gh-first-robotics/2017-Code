package org.usfirst.frc.team5530.robot.actions.climber;

import org.usfirst.frc.team5530.robot.systems.ClimberSystem;

import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.ResourceScope;

public class ClimbAction extends Action {
	private Talon climber;
	private double power;
	
	public ClimbAction(double power) {
		super("Climb at " + (power * 100) + "% power");
		this.power = Math.max(power, 0);
	}
	
	@Override
	protected void init(ResourceScope scope) {
		climber = scope.require(ClimberSystem.motor);
	}
	
	@Override
	protected void before() {
		climber.set(power);
	}
	
	@Override
	protected void update() { }
	
	@Override
	protected void after() {
		climber.set(0);
	}

	@Override
	protected void abort() {
		climber.set(0);
	}
}
