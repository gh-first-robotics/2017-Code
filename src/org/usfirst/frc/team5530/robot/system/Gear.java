package org.usfirst.frc.team5530.robot.system;


import edu.wpi.first.wpilibj.DigitalInput;
import edu.wpi.first.wpilibj.Servo;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;

import org.usfirst.frc.team5530.robot.teleop.InputButton;
import org.usfirst.frc.team5530.robot.teleop.Operator;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import com.ctre.CANTalon;
import com.ctre.CANTalon.FeedbackDevice;
import com.ctre.CANTalon.TalonControlMode;


public class Gear implements RobotSystem{
	int x = 5, //talonX
		y = 6, //talonY
		r = 4, //rotates gear (talonR)
		bB = 7, //breakBeam
		lS = 2, //stopSwitch
		g = 1,  //gripper arm
		chute_panel = 4,
		gearInRobot = 6,
		xhome = 0,
		yhome = 1,
		ab1 = 3,
		ab2 = 4,
		ab3 = 5; 
	
	//break beam is true when beam is not broken
	private CANTalon talonX = new CANTalon(x);
	private CANTalon talonY = new CANTalon(y);
	private CANTalon talonR = new CANTalon(r);
	private DigitalInput breakBeam = new DigitalInput(bB);
	private DigitalInput stopSwitch= new DigitalInput(lS);
	private DigitalInput adjustBeam1 = new DigitalInput(ab1);
	private DigitalInput adjustBeam2 = new DigitalInput(ab2);
	private DigitalInput adjustBeam3 = new DigitalInput(ab3);
	private DigitalInput gear = new DigitalInput(gearInRobot);
	private DigitalInput xHomeSwitch = new DigitalInput(xhome);
	private DigitalInput yHomeSwitch = new DigitalInput(yhome);
	private Servo gripper = new Servo(g);
	private Servo chutePanel = new Servo(chute_panel);
	
	boolean first = true;

	double  XresetPosition = 0,
			YresetPosition = 0,
			XforwardPosition = 7.5/*inches*/ * 0.0499/*inches per rotation*/,
			YloadingPosition = 6.56 /*inches*/ * 0.0499/*inches per rotation*/,
			YforwardPosition = 9.5/*inches*/ * 0.0499/*inches per rotation*/,
			YreleaseGripperPosition = 7.5/*inches*/ * 0.0499/*inches per rotation*/;
	
	//booleans for beams will be true when beam is broken
	boolean beam = !breakBeam.get();
	boolean limit = !stopSwitch.get();
	boolean adjust1 = !adjustBeam1.get();
	boolean adjust2 = !adjustBeam2.get();
	boolean adjust3 = !adjustBeam3.get();
	boolean gearExists = !gear.get();
	boolean xHome = !xHomeSwitch.get();
	boolean yHome = !yHomeSwitch.get();
	
	double 	fast_speed = 0.8,
			slow_speed = 0.3,
			chuteOpenAngle = -180,
			chuteClosedAngle = 0,
			gripperClosedAngle = 0,
			gripperOpenAngle = -180;
	double error = 0.3 * 0.0499;
	int allowableClosedLoopError = 5;
	private enum GearState {
		OFF, INTAKING, MOVING_FAST, MOVING_SLOW, ARM_DEPLOYING, MOVING_FORWARD, ARM_RELEASING, RESETTING 
	}
	
	private GearState state = GearState.OFF;
	
	public Gear() {
		talonX.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talonX.reverseSensor(false);
		talonX.configEncoderCodesPerRev(497);
		talonX.setPID(0.5, 0.001, 0.0);
		talonX.setAllowableClosedLoopErr(allowableClosedLoopError);
		talonX.setInverted(true);
		
		talonY.setFeedbackDevice(FeedbackDevice.QuadEncoder);
		talonY.reverseSensor(false);
		talonY.configEncoderCodesPerRev(497);
		talonY.setPID(0.5, 0.001, 0.0);
		talonY.setAllowableClosedLoopErr(allowableClosedLoopError);
		
		if (xHome){
			talonX.setPosition(XresetPosition);
		}
		if (yHome){
			talonY.setPosition(YresetPosition);
		}
	}
	
	boolean zeroing_position = false,
			Xzeroed = false,
			Yzeroed = false;
	public void zeroPosition(){
		zeroing_position = true;
		Xzeroed = false;
		Yzeroed = false;
		talonX.set(-.2);
		talonY.set(-.2);
		
	}
	
	
	public void manualGearMovement(Vector2 stick){
		//SmartDashboard.putNumber("X gear position", talonX.getPosition());
		//SmartDashboard.putNumber("Y gear position", talonY.getPosition());
	
		
		talonX.changeControlMode(TalonControlMode.PercentVbus);
		talonY.changeControlMode(TalonControlMode.PercentVbus);
		
		
		if (talonX.getPosition() >= XforwardPosition){
			talonX.set(Math.min(-stick.x, 0));
		}
		else if (talonX.getPosition() <= XresetPosition || xHome){
			talonX.set(Math.max(-stick.x, 0));
		}
		else{
			talonX.set(-stick.x);
		}
		if (talonY.getPosition() >= YforwardPosition){
			talonY.set(Math.min(stick.y, 0));
		}
		else if (talonY.getPosition() <= YresetPosition || yHome){
			talonY.set(Math.max(stick.y, 0));
		}
		else{
			talonY.set(stick.y);
		}
		if (xHome){
			talonX.setPosition(XresetPosition);
		}
		if (yHome){
			talonY.setPosition(YresetPosition);
		}
		
	}
	
