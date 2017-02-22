package me.mfroehlich.frc.actionloop.actions.lib;

import me.mfroehlich.frc.actionloop.actions.Action;

public class DelayAction extends Action {
	private int millis;
	private Thread thread;
	
	public DelayAction(int millis) {
		this.millis = millis;
	}
	
	private void run() {
		try {
			Thread.sleep(millis);

			this.complete();
		} catch (InterruptedException e) {
			System.err.println("Delay cancelled");
		}
	}
	
	@Override
	protected void before() {
		thread = new Thread(this::run);
		thread.start();
	}
	
	@Override
	protected void update() { }

	@Override
	protected void abort() {
		thread.interrupt();
	}
}
