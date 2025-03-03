package lab.game;

import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;

import javafx.geometry.Point2D;
import javafx.scene.canvas.GraphicsContext;
import lab.Setting;

public class World {

	public static final Point2D GRAVITY = new Point2D(0, Setting.getInstance().getGravity());
	private final double width;

	private final double height;

	private List<DrawableSimulable> entities;
	private Collection<DrawableSimulable> entitiesToRemove = new LinkedList<>();
	private Collection<DrawableSimulable> entitiesToAdd = new LinkedList<>();

	private Cannon cannon;
//	private BulletAnimated bulletAnimated;

	public World(double width, double height) {
		this.width = width;
		this.height = height;
		entities = new ArrayList<>();
		cannon = new Cannon(this, new Point2D(0, height - 20), -45);
		entities.add(cannon);
		entities.add(new Bullet(this, new Point2D(0, height),
				new Point2D(Setting.getInstance().getNormalBulletSpeed(), -Setting.getInstance().getNormalBulletSpeed()),
				GRAVITY));
		for (int i = 0; i < Setting.getInstance().getNumberOfUfos(); i++) {
			entities.add(new Ufo(this));
		}
	}

	public void draw(GraphicsContext gc) {
		gc.clearRect(0, 0, width, height);

		gc.save();
		for (DrawableSimulable entity : entities) {
			entity.draw(gc);
		}
		gc.restore();
	}

	public void simulate(double deltaT) {
		for (DrawableSimulable entity : entities) {
			entity.simulate(deltaT);
		}
		for (int i = 0; i < entities.size(); i++) {
			if (entities.get(i) instanceof Collisionable c1) {
				for (int j = i + 1; j < entities.size(); j++) {
					if (entities.get(j) instanceof Collisionable c2) {
						if (c1.intersect(c2.getBoundingBox())) {
							c1.hitBy(c2);
							c2.hitBy(c1);
						}
					}
				}
			}
		}
		entities.removeAll(entitiesToRemove);
		entities.addAll(entitiesToAdd);
		entitiesToAdd.clear();
		entitiesToRemove.clear();
	}

	public double getWidth() {
		return width;
	}

	public void add(DrawableSimulable entity) {
		entitiesToAdd.add(entity);
	}

	public void remove(DrawableSimulable entity) {
		entitiesToRemove.add(entity);

	}

	public double getHeight() {
		return height;
	}

	public Cannon getCannon() {
		return cannon;
	}

//	public BulletAnimated getBulletAnimated() {
//		return bulletAnimated;
//	}
//
}