	public void rotateGear(){
		talonR.set(0);	
		//SmartDashboard.putBoolean("adjust1", adjust1);
		if(adjust1 || !adjust2 || adjust3){
			talonR.set(0.2);
			//SmartDashboard.putBoolean("rotating", true);
		}
		else{
			//SmartDashboard.putBoolean("rotating", false);
			talonR.set(0);
		}
		
	}
	
	public void turn_off(){
		state = GearState.OFF;
	}
	
	public void intakeGear(){
		state = GearState.INTAKING;
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
	
	public void zeroSpeed(){
		talonX.changeControlMode(TalonControlMode.PercentVbus);
		talonY.changeControlMode(TalonControlMode.PercentVbus);
			talonX.set(0);
			talonY.set(0);
	}
	
	public void preventMovingTooFar(){
		//block x and y motors from going too far in any direction
			if (talonX.getPosition()>=XforwardPosition && (talonX.getControlMode()==TalonControlMode.PercentVbus || talonX.getControlMode()==TalonControlMode.Speed)){
				talonX.set(Math.min(0, talonX.get()));
			}
			if (xHome && (talonX.getControlMode()==TalonControlMode.PercentVbus || talonX.getControlMode()==TalonControlMode.Speed)){
				talonX.set(Math.max(0, talonX.get()));
			}
			if (talonY.getPosition()>=YforwardPosition && (talonY.getControlMode()==TalonControlMode.PercentVbus || talonY.getControlMode()==TalonControlMode.Speed)){
				talonY.set(Math.min(0, talonY.get()));
			}
			if (yHome && (talonY.getControlMode()==TalonControlMode.PercentVbus || talonY.getControlMode()==TalonControlMode.Speed)){
				talonY.set(Math.max(0, talonY.get()));
			}
	}
	
	@Override
	public void update() {
		
		beam = !breakBeam.get();
		limit = !stopSwitch.get();
		adjust1 = !adjustBeam1.get();
		adjust2 = !adjustBeam2.get();
		adjust3 = !adjustBeam3.get();
		gearExists = !gear.get();
		xHome = !xHomeSwitch.get();
		yHome = !yHomeSwitch.get();
		
		
		if(!Operator.manualMove && !zeroing_position && state==GearState.OFF){
			zeroSpeed();
		}
		
		if (zeroing_position){
			if (xHome){
				Xzeroed = true;
				talonX.set(0);
				talonX.setPosition(XresetPosition);
			}
			if (yHome){
				Yzeroed = true;
				talonY.set(0);
				talonY.setPosition(YresetPosition);
			}
			if (Xzeroed && Yzeroed){
				zeroing_position = false;
			}
		}
		
		if (xHome){
			talonX.setPosition(XresetPosition);
		}
		if (yHome){
			talonY.setPosition(YresetPosition);
		}
	
		SmartDashboard.putBoolean("gear exists", gearExists);
	//	SmartDashboard.putBoolean("adjust1", adjust1);
	//	SmartDashboard.putBoolean("adjust2", adjust2);
	//	SmartDashboard.putBoolean("adjust3", adjust3);
		if (gearExists){
			rotateGear();
		}
		else{
			talonR.set(0);
		}
		if (state==GearState.INTAKING && !gearExists){
			gripper.setAngle(gripperClosedAngle);
			chutePanel.setAngle(chuteOpenAngle);
			talonY.setSetpoint(YloadingPosition);
		}
		if (state==GearState.INTAKING && gearExists){
			reset();		
		}
		
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
			talonX.set(0);
			gripper.setAngle(gripperClosedAngle);
		}
		if (state == GearState.ARM_DEPLOYING && gripper.getAngle()==gripperClosedAngle){
			state = GearState.MOVING_FORWARD;
		}
		if (state == GearState.MOVING_FORWARD){
			if (first){
				talonY.changeControlMode(TalonControlMode.Position);
				talonY.setSetpoint(YforwardPosition);
				first = false;
			}
		}
		if (state == GearState.MOVING_FORWARD && Math.abs(talonY.getPosition() - YreleaseGripperPosition) < error){
			gripper.setAngle(gripperOpenAngle);
		}		
		if (state == GearState.MOVING_FORWARD && Math.abs(talonY.getPosition() - YforwardPosition) < error){
			state = GearState.ARM_RELEASING;
		}
		if (state == GearState.ARM_RELEASING){
			gripper.setAngle(gripperOpenAngle);
		}
		if (state == GearState.ARM_RELEASING && gripper.getAngle()==gripperOpenAngle){
			state = GearState.OFF;
		}
		if (state == GearState.RESETTING){
			chutePanel.setAngle(chuteClosedAngle);
			gripper.setAngle(gripperOpenAngle);
			
			talonY.changeControlMode(TalonControlMode.Position);
			talonY.setSetpoint(YresetPosition);
			
			talonX.changeControlMode(TalonControlMode.Position);
			talonX.setSetpoint(XresetPosition);
			
			if (xHome){
				talonX.changeControlMode(TalonControlMode.PercentVbus);
				talonX.set(0);
			}
			
			if (yHome){
				talonY.changeControlMode(TalonControlMode.PercentVbus);
				talonY.set(0);
			}
			
			first = true;
		}
		if (state == GearState.RESETTING && gripper.getAngle()==gripperOpenAngle &&  chutePanel.getAngle()==chuteClosedAngle && Math.abs(talonY.getPosition() - YresetPosition) < error && Math.abs(talonX.getPosition() - XresetPosition) < error){
			state = GearState.OFF;
		}
		
		preventMovingTooFar();
	}

}
