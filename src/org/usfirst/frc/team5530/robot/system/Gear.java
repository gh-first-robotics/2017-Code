package org.usfirst.frc.team5530.robot.system;


import edu.wpi.first.wpilibj.DigitalInput;
import com.ctre.CANTalon;


public class Gear implements RobotSystem{
	int x = 5,
		y = 6,
		bB = 0,
		lS = 1;
	
	private CANTalon talonX = new CANTalon(x);
	private CANTalon talonY = new CANTalon(y);
	private DigitalInput breakBeam = new DigitalInput(bB);
	private DigitalInput stopSwitch= new DigitalInput(lS);
	
	public Gear() {
		
	}
	
	@Override
	public void update() {
		// TODO Auto-generated method stub
		
	}

}
