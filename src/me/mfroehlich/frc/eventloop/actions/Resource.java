package me.mfroehlich.frc.eventloop.actions;

public class Resource<T> {
	private T value;
	private ResourceScope owner;
	
	public Resource(T value) {
		this.value = value;
	}
	
	/**
	 * Get the value of the dependency.
	 * @return the dependency
	 */
	T get() {
		return this.value;
	}

	/**
	 * Locks the dependency, registering an action as the current consumer.
	 * @param owner the action currently using the consumer
	 */
	boolean lock(ResourceScope owner) {
		if (this.owner == owner) {
			return true;
		}
		
		if (this.owner != null) {
			this.owner.cancel();
			return false;
		}
		
		this.owner = owner;
		return true;
	}
	
	/**
	 * Releases the dependency, making it available to be locked again
	 */
	void release() {
		this.owner = null;
	}
}
