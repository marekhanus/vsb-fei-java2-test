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
		bullet = new Bullet(this, new Point2D(0, 0), new Point2D(30, 30), new Point2D(0, -9.81));
		bullet2 = new BulletAnimated(this, new Point2D(0, 0), new Point2D(30, 70), new Point2D(0, -9.81));
		cannon = new Cannon(this, new Point2D(0, 0), 45);
	}

	public void draw(GraphicsContext gc) {
		gc.clearRect(0, 0, width, hight);

		gc.save();
		// Change coordinate system to human like
		gc.scale(1, -1);
		gc.translate(0, -hight);
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
}