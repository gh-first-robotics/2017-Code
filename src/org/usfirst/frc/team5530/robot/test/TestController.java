package org.usfirst.frc.team5530.robot.test;

import java.util.LinkedList;
import java.util.Queue;

import org.usfirst.frc.team5530.robot.FindLocation;
import org.usfirst.frc.team5530.robot.actions.climber.ClimbAction;
import org.usfirst.frc.team5530.robot.actions.climber.RopeGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;
import org.usfirst.frc.team5530.robot.actions.gears.ResetAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetLateralSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.RotateGearAction;
import org.usfirst.frc.team5530.robot.systems.AxialSlideSystem;
import org.usfirst.frc.team5530.robot.systems.GearChuteSystem;
import org.usfirst.frc.team5530.robot.systems.LateralSlideSystem;
import org.usfirst.frc.team5530.robot.systems.PegInterfaceSystem;
import org.usfirst.frc.team5530.robot.teleop.ControlButton;

import me.mfroehlich.frc.abstractions.Controls;
import me.mfroehlich.frc.actionloop.Controller;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.lib.DelayAction;
import me.mfroehlich.frc.controls.ControlsState;

public class TestController extends Controller {
	private Queue<Action> queue = new LinkedList<>();
	private Controls controls = Controls.create(0);
	private ControlsState state, lastState;
	private ControlButton button = new ControlButton(1, 7);
	
	private Action last;
	
	private void test(String message, Action action) {
		queue.add(new TestAction(message, action));
	}
	
	@Override
	protected void init() { }
	
	@Override
	public void start() {
		test("Test peg break beam", new WaitForDigitalSensorAction(PegInterfaceSystem.breakBeam, true));
		test("Test peg limit switch", new WaitForDigitalSensorAction(PegInterfaceSystem.limitSwitch, true));
		
		test("Test gear presence break beam", new WaitForDigitalSensorAction(GearChuteSystem.gearSensor, true));
		test("Test gear alignment left break beam", new WaitForDigitalSensorAction(GearChuteSystem.leftSensor, true));
		test("Test gear alignment middle break beam", new WaitForDigitalSensorAction(GearChuteSystem.middleSensor, true));
		test("Test gear alignment right break beam", new WaitForDigitalSensorAction(GearChuteSystem.rightSensor, true));
		
		test("Test axial (Y) home limit switch", new WaitForDigitalSensorAction(AxialSlideSystem.homeSwitch, true));
		test("Test lateral (X) home limit switch", new WaitForDigitalSensorAction(LateralSlideSystem.homeSwitch, true));
		
		test("Open peg gripper", new PegGripperAction(Position.OPEN));
		test("Half peg gripper", new PegGripperAction(Position.HALF));
		test("Close peg gripper ", new PegGripperAction(Position.CLOSED));
		
		test("Open gear chute", new ChutePanelAction(true));
		test("Close gear chute", new ChutePanelAction(false));
		
		test("Open rope grippers", new RopeGripperAction(true));
		test("Close rope grippers", new RopeGripperAction(false));

		test("Reset axial (Y) slide", new ResetAxialSlideAction());
		test("Reset lateral (X) slide", new ResetLateralSlideAction());
		
		test("Align gear", new RotateGearAction());
		
		test("Climb for 1 second", Action.inRace(true,
			new DelayAction(1000),
			new ClimbAction(.5)
		));
	}

	@Override
	public void stop() { }
	
	public void tick() {
		
		FindLocation.update();
		
		lastState = state;
		state = controls.getState();
		
		if (state.isNewlyPressed(button, lastState)) {
			if (last != null && last.isRunning()) {
				last.cancel();
			} else {
				Action action = queue.poll();
				
				if (action != null) {
					execute(action);
				}
				
				last = action;
			}
		}
	}
}
