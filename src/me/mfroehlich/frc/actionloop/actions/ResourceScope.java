package me.mfroehlich.frc.actionloop.actions;

import java.util.HashSet;
import java.util.Set;

public class ResourceScope {
	private Set<Resource<?>> dependencies = new HashSet<>();
	private Action owner;
	
	public ResourceScope(Action scope) {
		this.owner = scope;
	}
	
	public <T> T require(Resource<T> t) {
		this.dependencies.add(t);
		return t.get();
	}
	
	boolean lock() {
		boolean success = true;
		for (Resource<?> dep : dependencies) {
			success = success && dep.lock(this);
		}
		return success;
	}
	
	void release() {
		for (Resource<?> dep : dependencies) {
			dep.release();
		}
	}
	
	void cancel() {
		System.err.println("ERROR: ABORTING ACTION" + owner.getClass().getSimpleName());
		owner.cancel();
	}
}
