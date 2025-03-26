package lab.jpa;

import java.util.List;
import java.util.stream.Stream;

import lab.Tools;

public class ArchitectManager {

	public static void main(String[] args) {
		new  ArchitectManager().run();
	}

	/**
	 * Can be modified for testing
	 */
	public void run() {
		JpaConnector connector = new JpaConnector();
		countries(connector);
		buildingsAndCityies(connector);
	}
	
	public void countries(JpaConnector connector) {
		
		for (Country country: generateCountries()) {
			connector.save(country);
		}
		for(Country county: connector.getAllCountries()) {
			System.out.println(county);
		}
		connector.delete(Tools.randomElementFrom(connector.getAllCountries()));
	}
	
	public List<Country> generateCountries(){
		return Stream.generate(Country::generate).limit(10).toList(); 
	}

	public void buildingsAndCityies(JpaConnector connector) {
		for(City city : generateCities()) {
			connector.save(city);
		}
		
		for(Building building: generateBuildings(connector.getAllCities())) {
			connector.save(building);
		}
		
		connector.getEntityManager().clear();
		
		for(City city: connector.getAllCities()) {
			System.out.println("City: " + city);
			//TODO: print out all buildings for city
		}
		
		for (Building building : connector.executeCriteriaQuery(connector.createCriteriaQueryForBuildingsWithFloorRange(200, 300))) {
			System.out.println(building);
		}
		
	}
	public List<City> generateCities(){
		return Stream.generate(City::generate).limit(5).toList(); 
	}

	public List<Building> generateBuildings(List<City> cities){
		return Stream.generate(() -> Building.generate(cities)).limit(20).toList(); 
	}


}
