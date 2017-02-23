package me.mfroehlich.frc.events;

public interface EventSource {
	public void single(Callback cb);
	public void listen(Callback cb);
	public void remove(Callback cb);
}
