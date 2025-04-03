package lab.cleaning;

import java.io.Serial;

// TODO: Make the class a valid exception.
public class NotEnoughSpaceException {

	@Serial
	private static final long serialVersionUID = -9174136778620509979L;

	private final Box box;

	public NotEnoughSpaceException(Box box) {
		this.box = box;
	}

	public Box getBox() {
		// TODO: Replace with Lombok annotation.
		return box;
	}

}
