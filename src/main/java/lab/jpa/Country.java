package lab.jpa;

import lab.Tools;

/**
 * Create a valid entity (entity class) from this class 
 * using annotations from lombok library.
 * The class will have:
 * - a constructor without parameters
 * - getters and setters for all instance variables
 * - a toString method
 * - don't forget about ID
 */
//TODO:
public class Country {

	private Long id;
	private String name;
		
	public static Country generate() {
		Country c = new Country();
		c.name = Tools.randomCountry();
		return c;
	}
}
