package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.system.DriveTrain;

public class Other implements AutoProgram {
	@Override
	public void run(Robot rob, int position) {
		DriveTrain driveTrain = rob.getSystem(DriveTrain.class);
		
		driveTrain.drive(.85);
		rob.sleep(2000);
		driveTrain.drive(0);
	}

	@Override
	public String getName() {
		return "Default";
	}
}
