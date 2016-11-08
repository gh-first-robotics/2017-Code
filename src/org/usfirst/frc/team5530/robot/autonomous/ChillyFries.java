package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.macros.DriveMacro;
import org.usfirst.frc.team5530.robot.macros.ShootMacro;
import org.usfirst.frc.team5530.robot.system.DriveTrain;
import org.usfirst.frc.team5530.robot.system.LowArm;
import org.usfirst.frc.team5530.robot.system.Shooter;

public class ChillyFries implements AutoProgram {
	private static final double distance3 = 1 + 7.0 / 12;
	private static final double distanceToCheval = 6.2;
	private static final double driveDistance = 20 + 1.0 / 6;

	@Override
	public void run(Robot rob, int position) {
		DriveTrain driveTrain = rob.getSystem(DriveTrain.class);
		Shooter shooter = rob.getSystem(Shooter.class);
		LowArm lowArm = rob.getSystem(LowArm.class);
		
		boolean shoot = position == 1;

		lowArm.lower();
		rob.sleep(400);
		lowArm.stop();

		rob.execute(new DriveMacro(distanceToCheval));

		lowArm.lower();
		driveTrain.drive(.03);
		rob.sleep(1400);

		rob.execute(new DriveMacro(driveDistance - distanceToCheval));

		if (shoot) {
			rob.execute(new ShootMacro(5 * 12));
		}

		lowArm.raise();
		rob.sleep(800);
		lowArm.stop();

		rob.execute(new DriveMacro(distance3));

		if (shoot) {
			shooter.launch();
			rob.sleep(1010);
		}
	}

	@Override
	public String getName() {
		return "Cheval de Frise";
	}
}
