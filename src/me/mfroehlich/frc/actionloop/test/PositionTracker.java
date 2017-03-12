package me.mfroehlich.frc.actionloop.test;

import java.io.PrintWriter;

import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import me.mfroehlich.frc.abstractions.Environment;
import me.mfroehlich.frc.abstractions.Talon;
import me.mfroehlich.frc.abstractions.live.LiveEnvironment;
import me.mfroehlich.frc.events.Event;

public class PositionTracker {
	private static int distanceBetweenWheels = (int) (DriveTrainSystem.ticksPerInch * 25.5);
	
	private Talon leftTalon, rightTalon;
	private int leftEncoder, rightEncoder;
	
	private double angle = 0;
	private Vector2 position = new Vector2();
	
	private PrintWriter writer;
	
	public final Event moved = new Event();
	
	public Vector2 getPosition() {
		return this.position;
	}
	
	public double getAngle() {
		return this.angle;
	}
	
	public PositionTracker(Talon left, Talon right) {
		this.leftTalon = left;
		this.rightTalon = right;
		
		leftEncoder = -left.getEncoderPosition();
		rightEncoder = right.getEncoderPosition();
		
		try {
			if (Environment.is(LiveEnvironment.class)) {
				this.writer = new PrintWriter("/home/lvuser/positions.txt");
			}
		} catch (Throwable x) {
			x.printStackTrace();
		}
	}
	
	public void tick() {
		try {
			int newLeftEncoder = -leftTalon.getEncoderPosition();
			int newRightEncoder = rightTalon.getEncoderPosition();
			double dLeft = newLeftEncoder - this.leftEncoder;
			double dRight = newRightEncoder - this.rightEncoder;
			double ratio = dLeft / dRight;
			
			if (dLeft == 0 && dRight == 0) {
				// The encoders have not changed
				return;
			} else if (ratio == 1) {
				// dLeft == dRight
				Vector2 movement = new Vector2(0, dRight).rotate(this.angle);
				
				this.position = this.position.add(movement);
			} else if (ratio == -1) {
				// dLeft == -dRight
				double turnRadius = distanceBetweenWheels / 2;
				double turnAngle = dLeft / turnRadius;
				
				this.angle = this.angle + turnAngle;
			} else {
				double turnRadius = (distanceBetweenWheels / (ratio - 1)) + (distanceBetweenWheels / 2);
				double turnAngle = (dLeft + dRight) / 2 / turnRadius;
				
				double x = turnRadius * (1 - Math.cos(turnAngle));
				double y = turnRadius * Math.sin(turnAngle);
				
				Vector2 movement = new Vector2(x, y).rotate(this.angle);
				
				this.angle = this.angle + turnAngle;
				this.position = this.position.add(movement);
			}
			
			this.leftEncoder = newLeftEncoder;
			this.rightEncoder = newRightEncoder;
			
			if (writer != null) {
				writer.write(ratio + ": " + this.position.x + " " + this.position.y + " " + this.angle + "\n");
				writer.flush();
			}
			this.moved.emit();
		} catch (Throwable x) {
			x.printStackTrace();
		}
	}
}
