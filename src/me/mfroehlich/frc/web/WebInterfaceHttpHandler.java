package me.mfroehlich.frc.web;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;

public class WebInterfaceHttpHandler implements HttpHandler {
	@Override
	public void handle(HttpExchange http) throws IOException {
		String path = http.getRequestURI().getPath();
		
		if (path.endsWith("/")) {
			path += "index.html";
		}
		
		System.out.println(path);
		try (InputStream src = WebInterfaceHttpHandler.class.getResourceAsStream("files" + path)) {
			if (src == null) {
				http.sendResponseHeaders(404, 0);
			} else {
				http.sendResponseHeaders(200, 0);
				copy(src, http.getResponseBody());
			}
			
			http.close();
		} catch (Throwable e) {
			e.printStackTrace();
		}
	}
	
	private static void copy(InputStream src, OutputStream dst) throws IOException {
		byte[] buffer = new byte[8192];
		int read;
		while ((read = src.read(buffer)) > 0) {
			dst.write(buffer, 0, read);
		}
	}
}
