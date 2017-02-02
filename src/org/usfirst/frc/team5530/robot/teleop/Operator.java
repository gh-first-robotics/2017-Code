package org.usfirst.frc.team5530.robot.teleop;

import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.macros.ShootMacro;
import org.usfirst.frc.team5530.robot.system.DriveTrain;
import org.usfirst.frc.team5530.robot.system.LowArm;
import org.usfirst.frc.team5530.robot.system.Scaler;
import org.usfirst.frc.team5530.robot.system.Shooter;

import edu.wpi.first.wpilibj.Joystick;

public class Operator {
	private DriveTrain driveTrain;
	private Shooter shooter;
	private LowArm lowArm;
	private Scaler scaler;

	private Robot rob;

	private Controls controls;
	private ControlState state, previousState;

	private boolean enableScale;
	private boolean reverseDriving;

	public Operator(Robot rob, Joystick... sticks) {
		this.driveTrain = rob.getSystem(DriveTrain.class);
		this.shooter = rob.getSystem(Shooter.class);
		this.lowArm = rob.getSystem(LowArm.class);
		this.scaler = rob.getSystem(Scaler.class);

		this.rob = rob;

		this.controls = new Controls(sticks);
	}

	public void tick() {
		previousState = state;
		state = controls.update();
//if (!DriveTrain.autoDrive){
		driveTrain.arcadeDrive(state.getStick(0), reverseDriving);
//		}
		if (enableScale)
			scaler.move(state.getStick(1));

		//if (state.isPressed(InputButton.Raise_Arm))
			//lowArm.raise();
		//else if (state.isPressed(InputButton.Lower_Arm))
			//lowArm.lower();
		//else
		//	lowArm.stop();

		if (state.isNewlyPressed(InputButton.Reverse_Steering, previousState))
			reverseDriving = !reverseDriving;	
		
		if (state.isNewlyPressed(InputButton.Drive_Towards_Target, previousState))
			driveTrain.startDriveToTarget();


		if (state.isNewlyPressed(InputButton.Shoot_Auto, previousState))
			driveTrain.turnTowardsTarget();
		
		if (state.isNewlyPressed(InputButton.Test_Drive_Straight, previousState))
			driveTrain.driveStraight(3,3);
		
		if (state.isNewlyPressed(InputButton.Test_Drive_Distance, previousState))
			driveTrain.driveStraightDistance(5*12);

		if (state.isNewlyPressed(InputButton.Intake, previousState))
			shooter.toggleIntake();

		if (state.isNewlyPressed(InputButton.Shoot_Max, previousState))
			shooter.shootRaw(1);

		if (state.isNewlyPressed(InputButton.Shoot_5, previousState))
			rob.execute(new ShootMacro(5 * 12));

		if (state.isNewlyPressed(InputButton.Shoot_10, previousState))
			rob.execute(new ShootMacro(10 * 12));

		if (state.isNewlyPressed(InputButton.Shoot_Low, previousState))
			shooter.shootLow();

		if (state.isNewlyPressed(InputButton.Toggle_Scaler, previousState))
			enableScale = !enableScale;

		if (state.isNewlyPressed(InputButton.Lock_Scaler, previousState))
			scaler.lock();

		if (state.isNewlyPressed(InputButton.Unlock_Scaler, previousState))
			scaler.unlock();

		if (state.isNewlyPressed(InputButton.Shoot_Finish, previousState))
			shooter.launch();
	}
}
