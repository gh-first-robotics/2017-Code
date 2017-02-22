package me.mfroehlich.frc.actionloop;

import me.mfroehlich.frc.actionloop.actions.Action;

public abstract class Controller {
	private ActionRobot robot;
	
	public final void initialize(ActionRobot rob) {
		this.robot = rob;
		this.init();
	}
	
	protected abstract void init();
	
	public abstract void start();
	public abstract void tick();
	public abstract void stop();
	
	public final void execute(Action command) {
		robot.execute(command);
	}
}
