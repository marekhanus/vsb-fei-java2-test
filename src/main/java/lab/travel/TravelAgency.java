package lab.travel;

import java.time.LocalDateTime;
import java.util.List;

public class TravelAgency {

	public static void main(String[] args) {
		new TravelAgency().run();
	}

	public void run() {
		List<Person> customers = generateCustomers();
		printCustomers(customers);
		List<Ticket> tickets = generateTickets();
		printTickets(tickets);
		testEquals();
	}

	/**
	 * Generate a list of 10 people using the generate method
	 * 
	 * @return list of 10 people
	 */
	public List<Person> generateCustomers() {
		// TODO:
		return null;
	}

	/**
	 * Generate a list of 5 tickets, 5 boat tickets and 5 cinema tickets using the
	 * generate method
	 * 
	 * @return a list of 15 tickets and its descendants
	 */
	public List<Ticket> generateTickets() {
		// TODO:
		return null;
	}

	public void printCustomers(List<Person> persons) {
		for (Person person : persons) {
			System.out.println(person);
		}
	}

	public void printTickets(List<Ticket> tickets) {
		for (Ticket ticket : tickets) {
			System.out.println(ticket);
		}
	}

	/**
	 * Can be modified for testing
	 */
	public void testEquals() {
		testEquals(new Ticket("a", 10, LocalDateTime.of(2025, 4, 1, 14, 15)),
				new Ticket("a", 10, LocalDateTime.of(2025, 4, 1, 14, 15)));
	}

	private void testEquals(Ticket t1, Ticket t2) {
		System.out.printf("Tickets:%n\t%s%n\t%s%n  are equal:%b%n", t1, t2, t1.equals(t2));
	}

}
