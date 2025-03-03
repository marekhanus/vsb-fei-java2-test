package lab.game;

import java.util.ArrayList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class BulletAnimated extends Bullet {

	private static final double SIZE = 40;
	private final Point2D initVelocity;
	private Cannon cannon;
	private static Image image = new Image(BulletAnimated.class.getResourceAsStream("fireball-transparent.gif"));
	private List<HitListener> hitListeners = 
			new ArrayList<>();

	public BulletAnimated(World world, Cannon cannon, Point2D position, Point2D velocity, Point2D acceleration) {
		super(world, position, velocity, acceleration);
		this.initVelocity = velocity;
		this.cannon = cannon;
	}

	@Override
	public void drawInternal(GraphicsContext gc) {
		gc.drawImage(image, getPosition().getX(), getPosition().getY(), SIZE, SIZE);
		gc.strokeRect(position.getX(), position.getY(), SIZE, SIZE);
	}

	@Override
	public void hitBy(Collisionable another) {
		if (another instanceof Ufo) {
			world.remove(this);
			fireUfoDestroyed();
		}
	}

	public void reload() {
		position = cannon.getPosition();
		velocity = new Point2D(0, 0);
	}

	public boolean addHitListener(HitListener e) {
		return hitListeners.add(e);
	}

	public boolean removeHitListener(HitListener o) {
		return hitListeners.remove(o);
	}
	
	private void fireUfoDestroyed() {
		for (HitListener hitListener : hitListeners) {
			hitListener.ufoDestroyed();
		}
	}
	
}