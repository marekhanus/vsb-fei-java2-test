package lab.cleaning;

import java.util.List;

public class Shelf {

	private int width;
	
	
	public Shelf(int width) {
		this.width = width;
	}


	/**
	 * Create a method that puts the boxes in the same
	 * order they came in.
	 * 
	 * Each box will have its y coordinate set to zero and
	 * its x coordinate set to the end of the previous box.
	 * You can use the moveToX method.
	 * 
	 * If the boxes do not fit on the shelf (the coordinate 
	 * of the box plus its width is greater than the width
	 * of the shelf) throw a NotEnoughSpaceException
	 * 
	 * @param boxes
	 * @throws NotEnoughSpaceException
	 */
	public void insert(List<Box> boxes) {
		// TODO:
	}
}
