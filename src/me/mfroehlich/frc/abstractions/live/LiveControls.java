package me.mfroehlich.frc.abstractions.live;

import java.util.HashSet;
import java.util.Set;

import org.usfirst.frc.team5530.robot.teleop.Vector2;

import edu.wpi.first.wpilibj.DriverStation;
import me.mfroehlich.frc.abstractions.Controls;
import me.mfroehlich.frc.controls.ControlButton;
import me.mfroehlich.frc.controls.ControlsState;

class LiveControls implements Controls {
	private int[] sticks;
	private DriverStation ds;
	
	public LiveControls(int[] sticks) {
		this.sticks = sticks;
		this.ds = DriverStation.getInstance();
	}

	@Override
	public ControlsState getState() {
		Set<ControlButton> buttons = new HashSet<>();
		Vector2[] sticks = new Vector2[this.sticks.length];
		
		for (int i = 0; i < this.sticks.length; i++) {
			int stick = this.sticks[i];
			
			sticks[i] = new Vector2(ds.getStickAxis(stick, 0), ds.getStickAxis(stick, 1));
			
			for (byte j = 0; j < ds.getStickButtonCount(stick); j++) {
				if (!ds.getStickButton(stick, (byte) (j + 1))) {
					continue;
				}
				
				buttons.add(new ControlButton(stick + 1, j + 1));
			}
		}
		
		return new ControlsState(sticks, buttons);
	}
}
