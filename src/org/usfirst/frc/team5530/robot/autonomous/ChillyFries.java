package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.macros.DriveMacro;
import org.usfirst.frc.team5530.robot.macros.ShootMacro;
import org.usfirst.frc.team5530.robot.system.DriveTrain;
import org.usfirst.frc.team5530.robot.system.LowArm;
import org.usfirst.frc.team5530.robot.system.Shooter;

public class ChillyFries extends AutoProgram {
	private static final double distance3 = 1 + 7.0 / 12;
	private static final double distanceToCheval = 6.2;
	private static final double driveDistance = 20 + 1.0 / 6;

	private boolean shoot;

	public ChillyFries(boolean shoot) {
		this.shoot = shoot;
	}

	@Override
	void run(Robot rob) {
		DriveTrain driveTrain = rob.getSystem(DriveTrain.class);
		Shooter shooter = rob.getSystem(Shooter.class);
		LowArm lowArm = rob.getSystem(LowArm.class);

		lowArm.lower();
		rob.sleep(400);
		lowArm.stop();

		rob.execute(new DriveMacro(distanceToCheval));

		lowArm.lower();
		driveTrain.drive(.03);
		rob.sleep(1400);

		rob.execute(new DriveMacro(driveDistance - distanceToCheval));

		if (this.shoot) {
			rob.execute(new ShootMacro(5 * 12));
		}

		lowArm.raise();
		rob.sleep(800);
		lowArm.stop();

		rob.execute(new DriveMacro(distance3));

		if (this.shoot) {
			shooter.launch();
			rob.sleep(1010);
		}
	}
}
