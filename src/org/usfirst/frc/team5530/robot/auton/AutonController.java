package org.usfirst.frc.team5530.robot.auton;

import org.usfirst.frc.team5530.robot.FindLocation;
import org.usfirst.frc.team5530.robot.Robot;
import org.usfirst.frc.team5530.robot.actions.auton.Left;
import org.usfirst.frc.team5530.robot.actions.auton.Right;
import org.usfirst.frc.team5530.robot.actions.auton.Center;

import edu.wpi.first.wpilibj.smartdashboard.SendableChooser;
import edu.wpi.first.wpilibj.smartdashboard.SmartDashboard;
import me.mfroehlich.frc.actionloop.Controller;

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
			execute(new Left());
		case right:
			execute(new Right());
		case center:
			execute(new Center());
		
		}
	}
	
	@Override
	public void stop() { }
	
	public void tick() {
		
		FindLocation.update();
		
	}
}
