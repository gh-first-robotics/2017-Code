package me.mfroehlich.frc.eventloop.actions.lib;

import me.mfroehlich.frc.eventloop.actions.Action;

public class PrintAction extends Action {
	private String message;
	
	public PrintAction(String message) {
		this.message = message;
	}
	
	@Override
	protected void update() {
		System.out.println(message);
		this.complete();
	}

	@Override
	protected void abort() { }
}
