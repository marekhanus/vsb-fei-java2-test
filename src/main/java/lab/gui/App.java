package lab.gui;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import lab.Setting;

/**
 * Class <b>App</b> - extends class Application and it is an entry point of the
 * program
 * 
 * @author Java I
 */
public class App extends Application {

	private static Logger log = LogManager.getLogger(App.class);
	private GameController gameController;

	public static void main(String[] args) {
		log.info("Application lauched");
		Setting.configure(Setting.getInstanceForHardcoreGame().toBuilder().ufoMinPercentageHeight(0.4).build());
		launch(args);
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
		log.info("Exiting game");
		System.exit(0);
	}
}