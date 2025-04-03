package lab;

import java.io.IOException;
import java.sql.SQLException;

import org.h2.tools.Server;

public class DatabaseControl {

	private static Server server;  
	
	public static void startDBWebServer() {
		// Start HTTP server for access H2 DB for look inside
		try {
			server = Server.createWebServer();
			System.out.println(server.getURL());
			server.start();
			System.out.println("DB Web server started!");			
		} catch (SQLException e) {
			System.out.println("Cannot create DB web server.");
			e.printStackTrace();
		}
	}
	public static void waitForKeyAndStopDBWebServer() {
		waitForKeyPress();
		stopDBWebServer();
	}
	public static void stopDBWebServer() {
		// Stop HTTP server for access H2 DB
		System.out.println("Ending DB web server BYE.");
		server.stop();
	}

	public static void waitForKeyPress() {
		System.out.println("Waiting for Key press (ENTER)");
		try {
			System.in.read();
		} catch (IOException e) {
			System.out.println("Cannot read input from keyboard.");
			e.printStackTrace();
		}
	}

}