package org.usfirst.frc.team5530.robot.system;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;


public class Gear implements RobotSystem{
	int x = 5, //talonX
		y = 6, //talonY
		bB = 0, //breakBeam
		lS = 1, //stopSwitch
		g = 3; //gripper arm
	
	
	private CANTalon talonX = new CANTalon(x);
	private CANTalon talonY = new CANTalon(y);
	private DigitalInput breakBeam = new DigitalInput(bB);
	private DigitalInput stopSwitch= new DigitalInput(lS);
	private Servo gripper = new Servo(g);
	
	boolean first = true;
	double forward_position = 10;
	double  XresetPosition = 0,
			YresetPosition = 0;
	
	boolean beam = breakBeam.get();
	boolean limit = stopSwitch.get();
	double 	fast_speed = 0.8,
			slow_speed = 0.3,
			gripperClosedAngle = 90,
			gripperOpenAngle = 0;
	int error = 5;
	private enum GearState {
		OFF, MOVING_FAST, MOVING_SLOW, ARM_DEPLOYING, MOVING_FORWARD, ARM_RELEASING, RESETTING 
	}
	
	private GearState state = GearState.OFF;
	
	public Gear() {
		talonX.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talonX.reverseSensor(false);
		talonX.configEncoderCodesPerRev(4);
		talonX.setPID(0.5, 0.001, 0.0);
		talonX.setAllowableClosedLoopErr(error);
		
		talonY.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talonY.reverseSensor(false);
		talonY.configEncoderCodesPerRev(4);
		talonY.setPID(0.5, 0.001, 0.0);
		talonY.setAllowableClosedLoopErr(error);
	}
	
	
	public void placeGear(){
		talonX.changeControlMode(TalonControlMode.PercentVbus);
		talonY.changeControlMode(TalonControlMode.PercentVbus);
		talonX.set(fast_speed);
		state = GearState.MOVING_FAST;
	}
	
	public void reset(){
		state = GearState.RESETTING;
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		talonX.set(0);
		talonY.set(0);
		if ((state== GearState.MOVING_FAST) && !beam && !limit){
			talonX.set(fast_speed);
		}
		else if (state == GearState.MOVING_FAST && beam){
			state = GearState.MOVING_SLOW;
		}
		if (state == GearState.MOVING_SLOW && !limit){
			talonX.set(slow_speed);
		}
		if (limit && ((state == GearState.MOVING_SLOW) || (state == GearState.MOVING_FAST))){
			state = GearState.ARM_DEPLOYING;
		}
		if (state == GearState.ARM_DEPLOYING){
			gripper.setAngle(gripperClosedAngle);
		}
		if (state == GearState.ARM_DEPLOYING && gripper.getAngle()==gripperClosedAngle){
			state = GearState.MOVING_FORWARD;
		}
		if (state == GearState.MOVING_FORWARD){
			if (first){
				talonY.changeControlMode(TalonControlMode.Position);
				talonY.setSetpoint(forward_position);
				first = false;
			}
		}
		if (state == GearState.MOVING_FORWARD && Math.abs(talonY.getPosition() - forward_position) < error){
			state = GearState.ARM_RELEASING;
		}
		if (state == GearState.ARM_RELEASING){
			gripper.setAngle(gripperOpenAngle);
		}
		if (state == GearState.RESETTING){
			gripper.setAngle(gripperOpenAngle);
			
			talonY.changeControlMode(TalonControlMode.Position);
			talonY.setSetpoint(YresetPosition);
			
			talonX.changeControlMode(TalonControlMode.Position);
			talonX.setSetpoint(XresetPosition);
			
			first = true;
		}
		if (state == GearState.RESETTING && gripper.getAngle()==gripperOpenAngle && Math.abs(talonY.getPosition() - YresetPosition) < error && Math.abs(talonX.getPosition() - XresetPosition) < error){
			state = GearState.OFF;
		}
	}

}
