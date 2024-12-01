package lab;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ufo extends WorldEntity implements Collisionable {

	private static final Random RANDOM = new Random();
	private Image image;
	private Point2D velocity;

	public Ufo(World world) {
		this(world, new Point2D(RANDOM.nextDouble(world.getWidth()), RANDOM.nextDouble(0, world.getHeight() * 0.3)),
				new Point2D(RANDOM.nextDouble(70, 150), 0));
	}

	public Ufo(World world, Point2D position, Point2D velocity) {
		super(world, position);
		this.velocity = velocity;
	}

	private Image getImage() {
		if (image == null) {
			image = new Image(Ufo.class.getResourceAsStream("ufo-small.gif"));
		}
		return image;

	}

	@Override
	public void drawInternal(GraphicsContext gc) {
		gc.drawImage(getImage(), getPosition().getX(), getPosition().getY());
	}

	public void changeDirection() {
		velocity = velocity.multiply(-1);
	}

	@Override
	public void simulate(double deltaT) {
		position = position.add(velocity.multiply(deltaT));
		position = new Point2D(position.getX() % world.getWidth(), position.getY());
		if (position.getX() < -getImage().getWidth()) {
			position = new Point2D(world.getWidth(), position.getY());
		}
	}

	@Override
	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), getImage().getWidth(), getImage().getHeight());
	}

	@Override
	public boolean intersect(Rectangle2D another) {
		return getBoundingBox().intersects(another);
	}

	@Override
	public void hitBy(Collisionable another) {
		if (another instanceof BulletAnimated || another instanceof Bullet) {
			world.remove(this);
		}
	}
}
