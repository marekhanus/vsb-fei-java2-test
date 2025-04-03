package lab.travel;

import java.time.LocalDateTime;

import lab.Tools;

/**
 * TODO:
 * Using the Lombok library, annotate the class with:
 * - getters and setters for all instance variables
 * - listing using the toString method, which work correctly with inheritance
 * - the hashcode method, which works correctly with inheritance
 * - the equals method, which works correctly with inheritance
 */
public class BoatTicket extends Ticket {

	private int boatTripLength;
	private boolean businessClass;

	public BoatTicket(String name, int price, LocalDateTime time, int boatTripLength, boolean businessClass) {
		super(name, price, time);
		this.boatTripLength = boatTripLength;
		this.businessClass = businessClass;
	}

	public static BoatTicket generate() {
		return new BoatTicket(Tools.randomFullName(), Tools.RANDOM.nextInt(500, 2500), Tools.randomDateTimeInFuture(),
				Tools.RANDOM.nextInt(1000, 10000), Tools.RANDOM.nextBoolean());
	}
}
