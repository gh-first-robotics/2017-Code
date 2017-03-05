package org.usfirst.frc.team5530.robot.actions.drivetrain;

import org.usfirst.frc.team5530.robot.teleop.Vector2;
import me.mfroehlich.frc.actionloop.actions.SequentialActionSet;

public class DriveVector extends SequentialActionSet {
	Vector2 vector;
	
	public DriveVector(Vector2 vector) {
		this.vector = vector;
		add(new TurnToAngleAction(vector.direction));
		add(new DriveDistanceAction(vector.magnitude));
	}
	
}
