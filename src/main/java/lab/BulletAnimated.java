package lab;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BulletAnimated {

	private static final double SIZE = 40;
	private final World world;
	private Image image = new Image(this.getClass().getResourceAsStream("fireball-transparent.gif"));

	public BulletAnimated(World world, Point2D position, Point2D velocity, Point2D acceleration) {
		this.world = world;
		this.position = position;
		this.velocity = velocity;
		this.acceleration = acceleration;
	}

	public void draw(GraphicsContext gc) {
		gc.drawImage(image, getPosition().getX(), getPosition().getY(), SIZE, SIZE);
		gc.strokeRect(position.getX(), position.getY(), SIZE, SIZE);
	}

	private final Point2D acceleration;

	private Point2D position;
	private Point2D velocity;

	public void simulate(double deltaT) {
		position = position.add(velocity.multiply(deltaT));
		velocity = velocity.add(acceleration.multiply(deltaT));
	}

	protected Point2D getPosition() {
		return position;
	}

	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), SIZE,SIZE); 
	}

}
