package lab;

import javafx.beans.value.ChangeListener;
import javafx.beans.value.ObservableValue;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Point2D;
import javafx.scene.canvas.Canvas;
import javafx.scene.control.Slider;

public class GameController {

	@FXML
	private Slider angle;

	@FXML
	private Slider speed;

	@FXML
	private Canvas canvas;

	private DrawingThread timer;

	@FXML
	void fire(ActionEvent event) {
		double angle = timer.getWorld().getCannon().getAngle();
		double angleRad = Math.toRadians(angle);
		double speedValue = speed.getValue();
		timer.getWorld().getBulletAnimated().reload();
		Point2D velocity = new Point2D(
				Math.cos(angleRad)*speedValue, 
				Math.sin(angleRad)*speedValue); 
		timer.getWorld().getBulletAnimated().setVelocity(velocity);
	}

	@FXML
	void initialize() {
		assert angle != null : "fx:id=\"angle\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert canvas != null : "fx:id=\"canvas\" was not injected: check your FXML file 'gameWindow.fxml'.";
		assert speed != null : "fx:id=\"speed\" was not injected: check your FXML file 'gameWindow.fxml'.";
		timer = new DrawingThread(canvas);
		timer.start();
		angle.valueProperty().addListener(new ChangeListener<Number>() {
			@Override
			public void changed(ObservableValue<? extends Number> observable, Number oldValue, Number newValue) {
				timer.getWorld().getCannon().setAngle(newValue.doubleValue());
			}
		});
	}

	public void stop() {
		timer.stop();
	}

}
