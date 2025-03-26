package lab.cleaning;

import java.io.Serial;

/**
 * Make the class a valid exception.
 */
//TODO:
public class NotEnoughtSpaceException {

	@Serial
	private static final long serialVersionUID = -9174136778620509979L;

	private final Box box;

	public NotEnoughtSpaceException(Box box) {
		//TODO: call parent with message "No enought space for all boxes."
		this.box = box;
	}

	public Box getBox() {
		return box;
	}

}
