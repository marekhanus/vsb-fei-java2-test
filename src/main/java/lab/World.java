package lab;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;

public class World {

	private final double width;

	private final double hight;

	private final Bullet bullet;
	private final BulletAnimated bullet2;
	private final Cannon cannon;

	public World(double width, double hight) {
		this.width = width;
		this.hight = hight;
		this.bullet = new Bullet(new Point2D(0, 0), new Point2D(30, 30), new Point2D(0, -9.81));
		this.bullet2 = new BulletAnimated(new Point2D(0, 0), new Point2D(30, 70), new Point2D(0, -9.81));
		this.cannon = new Cannon(new Point2D(0, 0), 45);
	}

	public void draw(GraphicsContext gc) {
		gc.clearRect(0, 0, width, hight);

		gc.save();
		// Change coordinate system to human like
		gc.scale(1, -1);
		gc.translate(0, -hight);
		this.bullet.draw(gc);
		this.bullet2.draw(gc);
		this.cannon.draw(gc);
		gc.restore();
	}

	public void simulate(double deltaT) {
		this.bullet.simulate(deltaT);
		this.bullet2.simulate(deltaT);
		this.cannon.simulate(deltaT);
	}
}