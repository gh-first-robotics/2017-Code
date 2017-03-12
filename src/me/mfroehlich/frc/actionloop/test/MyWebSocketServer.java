package me.mfroehlich.frc.actionloop.test;

import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.java_websocket.WebSocket;
import org.java_websocket.handshake.ClientHandshake;
import org.java_websocket.server.WebSocketServer;
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
import org.usfirst.frc.team5530.robot.systems.DriveTrainSystem;
import org.usfirst.frc.team5530.robot.teleop.Vector2;

import me.mfroehlich.frc.actionloop.actions.ActionContext;

public class MyWebSocketServer extends WebSocketServer {
	private List<WebInterfaceEntry> entries = new ArrayList<>();
	private Set<WebSocket> sockets = new HashSet<>();
	
	private ActionContext context;
	
	public MyWebSocketServer(ActionContext context, int port) throws UnknownHostException {
		super(new InetSocketAddress(port));
		
		this.context = context;

		entries.add(new WebInterfaceEntry("Rope gripper", new RopeGripperAction(false)));
		entries.add(new WebInterfaceEntry("Rope gripper", new RopeGripperAction(true)));
		entries.add(new WebInterfaceEntry("Rope gripper", new ToggleRopeGripperAction()));
		
		entries.add(new WebInterfaceEntry("Driving", new DriveAccurateAction(12)));
		entries.add(new WebInterfaceEntry("Driving", new DriveAccurateAction(60)));
		entries.add(new WebInterfaceEntry("Driving", new DriveDistanceAction(12)));
		entries.add(new WebInterfaceEntry("Driving", new DriveDistanceAction(60)));
		entries.add(new WebInterfaceEntry("Turning", new TurnAction(90)));
		entries.add(new WebInterfaceEntry("Turning", new TurnAction(180)));
		
		entries.add(new WebInterfaceEntry("Gear chute", new ChutePanelAction(true)));
		entries.add(new WebInterfaceEntry("Gear chute", new ChutePanelAction(false)));
		entries.add(new WebInterfaceEntry("Gear chute", new RotateGearAction()));
		
		entries.add(new WebInterfaceEntry("Peg gripper", new PegGripperAction(Position.OPEN)));
		entries.add(new WebInterfaceEntry("Peg gripper", new PegGripperAction(Position.HALF)));
		entries.add(new WebInterfaceEntry("Peg gripper", new PegGripperAction(Position.CLOSED)));
		
		entries.add(new WebInterfaceEntry("Slide", new AlignSlideWithPegAction()));
		
		entries.add(new WebInterfaceEntry("Lateral slide", new PositionLateralSlideAction(1)));
		entries.add(new WebInterfaceEntry("Lateral slide", new PositionLateralSlideAction(3)));
		entries.add(new WebInterfaceEntry("Lateral slide", new PositionLateralSlideAction(5)));
		entries.add(new WebInterfaceEntry("Lateral slide", new ResetLateralSlideAction()));
		
		entries.add(new WebInterfaceEntry("Axial slide", new PositionAxialSlideAction(1)));
		entries.add(new WebInterfaceEntry("Axial slide", new PositionAxialSlideAction(4.5)));
		entries.add(new WebInterfaceEntry("Axial slide", new PositionAxialSlideAction(8)));
		entries.add(new WebInterfaceEntry("Axial slide", new ResetAxialSlideAction()));
		
		for (WebInterfaceEntry entry : entries) {
			entry.action.stateChanged.listen(() -> update(entry));
		}
		
		DriveTrainSystem.position.moved.listen(this::updatePosition);
	}
	
	private void updatePosition() {
		Vector2 pos = DriveTrainSystem.position.getPosition();
		double angle = DriveTrainSystem.position.getAngle();

		StringBuilder str = new StringBuilder("{ ");
		str.append("\"type\": \"position\", ");
		str.append("\"x\": " + pos.x + ", ");
		str.append("\"y\": " + pos.y + ", ");
		str.append("\"angle\": " + angle);
		str.append(" }");
		
		send(str.toString());
	}
	
	private String stringify(WebInterfaceEntry entry) {
		StringBuilder str = new StringBuilder("{ ");
		str.append("\"type\": \"action\", ");
		str.append("\"index\": " + entries.indexOf(entry) + ", ");
		str.append("\"category\": \"" + entry.category + "\", ");
		str.append("\"state\": \"" + entry.action.getState().name() + "\", ");
		str.append("\"name\": \"" + entry.action.name + "\"");
		str.append(" }");
		return str.toString();
	}
	
	private void update(WebInterfaceEntry action) {
		String str = stringify(action);
		send(str);
	}
	
	private void send(String str) {
		for (WebSocket conn : sockets) {
			conn.send(str);
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		this.sockets.add(conn);
		
		for (WebInterfaceEntry action : entries) {
			update(action);
		}
	}

	@Override
	public void onMessage(WebSocket conn, String message) {
		if (message.startsWith("execute ")) {
			int index = Integer.parseInt(message.substring(8));
			WebInterfaceEntry entry = entries.get(index);
			context.execute(entry.action);
			System.out.println("executing " + index);
		} else {
			System.out.println(message);
		}
	}

	@Override
	public void onClose(WebSocket conn, int code, String reason, boolean remote) {
		this.sockets.remove(conn);
	}

	@Override
	public void onError(WebSocket conn, Exception e) {
		e.printStackTrace();
	}
}
