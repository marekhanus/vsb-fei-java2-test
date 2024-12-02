package lab;

import java.util.List;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Slider;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class GameController {

    @FXML
    private Button btnGenerateScore;

    @FXML
    private Button btnLoadAll;

    @FXML
    private Button btnLoadFirstTen;

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


	private DrawingThread timer;

    @FXML
    private Label hits;
    private int hitcount = 0;
	@FXML
	void fire(ActionEvent event) {
		double angle = timer.getWorld().getCannon().getAngle();
		double angleRad = Math.toRadians(angle);
		double speedValue = speed.getValue();
		Point2D velocity = new Point2D(
				Math.cos(angleRad)*speedValue, 
				Math.sin(angleRad)*speedValue); 
		BulletAnimated bulletAnimated = new BulletAnimated(
				timer.getWorld(), 
				timer.getWorld().getCannon(),
				timer.getWorld().getCannon().getPosition(), 
				velocity, World.GRAVITY);
		timer.getWorld().add(bulletAnimated);
		bulletAnimated.addHitListener(this::increaseHits);
		bulletAnimated.addHitListener(
				() -> System.out.println("au!!!!"));
	}

    @FXML
    void btnGenerateScoreAction(ActionEvent event) {
    	Score score = Score.generate();
    	this.scores.getItems().add(score);
    	DbConnector.insertScore(score);
    }

    @FXML
    void btnLoadAllAction(ActionEvent event) {
    	updateScoreTable(DbConnector.getAll());
    }

    @FXML
    void btnLoadFirstTenAction(ActionEvent event) {
    	updateScoreTable(DbConnector.getFirstTen());
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
		timer = new DrawingThread(canvas);
		timer.start();
		angle.valueProperty().addListener(
				(observable, oldValue, newValue) -> 
					timer.getWorld().getCannon().setAngle(newValue.doubleValue()));
		
		nickColumn.setCellValueFactory(new PropertyValueFactory<>("name"));
		pointsColumn.setCellValueFactory(new PropertyValueFactory<>("points"));
		
		initDB();
	}
	
	private void initDB() {
		//Stream.generate(Score::generate).limit(10).toList();
		DbConnector.createTable();
		scores.getItems().addAll(DbConnector.getAll());
	}

	public void stop() {
		timer.stop();
	}

}
