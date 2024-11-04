package lab;

import javafx.animation.AnimationTimer;
import javafx.scene.canvas.Canvas;
import javafx.scene.canvas.GraphicsContext;

public class DrawingThread extends AnimationTimer {

	private final Canvas canvas;
	private final GraphicsContext gc;
	private final World world;
	private long lastTime;

	public DrawingThread(Canvas canvas) {
		this.canvas = canvas;
		this.gc = canvas.getGraphicsContext2D();
		world = new World(canvas.getWidth(), canvas.getHeight());
		lastTime = System.nanoTime();
	}

	/**
	 * Draws objects into the canvas. Put you code here.
	 */
	@Override
	public void handle(long now) {
		double deltaT = (now - lastTime) / 1e9;
		// call draw on world
		this.world.draw(gc);
		// call simulate on world
		this.world.simulate(deltaT);
		lastTime = now;
	}

	public World getWorld() {
		return world;
	}

}
