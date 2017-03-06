package me.mfroehlich.frc.controls;

public class ControlButton {
	public final int stick;
	public final int button;
	
	public ControlButton(int stick, int button) {
		this.stick = stick;
		this.button = button;
	}
	
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
