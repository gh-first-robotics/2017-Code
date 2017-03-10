package org.usfirst.frc.team5530.robot.test;

import java.util.LinkedList;
import java.util.Queue;

import org.usfirst.frc.team5530.robot.actions.climber.ClimbAction;
import org.usfirst.frc.team5530.robot.actions.climber.RopeGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;
import org.usfirst.frc.team5530.robot.actions.gears.PositionAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.PositionLateralSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetLateralSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.RotateGearAction;
import org.usfirst.frc.team5530.robot.systems.AxialSlideSystem;
import org.usfirst.frc.team5530.robot.systems.GearChuteSystem;
import org.usfirst.frc.team5530.robot.systems.LateralSlideSystem;
import org.usfirst.frc.team5530.robot.systems.PegInterfaceSystem;

import me.mfroehlich.frc.abstractions.Controls;
import me.mfroehlich.frc.actionloop.Controller;
import me.mfroehlich.frc.actionloop.actions.Action;
import me.mfroehlich.frc.actionloop.actions.lib.DelayAction;
import me.mfroehlich.frc.actionloop.actions.lib.PrintAction;
import me.mfroehlich.frc.controls.Button;
import me.mfroehlich.frc.controls.ButtonMap;

public class TestController extends Controller {
	private Queue<Action> queue = new LinkedList<>();
	
	private ButtonMap controls = new ButtonMap(Controls.create(0), this);
	private Button advance = controls.map(1, 7, "Advance");
	
	private Action last;
	
	private void test(String message, Action action) {
		queue.add(new PrintAction(message));
		
		queue.add(Action.inSequence("Test action: " + message,
			action,
			new PrintAction("done")
		));
	}
	
	@Override
	protected void init() { }
	
	@Override
	public void start() {
//		test("Wait 2sec, close peg gripper", Action.inSequence("TEST",
//			new DelayAction(2000),
//			new PrintAction("HI"),
//			new PegGripperAction(Position.CLOSED)
//		));
//		
//		test("Wait 2sec, close peg gripper", Action.inSequence("TEST2",
//			Action.inRace("TEST", true,
//				new ClimbAction(.1),
//				new DelayAction(2000)
//			),
//			new PrintAction("DONE TEST2")
//		));
//		
//		test("Turn 90 (PID)", new TurnAction(90));
//		test("Turn 90", new Turn2Action(90));
//		
//		test("Turn -90 (PID)", new TurnAction(-90));
//		test("Turn -90", new Turn2Action(-90));
		
		test("Test peg break beam", new WaitForDigitalSensorAction(PegInterfaceSystem.breakBeam));
		test("Test peg limit switch", new WaitForDigitalSensorAction(PegInterfaceSystem.limitSwitch));
		
		test("Test gear presence break beam", new WaitForDigitalSensorAction(GearChuteSystem.gearSensor));
		test("Test gear alignment left break beam", new WaitForDigitalSensorAction(GearChuteSystem.leftSensor));
		test("Test gear alignment middle break beam", new WaitForDigitalSensorAction(GearChuteSystem.middleSensor));
		test("Test gear alignment right break beam", new WaitForDigitalSensorAction(GearChuteSystem.rightSensor));
		
		test("Test axial (Y) home limit switch", new WaitForDigitalSensorAction(AxialSlideSystem.homeSwitch));
		test("Test lateral (X) home limit switch", new WaitForDigitalSensorAction(LateralSlideSystem.homeSwitch));
		
		test("Open peg gripper", new PegGripperAction(Position.OPEN));
		test("Half peg gripper", new PegGripperAction(Position.HALF));
		test("Close peg gripper ", new PegGripperAction(Position.CLOSED));
		
		test("Open gear chute", new ChutePanelAction(true));
		test("Close gear chute", new ChutePanelAction(false));
		
		test("Open rope grippers", new RopeGripperAction(true));
		test("Close rope grippers", new RopeGripperAction(false));

		test("Reset axial (Y) slide", new ResetAxialSlideAction());
		test("Reset lateral (X) slide", new ResetLateralSlideAction());
		
		test("Position axial slide", new PositionAxialSlideAction(4));
		test("Position lateral slide", new PositionLateralSlideAction(3));
		
		test("Align gear", new RotateGearAction());
		
		test("Climb for 1 second", Action.inRace("Test climber", true,
			new DelayAction(1000),
			new ClimbAction(.1)
		));
	}

	@Override
	public void stop() { }
	
	public void tick() {
		controls.update();
		
		if (advance.isNewlyPressed()) {
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
