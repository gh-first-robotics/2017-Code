package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.system.LowArm;
import org.usfirst.frc.team5530.robot.macros.DriveMacro;

public class Porticullis extends AutoProgram {
	private static final double distanceToPorticullis = 10;
	private static final double driveDistance = 20 + 1.0 / 6;
	

	@Override
	void run(Robot rob) {
		LowArm lowArm = rob.getSystem(LowArm.class);

		lowArm.lower();
		rob.sleep(1800);
		
		rob.execute(new DriveMacro(distanceToPorticullis));
		
		lowArm.raise();
		rob.sleep(1800);
		
		rob.execute(new DriveMacro(driveDistance - distanceToPorticullis));
		
		lowArm.lower();
	}
}
