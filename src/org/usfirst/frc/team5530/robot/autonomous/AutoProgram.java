package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.Robot;

public interface AutoProgram {
	String getName();
	void run(Robot rob, int position);
}
