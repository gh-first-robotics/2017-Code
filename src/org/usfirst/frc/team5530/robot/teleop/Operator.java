package org.usfirst.frc.team5530.robot.teleop;

import org.usfirst.frc.team5530.robot.Robot;

import org.usfirst.frc.team5530.robot.system.DriveTrain;
import org.usfirst.frc.team5530.robot.system.Gear;
import org.usfirst.frc.team5530.robot.system.Scaler;


import edu.wpi.first.wpilibj.Joystick;

public class Operator {
	private DriveTrain driveTrain;
	
	private Gear gear;
	private Scaler scaler;

	private Robot rob;

	private Controls controls;
	private ControlState state, previousState;

	private boolean enableScale;
	private boolean reverseDriving;

	public Operator(Robot rob, Joystick... sticks) {
		this.driveTrain = rob.getSystem(DriveTrain.class);
		
		this.gear = rob.getSystem(Gear.class);
		this.scaler = rob.getSystem(Scaler.class);

		this.rob = rob;

		this.controls = new Controls(sticks);
	}

	public static boolean manualMove = false;
	public void tick() {
		previousState = state;
		state = controls.update();
if (!DriveTrain.autoDrive){
		driveTrain.arcadeDrive(state.getStick(0), reverseDriving);
		}

if (state.isPressed(InputButton.Enable_Gear_Movement)){
	System.out.println("move gear buton pressed");
	gear.manualGearMovement(state.getStick(1));
	manualMove = true;
	}
else{manualMove = false;}
		
		if (state.isNewlyPressed(InputButton.Reverse_Steering, previousState))
			reverseDriving = !reverseDriving;	
		
		if (state.isNewlyPressed(InputButton.Drive_Towards_Target, previousState))
			driveTrain.startDriveToTarget();

		if (state.isNewlyPressed(InputButton.Intake_Gear, previousState))
			gear.intakeGear();

		if (state.isNewlyPressed(InputButton.Place_Gear, previousState))
			gear.placeGear();
		
		if (state.isNewlyPressed(InputButton.Zero_Position, previousState))
			gear.zeroPosition();

		if (state.isNewlyPressed(InputButton.Toggle_Scaler, previousState))
			enableScale = !enableScale;

		if (state.isNewlyPressed(InputButton.Lock_Scaler, previousState))
			scaler.lock();

		if (state.isNewlyPressed(InputButton.Unlock_Scaler, previousState))
			scaler.unlock();

	}
}
