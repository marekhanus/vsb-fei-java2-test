package lab.gui;

import java.util.ArrayList;
import java.util.List;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.collections.ListChangeListener;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.SelectionMode;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseEvent;
import lab.Setting;
import lab.data.Level;
import lab.data.Score;
import lab.game.BulletAnimated;
import lab.game.DrawingThread;
import lab.game.World;

public class GameController {

	private static Logger log = LogManager.getLogger(GameController.class);

	@FXML
	private Button btnGenerateScore;

	@FXML
	private Button btnLoadAll;

	@FXML
	private Button btnLoadFirstTen;

	@FXML
	private Button btnDelete;

	@FXML
	private Slider angle;

	@FXML
	private Slider speed;

	@FXML
	private Canvas canvas;

	@FXML
	private TableView<Score> scores;

	@FXML
	private TableColumn<Score, String> nickColumn;

	@FXML
	private TableColumn<Score, Integer> pointsColumn;

	@FXML
	private TableColumn<Score, Level> levelColumn;

	private DrawingThread timer;

	@FXML
	private Label hits;
	private int hitcount = 0;

	@FXML
	void fire(ActionEvent event) {
		double angle = timer.getWorld().getCannon().getAngle();
		double angleRad = Math.toRadians(angle);
		double speedValue = speed.getValue();
		Point2D velocity = new Point2D(Math.cos(angleRad) * speedValue, Math.sin(angleRad) * speedValue);
		BulletAnimated bulletAnimated = new BulletAnimated(timer.getWorld(), timer.getWorld().getCannon(),
				timer.getWorld().getCannon().getPosition(), velocity, World.GRAVITY);
		timer.getWorld().add(bulletAnimated);
		bulletAnimated.addHitListener(this::increaseHits);
		bulletAnimated.addHitListener(() -> log.info("au!!!!"));
	}

	@FXML
	void btnGenerateScoreAction(ActionEvent event) {
		Score score = Score.generate();
		this.scores.getItems().add(score);
		Setting.getInstance().getScoreStorageInterface().save(score);
	}

	@FXML
	void btnLoadAllAction(ActionEvent event) {
		updateScoreTable(Setting.getInstance().getScoreStorageInterface().getAll());
	}

	@FXML
	void btnLoadFirstTenAction(ActionEvent event) {
		updateScoreTable(Setting.getInstance().getScoreStorageInterface().getFirstTen());
	}

	@FXML
	void btnDeleteAction(ActionEvent event) {
		List<Score> selectedScores = new ArrayList<>(scores.getSelectionModel().getSelectedItems());
		Setting.getInstance().getScoreStorageInterface().delete(selectedScores);
		updateScoreTable(Setting.getInstance().getScoreStorageInterface().getAll());
	}

	@FXML
	void keyPressed(KeyEvent event) {
		log.info(event.getCode());
		event.consume();
	}

	@FXML
	void canvasClicked(MouseEvent event) {
		canvas.requestFocus();
	}

	@FXML
	void keyReleased(KeyEvent event) {

	}

	private void updateScoreTable(List<Score> scores) {
		this.scores.getItems().clear();
		this.scores.getItems().addAll(scores);
	}

	private void updateHits() {
		hits.setText(String.format("Hit count: %03d", hitcount));
	}

	private void increaseHits() {
		hitcount++;
		updateHits();
	}

	@FXML
	void initialize() {
		assert angle != null : "fx:id=\"angle\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert speed != null : "fx:id=\"speed\" was not injected: check your FXML file 'gameWindow.fxml'.";
		speed.setMin(Setting.getInstance().getBulletMinSpeed());
		speed.setMax(Setting.getInstance().getBulletMaxSpeed());
		timer = new DrawingThread(canvas);
		timer.start();
		angle.valueProperty().addListener(
				(observable, oldValue, newValue) -> timer.getWorld().getCannon().setAngle(newValue.doubleValue()));

		nickColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
		levelColumn.setCellValueFactory(new PropertyValueFactory<>("level"));

		btnDelete.setDisable(true);
		scores.getSelectionModel().setSelectionMode(SelectionMode.MULTIPLE);
		scores.getSelectionModel().getSelectedItems()
				.addListener((ListChangeListener.Change<? extends Score> change) -> 
					btnDelete.setDisable(change.getList().isEmpty()));

		initStorage();
		log.info("Screeen initialized.");
		canvas.requestFocus();
	}

	private void initStorage() {
		Setting.getInstance().getScoreStorageInterface().init();
		scores.getItems().addAll(Setting.getInstance().getScoreStorageInterface().getAll());
	}

	public void stop() {
		timer.getWorld().pringDestroylog();
		timer.stop();
	}

}
