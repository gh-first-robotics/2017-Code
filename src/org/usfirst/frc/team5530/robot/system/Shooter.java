package org.usfirst.frc.team5530.robot.system;

import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import com.ctre.CANTalon;

public class Shooter implements RobotSystem {
	private enum ShooterState {
		INTAKE_POSITIONING, INTAKE_WAITING, OFF, SHOOTING, SHOOTING_LOW
	}

	private static final int intakeDuration = 1000;

	private CANTalon intakeTalon;
	private DigitalInput intakeSwitch1;
	private DigitalInput intakeSwitch2;

	private CANTalon shooterTalon1;
	private CANTalon shooterTalon2;

	private ShooterState state = ShooterState.OFF;
	private double shootSpeed;
	private long shootEnd;
	public boolean launching;

	public Shooter(CANTalon intake, DigitalInput switch1, DigitalInput switch2, CANTalon shooter1, CANTalon shooter2) {
		intakeTalon = intake;
		intakeSwitch1 = switch1;
		intakeSwitch2 = switch2;
		shooterTalon1 = shooter1;
		shooterTalon2 = shooter2;
	}

	/**
	 * If the shooter is currently inactive, starts intaking otherwise it stops
	 * the shooter
	 */
	public void toggleIntake() {
		if (state == ShooterState.OFF)
			state = ShooterState.INTAKE_WAITING;
		else
			state = ShooterState.OFF;
	}

	/**
	 * Shoots the ball based on a raw speed
	 * 
	 * @param speed
	 *            the speed to shoot at [-1, 1]
	 */
	public void shootRaw(double speed) {
		shootSpeed = speed;
		state = ShooterState.SHOOTING;
	}

	public void launch() {
		shootEnd = System.currentTimeMillis() + intakeDuration;
		launching = true;
		// System.out.println(launching);
	}

	/**
	 * Shoots the ball out through the intake
	 */
	public void shootLow() {
		shootEnd = System.currentTimeMillis() + 1000;
		state = ShooterState.SHOOTING_LOW;
	}

	/**
	 * Call repeatedly to set the speeds of the shooter and intake wheels
	 */
	@Override
	public void update() {
		// System.out.println(launching);
		double intakeSpeed = 0, shooterSpeed = 0;

		long shootRemaining = shootEnd - System.currentTimeMillis();
		// Inverted so switches[] is true if the ball is present at that
		// detector
		boolean[] switches = { !intakeSwitch1.get(), !intakeSwitch2.get() };

		if (state == ShooterState.INTAKE_WAITING) {
			if (switches[0] || switches[1])
				state = ShooterState.INTAKE_POSITIONING;
			else
				intakeSpeed = .5;
		}

		if (state == ShooterState.INTAKE_POSITIONING) {
			if (!switches[0] && !switches[1]) {
				state = ShooterState.OFF;
				intakeSpeed = 0;
			} else if (!switches[0] && switches[1]) {
				intakeSpeed = -.15;
			} else if (switches[0] && !switches[1]) {
				intakeSpeed = .15;
			}
		}

		if (state == ShooterState.SHOOTING) {
			shooterSpeed = shootSpeed;
			if (launching) {
				if (shootRemaining < 0) {
					state = ShooterState.OFF;
					launching = false;
				} else {
					intakeSpeed = 1;
				}
			}
		}

		if (state == ShooterState.SHOOTING_LOW) {
			if (shootRemaining < 0)
				state = ShooterState.OFF;
			intakeSpeed = -1;
		}

		SmartDashboard.putString("Shooter State", state.toString());

		shooterTalon1.set(-shooterSpeed);
		shooterTalon2.set(shooterSpeed);
		intakeTalon.set(intakeSpeed);
	}
}
