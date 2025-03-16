package lab.gui;

import java.sql.SQLException;

import org.h2.tools.Server;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.Setting;
import lab.storage.JpaConnector;
import lombok.extern.log4j.Log4j2;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the
 * program
 * 
 * @author Java I
 */
@Log4j2
public class App extends Application {

	private GameController gameController;

	public static void main(String[] args) {
		log.info("Application lauched");
		Setting.configure(Setting.builder().scoreStorageInterface(new JpaConnector()).build());

		startH2WebServerToInspectDb();

		launch(args);
	}

	private static void startH2WebServerToInspectDb() {
		//Start HTTP server for access H2 DB for look inside 
		try {
		    Server server = Server.createWebServer();
		    log.info(server.getURL());
		    server.start();
		} catch (SQLException e) {
		    e.printStackTrace();
		}
	}

	@Override
	public void start(Stage primaryStage) {
		try {
			// Construct a main window with a canvas.
			FXMLLoader gameLoader = new FXMLLoader(getClass().getResource("/lab/gui/gameWindow.fxml"));
			Parent root = gameLoader.load();
			gameController = gameLoader.getController();
			Scene scene = new Scene(root);
			primaryStage.setScene(scene);
			primaryStage.setTitle("Java 2 - 2nd laboratory");
			primaryStage.show();
			// Exit program when main window is closed
			primaryStage.setOnCloseRequest(this::exitProgram);
		} catch (Exception e) {
			log.error("Error during game play.", e);
		}
	}

	@Override
	public void stop() throws Exception {
		gameController.stop();
		super.stop();
		log.info("Gamne stoped");
	}

	private void exitProgram(WindowEvent evt) {
		if (gameController != null) {
			gameController.stop();
		}
		Setting.getInstance().getScoreStorageInterface().stop();
		log.info("Exiting game");
		System.exit(0);
	}
}