package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.Robot;

public abstract class AutoProgram {
	abstract void run(Robot rob);

	public void start(Robot rob) {
		this.run(rob);
	}
}
