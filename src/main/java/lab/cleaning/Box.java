package lab.cleaning;

import lab.Tools;

/**
 * TODO:
 * Refactor this class using the Lombok library (where possible).
 */
public class Box {

	private int x;
	private int y;
	private int width;
	private int height;

	public Box(int x, int y, int width, int height) {
		this.x = x;
		this.y = y;
		this.width = width;
		this.height = height;
	}

	public int getX() {
		return x;
	}

	public void setX(int x) {
		this.x = x;
	}

	public int getY() {
		return y;
	}

	public void setY(int y) {
		this.y = y;
	}

	public int getWidth() {
		return width;
	}

	public void setWidth(int width) {
		this.width = width;
	}

	public int getHeight() {
		return height;
	}

	public void setHeight(int height) {
		this.height = height;
	}
	
	public void moveToX(int x) {
		setX(x);
		setY(0);
	}

	@Override
	public String toString() {
		return "Box [x=" + x + ", y=" + y + ", width=" + width + ", height=" + height + "]";
	}
	
	public static Box generate() {
		return new Box(Tools.RANDOM.nextInt(20),
				Tools.RANDOM.nextInt(20),
				Tools.RANDOM.nextInt(5),
				Tools.RANDOM.nextInt(5));
	}
	
}
