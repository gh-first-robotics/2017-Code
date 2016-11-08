package org.usfirst.frc.team5530.robot.autonomous;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.system.LowArm;
import org.usfirst.frc.team5530.robot.system.Shooter;
import org.usfirst.frc.team5530.robot.macros.DriveMacro;
import org.usfirst.frc.team5530.robot.macros.ShootMacro;
import org.usfirst.frc.team5530.robot.macros.TurnMacro;

public class LowBar extends AutoProgram {
	private static final double driveDistance = 20 + 1.0 / 6;
	private static final double distance2 = 6 + 7 / 12;
	private static final double distance3 = 1 + 7 / 12;

	private boolean shoot;

	public LowBar(boolean shoot) {
		this.shoot = shoot;
	}

	@Override
	void run(Robot rob) {
		Shooter shooter = rob.getSystem(Shooter.class);
		LowArm lowArm = rob.getSystem(LowArm.class);

		lowArm.lower();
		rob.sleep(1800);

		rob.execute(new DriveMacro(driveDistance));
		rob.execute(new TurnMacro(-7.5));

		if (this.shoot) {
			rob.execute(new ShootMacro(5 * 12));
		}

		rob.execute(new DriveMacro(distance2));

		lowArm.raise();
		rob.sleep(800);
		lowArm.stop();

		rob.execute(new DriveMacro(distance3));

		if (this.shoot) {
			shooter.launch();

			rob.sleep(1010);

			rob.execute(new DriveMacro(-distance3));
			rob.execute(new DriveMacro(-distance2));

			lowArm.lower();
			rob.sleep(800);
			
			rob.execute(new TurnMacro(3));

			rob.execute(new DriveMacro(driveDistance - 1));
		}
	}
}
