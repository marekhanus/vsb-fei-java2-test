package lab;

import java.io.IOException;
import java.sql.SQLException;

import org.h2.tools.Server;

import lab.storage.JpaConnector;
import lombok.extern.log4j.Log4j2;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the
 * program
 * 
 * @author Java I
 */
@Log4j2
public class App {

	public static void main(String[] args) {
		log.info("Application lauched");

		Server server = startDBWebServer();

		JpaConnector connector = new JpaConnector();

		//TODO

		waitForKeyPress();
		stopDBWebServer(server);
	}

	private static Server startDBWebServer() {
		// Start HTTP server for access H2 DB for look inside
		try {
			Server server = Server.createWebServer();
			log.info(server.getURL());
			server.start();
			log.info("DB Web server started!");
			return server;
		} catch (SQLException e) {
			log.error("Cannot create DB web server.", e);
			return null;
		}
	}

	private static void stopDBWebServer(Server server) {
		// Stop HTTP server for access H2 DB
		log.info("Ending DB web server BYE.");
		server.stop();
	}

	private static void waitForKeyPress() {
		log.info("Waitnig for Key press (ENTER)");
		try {
			System.in.read();
		} catch (IOException e) {
			log.error("Cannot read input from keyboard.", e);
		}
	}

}