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

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;

import me.mfroehlich.frc.actionloop.actions.Action;
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
			entry.action.stateChanged.listen(() -> sendEntry(entry));
		}
		
		DriveTrainSystem.position.moved.listen(this::sendPosition);
		context.executingChanged.listen(this::sendExecuting);
	}
	
	private void sendExecuting() {
		JsonArray list = new JsonArray();
		
		for (Action action : context.getExecuting()) {
			JsonObject json = new JsonObject();
			json.addProperty("name", action.name);
			json.addProperty("state", action.getState().name());
			list.add(json);
		}
		
		send("executing", list);
	}
	
	private void sendPosition() {
		Vector2 pos = DriveTrainSystem.position.getPosition();
		double angle = DriveTrainSystem.position.getAngle();
		
		JsonObject json = new JsonObject();
		json.addProperty("angle", angle);
		json.addProperty("x", pos.x);
		json.addProperty("y", pos.y);
		send("position", json);
	}
	
	private void sendEntry(WebInterfaceEntry entry) {
		JsonObject json = new JsonObject();
		json.addProperty("index", entries.indexOf(entry));
		json.addProperty("category", entry.category);
		json.addProperty("state", entry.action.getState().name());
		json.addProperty("name", entry.action.name);
		send("action", json);
	}
	
	private void send(String type, JsonElement json) {
		JsonObject packet = new JsonObject();
		packet.addProperty("type", type);
		packet.add("data", json);
		
		String str = packet.toString();
			
		for (WebSocket conn : sockets) {
			conn.send(str);
		}
	}

	@Override
	public void onOpen(WebSocket conn, ClientHandshake handshake) {
		this.sockets.add(conn);
		
		for (WebInterfaceEntry action : entries) {
			sendEntry(action);
		}
		
		sendPosition();
		sendExecuting();
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
