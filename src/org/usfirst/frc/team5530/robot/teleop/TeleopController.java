package org.usfirst.frc.team5530.robot.teleop;

import org.usfirst.frc.team5530.robot.FindLocation;
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

import me.mfroehlich.frc.abstractions.Controls;
import me.mfroehlich.frc.actionloop.Controller;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.controls.Button;
import me.mfroehlich.frc.controls.Button.Binding;
import me.mfroehlich.frc.controls.ButtonMap;
import me.mfroehlich.frc.controls.ControlsState;

public class TeleopController extends Controller {
	private ButtonMap control = new ButtonMap(Controls.create(0, 1), this);
	
	private Button bStartup = control.map(1, 1, "Startup button");
	private Button bReset = control.map(1, 2, "Reset button");
	private Button bReceiveGear1 = control.map(1, 7, "Receive gear");
	private Button bReceiveGear2 = control.map(1, 8, "Receive gear");
	private Button bUnloadGear = control.map(1, 9, "Unload gear");
	
	private Button bClimbGrabber = control.map(1, 10, "Climb grab");
	private Button bClimbSlow = control.map(1, 11, "Climb slow");
	private Button bClimbFast = control.map(1, 12, "Climb fast");
	
	private ManualDriveAction drive;
	
	@Override
	public void init() {
		bStartup.bind(Binding.ON_PRESS, Action.inSequence(
			Action.inParallal(
				new ResetAxialSlideAction(),
				new ResetLateralSlideAction(),
				new RotateGearAction()
			),
			Action.inParallal(
				new PegGripperAction(Position.OPEN),
				new ChutePanelAction(false)
			)
		));
		
		bReset.bind(Binding.ON_PRESS, Action.inSequence(
			Action.inParallal(
				new ResetAxialSlideAction(),
				new ResetLateralSlideAction()
			),
			new ChutePanelAction(true)
		));
		
		bReceiveGear1.bind(Binding.ON_PRESS, Action.inSequence(
			new PegGripperAction(Position.HALF),
			new ChutePanelAction(true),
			Action.inParallal(
				new PositionAxialSlideAction(6.5),
				new PositionLateralSlideAction(3)
			)
		));
		
		bReceiveGear2.bind(Binding.ON_PRESS, Action.inSequence(
			new ChutePanelAction(false),
			new PegGripperAction(Position.OPEN),
			
			Action.inParallal(
				new ResetAxialSlideAction(),
				new ResetLateralSlideAction()
			),
			
			new RotateGearAction()
		));
		
		bUnloadGear.bind(Binding.ON_PRESS, Action.inSequence(
			new AlignSlideWithPegAction(),
			Action.inParallal(
				new ChutePanelAction(false),
				new PegGripperAction(Position.CLOSED)
			),
			new RotateGearAction(),
			
			new PositionAxialSlideAction(4.5, true),
			new PegGripperAction(Position.OPEN),
			new PositionAxialSlideAction(8.5)
		));
		
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
	
	public void tick() {
		ControlsState state = control.update();
		FindLocation.update();
		this.drive.control(state.getStick(1));
	}
}
