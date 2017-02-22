package org.usfirst.frc.team5530.robot.teleop;

public class ControlButton {
	public final int stick;
	public final int button;
	
	public ControlButton(int stick, int button) {
		this.stick = stick;
		this.button = button;
	}
	
//	public static final ControlButton
//		Startup = new ControlButton(1, 1),
//		ReceiveGear = new ControlButton(1, 7),
//		AlignGear = new ControlButton(1, 8),
//		UnloadGear = new ControlButton(1, 9);
//	
//	public static final ControlButton
//		Climb = new ControlButton(1, 12);
	
	@Override
	public String toString() {
		return stick + " " + button;
	}
	
	@Override
	public int hashCode() {
		return stick * 1000 + button;
	}
	
	@Override
	public boolean equals(Object obj) {
		if (obj instanceof ControlButton) {
			ControlButton b = (ControlButton) obj;
			return b.stick == stick && b.button == button;
		} else {
			return false;
		}
	}
}
