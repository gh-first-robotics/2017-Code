package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.system.DriveTrain;

public class Other extends AutoProgram {
	@Override
	void run(Robot rob) {
		DriveTrain driveTrain = rob.getSystem(DriveTrain.class);
		
		driveTrain.drive(.85);
		rob.sleep(2000);
		driveTrain.drive(0);
	}
}
