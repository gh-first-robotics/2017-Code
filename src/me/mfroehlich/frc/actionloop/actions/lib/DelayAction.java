package me.mfroehlich.frc.actionloop.actions.lib;

import me.mfroehlich.frc.actionloop.actions.Action;

public class DelayAction extends Action {
	private final int millis;
	
	private long delayEnd;
	
	public DelayAction(int millis) {
		super("Delay " + millis + " millis");
		this.millis = millis;
	}
	
	@Override
	protected void before() {
		delayEnd = System.currentTimeMillis() + this.millis;
	}
	
	@Override
	protected void update() {
		if (System.currentTimeMillis() > delayEnd) {
			this.complete();
		}
	}

	@Override
	protected void abort() { }
}
