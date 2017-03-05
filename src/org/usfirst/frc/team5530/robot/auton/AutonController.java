package org.usfirst.frc.team5530.robot.auton;

import org.usfirst.frc.team5530.robot.FindLocation;
import org.usfirst.frc.team5530.robot.actions.auton.Center;
import org.usfirst.frc.team5530.robot.actions.auton.Left;
import org.usfirst.frc.team5530.robot.actions.auton.Right;
import org.usfirst.frc.team5530.robot.actions.climber.ClimbAction;
import org.usfirst.frc.team5530.robot.actions.climber.ToggleRopeGripperAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.ManualDriveAction;
import org.usfirst.frc.team5530.robot.actions.gears.AlignSlideWithPegAction;
import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;
import org.usfirst.frc.team5530.robot.actions.gears.PositionAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.PositionLateralSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetLateralSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.RotateGearAction;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import me.mfroehlich.frc.abstractions.Controls;
import me.mfroehlich.frc.actionloop.Controller;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.controls.Button;
import me.mfroehlich.frc.controls.Button.Binding;
import me.mfroehlich.frc.controls.ButtonMap;
import me.mfroehlich.frc.controls.ControlsState;

public class AutonController extends Controller {
	
	final String left = "left side";
	final String center = "center";
	final String right = "right side";
	SendableChooser locationChooser = new SendableChooser();	
	final String[] locations = { left, center, right };
	
	@Override
	public void init() {	
		locationChooser.addDefault(locations[0], locations[0]);
		SmartDashboard.putData("locations", locationChooser);
		
	}
	
	@Override
	public void start() {
		String locations = (String) locationChooser.getSelected();
		switch(locations){
		case left:
			new Left();
		case right:
			new Right();
		case center:
			new Center();
		
		}
	}
	
	@Override
	public void stop() { }
	
	public void tick() {
		
		FindLocation.update();
		
	}
}
