package lab.jpa;

import java.util.List;

import jakarta.persistence.EntityManager;
import jakarta.persistence.criteria.CriteriaQuery;

/**
 * Complete the class to work as usually: store and retrieve entities using JPA from and to DB.
 */
public class JpaConnector {

	private final EntityManager em;

	public JpaConnector() {
		//TODO:
	}

	public List<Country> getAllCountries() {
		//TODO:
		return null;
	}

	public Country save(Country entity) {
		//TODO:
		return null;
	}

	public void delete(Country entity) {
		//TODO:
	}

	public void stop() {
		em.close();
	}

	public EntityManager getEntityManager() {
		return em;
	}

	public Country find(long id) {
		return em.find(Country.class, id);
	}

	
	public List<City> getAllCities() {
		//TODO:
		return null;
	}

	public City save(City entity) {
		//TODO:
		return null;
	}

	public List<Building> getAllBuildings() {
		//TODO:
		return null;
	}

	public Building save(Building entity) {
		//TODO:
		return null;
	}
	
	
	/**
	 * Execute the passed criteria query for buildings (follows 
	 * the method createCriteriaQueryForBuildingsWithFloorRange) 
	 * 
	 * @param query
	 * @return list of buildings satisfying the query 
	 */
	public List<Building> executeCriteriaQuery(CriteriaQuery<Building> query){
		//TODO:
		return null;
	}
	
	/**
	 * Create a criteria query for buildings that finds buildings with
	 * the number of floors in a given range. 
	 */
	public CriteriaQuery<Building> createCriteriaQueryForBuildingsWithFloorRange(int minFloors, int maxFloors){
		//TODO:
		return null;
	}

}
