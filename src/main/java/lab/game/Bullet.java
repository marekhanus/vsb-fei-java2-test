package lab.game;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.paint.Color;

public class Bullet extends WorldEntity implements Collisionable{
	private static final double SIZE = 20;

	protected final Point2D acceleration;
	protected Point2D velocity;

	public Bullet(World world, Point2D position, Point2D velocity, Point2D acceleration) {
		super(world, position);
		this.velocity = velocity;
		this.acceleration = acceleration;
	}

	@Override
	public void drawInternal(GraphicsContext gc) {
		gc.setFill(Color.SILVER);
		gc.fillOval(position.getX(), position.getY(), SIZE, SIZE);
	}

	@Override
	public void simulate(double deltaT) {
		position = position.add(velocity.multiply(deltaT));
		velocity = velocity.add(acceleration.multiply(deltaT));
	}

	@Override
	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), SIZE, SIZE);
	}

	@Override
	public boolean intersect(Rectangle2D another) {
		return getBoundingBox().intersects(another);
	}

	@Override
	public void hitBy(Collisionable another) {
		
	}

	public void setVelocity(Point2D velocity) {
		this.velocity = velocity;
	}

}
