package lab;

import java.util.Random;

import javafx.geometry.Point2D;
import javafx.geometry.Rectangle2D;
import javafx.scene.canvas.GraphicsContext;
import javafx.scene.image.Image;

public class Ufo {

	private static final Random RANDOM = new Random();
	private Image image = new Image(this.getClass().getResourceAsStream("ufo-small.gif"));
	private final World world;
	private Point2D position;
	private Point2D velocity;

	public Ufo(World world) {
		this(world, 
				new Point2D(RANDOM.nextDouble(world.getWidth()), 
						RANDOM.nextDouble(0, world.getHeight()*0.3)), 
				new Point2D(RANDOM.nextDouble(70, 150), 0));
	}

	public Ufo(World world, Point2D position, Point2D velocity) {
		this.world = world;
		this.position = position;
		this.velocity = velocity;
	}

	public void draw(GraphicsContext gc) {
		gc.drawImage(image, getPosition().getX(), getPosition().getY());
	}

	public void changeDirection(){
		velocity = velocity.multiply(-1);
	}
	public void simulate(double deltaT) {
		position = position.add(velocity.multiply(deltaT));
		position = new Point2D(position.getX()%world.getWidth(), position.getY());
	}

	protected Point2D getPosition() {
		return position;
	}
	
	public Rectangle2D getBoundingBox() {
		return new Rectangle2D(position.getX(), position.getY(), image.getWidth(), image.getHeight()); 
	}
}
