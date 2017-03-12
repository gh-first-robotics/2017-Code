package me.mfroehlich.frc.web;

import java.io.IOException;
import java.net.InetSocketAddress;

import com.sun.net.httpserver.HttpServer;

import me.mfroehlich.frc.actionloop.actions.ActionContext;

public class WebInterface {
	public static void start(ActionContext actions, int http, int ws) {
		try {
			HttpServer httpServer = HttpServer.create(new InetSocketAddress(http), 10);
			httpServer.createContext("/", new WebInterfaceHttpHandler());
			httpServer.start();
			System.out.println("Started Http server on port: " + httpServer.getAddress().getPort());
			
			WebInterfaceSocketServer sockets = new WebInterfaceSocketServer(actions, ws);
			sockets.start();
			System.out.println("Started WebSocket server on port: " + sockets.getPort());
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}
