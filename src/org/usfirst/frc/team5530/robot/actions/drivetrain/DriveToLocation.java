package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.FindLocation;
import org.usfirst.frc.team5530.robot.teleop.Vector2;
import me.mfroehlich.frc.actionloop.actions.SequentialActionSet;

public class DriveToLocation extends SequentialActionSet{
	
	Vector2 vector;
	public DriveToLocation(Vector2 target) {
		Vector2 vector = target.subtract(FindLocation.location);
		add(new DriveVector(vector));
	}
	
	
}
