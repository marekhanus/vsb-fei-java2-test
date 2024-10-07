package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class World {

	private final double width;

	private final double height;

	private final Bullet bullet;
	private final BulletAnimated bullet2;
	private final Cannon cannon;

	public World(double width, double height) {
		this.width = width;
		this.height = height;
		bullet = new Bullet(this, new Point2D(0, height), new Point2D(30, -30), new Point2D(0, 9.81));
		bullet2 = new BulletAnimated(this, new Point2D(0, height), new Point2D(50, -80), new Point2D(0, 9.81));
		cannon = new Cannon(this, new Point2D(0, height-20), -45);
	}

	public void draw(GraphicsContext gc) {
		gc.clearRect(0, 0, width, height);

		gc.save();
		bullet.draw(gc);
		bullet2.draw(gc);
		cannon.draw(gc);
		gc.restore();
	}

	public void simulate(double deltaT) {
		bullet.simulate(deltaT);
		bullet2.simulate(deltaT);
		cannon.simulate(deltaT);
	}

	public double getWidth() {
		return width;
	}

	public double getHeight() {
		return height;
	}
	
}