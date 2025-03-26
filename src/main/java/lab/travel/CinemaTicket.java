package lab.travel;

import java.time.LocalDateTime;

import lab.Tools;

/**
 * Using the lombok library, extend the class with:
 * - getters and setters for all instance variables
 * - listing using the toString method, which work correctly with inheritance
 * - a the hashcode method, which works correctly with inheritance
 * - the equals method, which works correctly with inheritance
 */
//TODO:
public class CinemaTicket extends Ticket {

	private String cinemaHall;
	private char row;
	private int seat;

	public CinemaTicket(String name, int price, LocalDateTime time, String cinemaHall, char row, int seat) {
		super(name, price, time);
		this.cinemaHall = cinemaHall;
		this.row = row;
		this.seat = seat;
	}

	public static CinemaTicket generate() {
		return new CinemaTicket(Tools.randomFullName(), Tools.RANDOM.nextInt(100, 250),
				Tools.randomDateTimeInFuture(), "SÁL " + Tools.RANDOM.nextInt(1, 8),
				(char) ('A' + Tools.RANDOM.nextInt(15)), Tools.RANDOM.nextInt(1, 20));
	}

}
