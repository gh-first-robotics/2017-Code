package me.mfroehlich.frc.web;

import me.mfroehlich.frc.actionloop.actions.Action;

public class WebInterfaceEntry {
	public final String category;
	public final Action action;
	
	public WebInterfaceEntry(String category, Action action) {
		this.category = category;
		this.action = action;
	}
}
