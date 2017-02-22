package me.mfroehlich.frc.eventloop;

import me.mfroehlich.frc.eventloop.actions.Action;

public abstract class Controller {
	private EventLoopRobot robot;
	
	public final void initialize(EventLoopRobot rob) {
		this.robot = rob;
		this.init();
	}
	
	protected abstract void init();
	
	public abstract void start();
	public abstract void stop();
	
	public final void execute(Action command) {
		robot.execute(command);
	}
}
