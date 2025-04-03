package lab.jpa;

import java.util.List;

import lab.Tools;

/**
 * Create a valid entity (entity class) from this class 
 * using annotations from Lombok library.
 * The class will have:
 * - a constructor without parameters
 * - getters and setters for all instance variables
 * - a toString method
 * - ensure that the list of buildings is not part of the listing by using toString 
 * - don't forget about ID
 * - don't forget about relation to buildings
 */
//TODO:
public class City {

	private Long id;
	private String name;
	private List<Building> buildings;
	
	public static City generate() {
		City c = new City();
		c.name = Tools.randomCity();
		return c;
	}
}
