package org.usfirst.frc.team5530.robot;

import java.io.IOException;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.List;

import org.usfirst.frc.team5530.robot.actions.climber.RopeGripperAction;
import org.usfirst.frc.team5530.robot.actions.climber.ToggleRopeGripperAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveAccurateAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.DriveDistanceAction;
import org.usfirst.frc.team5530.robot.actions.drivetrain.TurnAction;
import org.usfirst.frc.team5530.robot.actions.gears.AlignSlideWithPegAction;
import org.usfirst.frc.team5530.robot.actions.gears.ChutePanelAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction;
import org.usfirst.frc.team5530.robot.actions.gears.PegGripperAction.Position;
import org.usfirst.frc.team5530.robot.actions.gears.PositionAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.PositionLateralSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetAxialSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.ResetLateralSlideAction;
import org.usfirst.frc.team5530.robot.actions.gears.RotateGearAction;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

import me.mfroehlich.frc.actionloop.actions.Action;

public class RobotHttpHandler implements HttpHandler {
	private List<Action> actions = new ArrayList<>();
	
	public RobotHttpHandler() {
		actions.add(new RopeGripperAction(true));
		actions.add(new RopeGripperAction(false));
		actions.add(new ToggleRopeGripperAction());
		
		actions.add(new DriveAccurateAction(12));
		actions.add(new DriveAccurateAction(60));
		actions.add(new DriveDistanceAction(12));
		actions.add(new DriveDistanceAction(60));
		
		actions.add(new TurnAction(90));
		actions.add(new TurnAction(180));
		
		actions.add(new AlignSlideWithPegAction());
		actions.add(new ChutePanelAction(true));
		actions.add(new ChutePanelAction(false));
		actions.add(new PegGripperAction(Position.OPEN));
		actions.add(new PegGripperAction(Position.HALF));
		actions.add(new PegGripperAction(Position.CLOSED));
		
		actions.add(new PositionLateralSlideAction(1));
		actions.add(new PositionLateralSlideAction(3));
		actions.add(new PositionLateralSlideAction(5));
		
		actions.add(new PositionAxialSlideAction(1));
		actions.add(new PositionAxialSlideAction(4.5));
		actions.add(new PositionAxialSlideAction(8));
		
		actions.add(new ResetAxialSlideAction());
		actions.add(new ResetLateralSlideAction());
		
		actions.add(new RotateGearAction());
	}
	
	@Override
	public void handle(HttpExchange http) throws IOException {
		StringBuilder str = new StringBuilder();
		
		str.append("[");
		for (int i = 0; i < actions.size(); i++) {
			Action act = actions.get(i);
			
			str.append("{ ");
			str.append("\"name\":\"" + act.name+"\",");
			str.append("\"index\":" + i+"");
			str.append(" },");
		}
		str.setLength(str.length() - 1);
		str.append("]");
		
		String body = str.toString();

		http.sendResponseHeaders(200, body.length());
		
		try (OutputStream out = http.getResponseBody()) {
			out.write(body.getBytes());
		}
	}
}
