package org.usfirst.frc.team5530.robot;

import java.util.function.Consumer;

import org.usfirst.frc.team5530.robot.system.IterativeSystem;

import edu.wpi.first.wpilibj.Timer;

public class AutonomousSchedule {
	private Consumer<AutonomousTimer> action;
	private IterativeSystem[] iterativeSystems;
	private int iterationDelay;

	public AutonomousSchedule(Consumer<AutonomousTimer> action) {
		this.action = action;
	}

	/**
	 * Executes the schedule
	 *
	 * @param delay
	 *            the iterationDelay between iterative system updates during
	 *            delays
	 * @param systems
	 *            all iterative iterativeSystems to update
	 */
	public void execute(int delay, IterativeSystem... systems) {
		this.iterationDelay = delay;
		this.iterativeSystems = systems;
		this.action.accept(this::delay);
	}

	/**
	 * Implementation of {@link AutonomousTimer#delay(int)}
	 */
	private void delay(int millis) {
		long end = System.currentTimeMillis() + millis;
		while (System.currentTimeMillis() < end) {
			for (IterativeSystem system : iterativeSystems)
				system.update();
			if (System.currentTimeMillis() < end)
				Timer.delay(iterationDelay / 1000.0);
		}
	}

	@FunctionalInterface
	public interface AutonomousTimer {
		/**
		 * Updates all {@link IterativeSystem} objects supplied in the execute
		 * method
		 *
		 * @param delay
		 *            The time to spend iterating
		 */
		void delay(int delay);

	}
}
