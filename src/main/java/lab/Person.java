package lab;

import java.time.Duration;
import java.time.LocalDate;
import java.time.Period;
import java.time.temporal.ChronoUnit;

public class Person {

	private String firstName;
	private String lastName;
	private LocalDate dayOfBirth;

	public Person(String firstName, String lastName, LocalDate dayOfBirth) {
		super();
		this.firstName = firstName;
		this.lastName = lastName;
		this.dayOfBirth = dayOfBirth;
	}

	public String getLastName() {
		return lastName;
	}

	public void setLastName(String lastName) {
		this.lastName = lastName;
	}

	public String getFirstName() {
		return firstName;
	}

	public LocalDate getDayOfBirth() {
		return dayOfBirth;
	}
	
	public int getAge() {
		return Period.between(dayOfBirth, LocalDate.now()).getYears();
	}
	
	public static void main(String[] args) {
		Person p = new Person("", "", LocalDate.now().minusDays(3700));
		System.out.println(p.getAge());
		System.out.println(p.getDaysToBirthday());
	}
	public LocalDate get50thBirthDay() {
		return dayOfBirth.plusYears(50);
	}
	
	public long getDaysToBirthday() {
		LocalDate nextBirthday = dayOfBirth.withYear(LocalDate.now().getYear());
		if(nextBirthday.isBefore(LocalDate.now())){
			nextBirthday = nextBirthday.plusYears(1);
		}
		return ChronoUnit.DAYS.between(LocalDate.now(), nextBirthday);
	}
}
