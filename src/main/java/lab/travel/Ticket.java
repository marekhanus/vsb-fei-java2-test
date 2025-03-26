package lab.travel;

import java.time.LocalDateTime;

import lab.Tools;

/**
 * Using the lombok library, extend the class with:
 * - getters and setters for all instance variables
 * - listing using the toString method
 * - a the hashcode method
 * - the equals method
 */
//TODO:
public class Ticket {

	private String name;
	private int price;
	private LocalDateTime time;
	
	public Ticket(String name, int price, LocalDateTime time) {
		this.name = name;
		this.price = price;
		this.time = time;
	}
	
	public static Ticket generate() {
		return new Ticket(Tools.randomFullName(), Tools.RANDOM.nextInt(100, 500), Tools.randomDateTimeInFuture());
	}
	
}
