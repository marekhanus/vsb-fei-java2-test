package lab.jpa;

import java.util.List;

import lab.Tools;

/**
 * Create a valid entity (entity class) from this class 
 * using annotations from lombok library.
 * The class will have:
 * - a constructor without parameters
 * - getters and setters for all instance variables
 * - a toString method
 * - don't forget about ID
 * - don't forget about relation to city
 */
//TODO:
public class Building {

	private Long id;
	private String name;
	private double area;
	private int floorsCount;
	private City city;
	
	public static Building generate(List<City> cities) {
		Building b = new Building();
		b.name = Tools.randomBuilding();
		b.area = Tools.RANDOM.nextDouble(500, 2000);
		b.floorsCount = Tools.RANDOM.nextInt(1, 500);
		b.city = Tools.randomElementFrom(cities);
		return b;
	}
}
