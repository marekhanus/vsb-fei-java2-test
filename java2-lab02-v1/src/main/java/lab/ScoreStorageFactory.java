package lab;

import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Random;
import java.util.ServiceLoader;

import cz.vsb.fei.java2.lab02.common.ScoreStorageInterface;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.stage.Modality;
import javafx.scene.control.Alert.AlertType;

public class ScoreStorageFactory {

	private static ScoreStorageInterface instance;

	private ScoreStorageFactory() {
		/* hide public one constructor */
	}

	public static ScoreStorageInterface getInstance() {
		if (instance == null) {
			List<ScoreStorageInterface> availableImplementations = new ArrayList<>();
			ServiceLoader.load(ScoreStorageInterface.class).forEach(availableImplementations::add);
			if (availableImplementations.isEmpty()) {
				throw new NoSuchElementException(
						"Service loader did not find any implementation of interface ScoreStorageInterface.");
			}
			instance = availableImplementations.get(new Random().nextInt(availableImplementations.size()));
			Alert info = new Alert(AlertType.INFORMATION,
					String.format("Storage %s selected.", instance.getClass().getName()), ButtonType.OK);
			info.initModality(Modality.WINDOW_MODAL);
			info.showAndWait();
		}
		return instance;
	}
}
