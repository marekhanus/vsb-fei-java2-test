package lab.game;

import java.util.Random;

import org.apache.logging.log4j.LogManager;
import org.apache.logging.log4j.Logger;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;
import lab.Setting;

public class Ufo extends WorldEntity implements Collisionable {

	private static Logger log = LogManager.getLogger(Ufo.class);

	private static final Random RANDOM = new Random();
	private Image image;
	private Point2D velocity;

	public Ufo(World world) {
		this(world,
				new Point2D(RANDOM.nextDouble(world.getWidth()),
						RANDOM.nextDouble(0, world.getHeight() * Setting.getInstance().getUfoMinPercentageHeight())),
				new Point2D(RANDOM.nextDouble(Setting.getInstance().getUfoMinSpeed(),
						Setting.getInstance().getUfoMaxSpeed()), 0));
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
		log.debug("Ufo chaned direction.");
	}

	@Override
	public void simulate(double deltaT) {
		position = position.add(velocity.multiply(deltaT));
		position = new Point2D(position.getX() % world.getWidth(), position.getY());
		if (position.getX() < -getImage().getWidth()) {
			position = new Point2D(world.getWidth(), position.getY());
		}
		log.trace("Ufo position: {}", position);
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
		log.trace("Ufo hitted by {}.", another);
		if (another instanceof BulletAnimated || another instanceof Bullet) {
			world.remove(this);
		}
	}
}
