package me.mfroehlich.frc.actionloop.actions.lib;

import me.mfroehlich.frc.actionloop.actions.Action;

public class PrintAction extends Action {
	private String message;
	
	public PrintAction(String message) {
		super("Print '" + message + "'");
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
