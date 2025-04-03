package lab.travel;

import lab.Tools;

/**
 * Using the Lombok library, annotate the class with:
 * - getters for all instance variables
 * - setter for password only
 * - an output using the toString method that does not contain the password
 * - the hashcode method
 * - the equals method
 * - constructor with all parameters
 * - constructor without parameters
 * - possibility of using the builder design pattern
 */
//TODO:
public class Person {

	private String email;
	private String firstName;
	private String lastName;

	private String password;

	public static Person generate() {
		Person p = new Person();
		p.firstName = Tools.randomFistName();
		p.lastName = Tools.randomLastName();
		p.email = Tools.randomEmail();
		p.password = Tools.randomPassword();
		return p;
		
	}
	
	
}
