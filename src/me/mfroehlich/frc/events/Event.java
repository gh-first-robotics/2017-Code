package me.mfroehlich.frc.events;

import java.util.ArrayList;
import java.util.LinkedList;
import java.util.List;

public class Event implements EventSource {
	private List<Callback> callbacks = new ArrayList<>();
	
	@Override
	public void single(Callback cb) {
		listen(new SingleListener(cb));
	}
	
	@Override
	public void listen(Callback cb) {
		callbacks.add(cb);
	}
	
	@Override
	public void remove(Callback cb) {
		callbacks.remove(cb);
	}
	
	public void emit() {
		List<Callback> callbacks = new LinkedList<>(this.callbacks);
		
		for (Callback cb : callbacks) {
			cb.invoke();
		}
	}
	
	private class SingleListener implements Callback {
		private Callback callback;
		
		public SingleListener(Callback callback) {
			this.callback = callback;
		}
		
		@Override
		public void invoke() {
			callback.invoke();
			remove(this);
		}
	}
}
