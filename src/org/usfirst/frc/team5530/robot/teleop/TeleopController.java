package org.usfirst.frc.team5530.robot.teleop;

import org.usfirst.frc.team5530.robot.actions.LoadGear1Action;
import org.usfirst.frc.team5530.robot.actions.LoadGear2Action;
import org.usfirst.frc.team5530.robot.actions.QuickResetAction;
import org.usfirst.frc.team5530.robot.actions.StartupAction;
import org.usfirst.frc.team5530.robot.actions.UnloadGearAction;
import org.usfirst.frc.team5530.robot.actions.climber.ClimbAction;
import org.usfirst.frc.team5530.robot.actions.climber.ToggleRopeGripperAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.ManualDriveAction;
import org.usfirst.frc.team5530.robot.systems.AxialSlideSystem;
import org.usfirst.frc.team5530.robot.systems.LateralSlideSystem;

import me.mfroehlich.frc.abstractions.Controls;
import me.mfroehlich.frc.actionloop.Controller;
import me.mfroehlich.frc.controls.Button;
import me.mfroehlich.frc.controls.Button.Binding;
import me.mfroehlich.frc.controls.ButtonMap;
import me.mfroehlich.frc.controls.ControlsState;

public class TeleopController extends Controller {
	private ButtonMap control = new ButtonMap(Controls.create(0, 1), this);
	
	private Button bStartup = control.map(1, 1, "Startup button");
	private Button bReset = control.map(1, 2, "Reset button");
	
	private Button bClimbGrabber = control.map(1, 7, "Climb grab");
	private Button bUnloadGear = control.map(1, 8, "Unload gear");
	private Button bReverse = control.map(1, 12, "Reverse driving");
	
	private Button bReceiveGear1 = control.map(2, 1, "Receive gear prt. 1");
	private Button bReceiveGear2 = control.map(2, 2, "Receive gear prt. 2");
	
	private Button bClimbSlow = control.map(2, 8, "Climb slow");
	private Button bClimbFast = control.map(2, 7, "Climb fast");
	
	private ManualDriveAction drive;
	
	@Override
	public void init() {
		AxialSlideSystem.isCalibrated = false;
		LateralSlideSystem.isCalibrated = false;
		
		bStartup.bind(Binding.ON_PRESS, new StartupAction());
		bReset.bind(Binding.ON_PRESS, new QuickResetAction());
		bReceiveGear1.bind(Binding.ON_PRESS, new LoadGear1Action());
		bReceiveGear2.bind(Binding.ON_PRESS, new LoadGear2Action());
		bUnloadGear.bind(Binding.ON_PRESS, new UnloadGearAction());
		
		bClimbSlow.bind(Binding.WHILE_PRESSED, new ClimbAction(.3));
		bClimbFast.bind(Binding.WHILE_PRESSED, new ClimbAction(.7));
		bClimbGrabber.bind(Binding.ON_PRESS, new ToggleRopeGripperAction());
	}
	
	@Override
	public void start() {
		drive = new ManualDriveAction();
		execute(drive);
	}
	
	@Override
	public void stop() { }
	
	@Override
	public void tick() {
		ControlsState state = control.update();
		
		if (bReverse.isNewlyPressed()) {
			this.drive.reverse();
		}
		
		this.drive.control(state.getStick(1));
	}
}
