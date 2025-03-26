package lab.jpa;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.stream.Stream;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;

class ArchitectManagerTest {

	StructureHelper helper = StructureHelper.getInstance(ArchitectManagerTest.class);

	// @formatter:off
	@ParameterizedTest
	@CsvSource({ 
		"Country.java,@Entity",
		"Country.java,@NoArgsConstructor",
		"Country.java,@Getter",
		"Country.java,@Setter",
		"Country.java,@ToString",
		"Country.java,@Id",
		"Country.java,@GeneratedValue",
		"Building.java,@Entity",
		"Building.java,@NoArgsConstructor",
		"Building.java,@Getter",
		"Building.java,@Setter",
		"Building.java,@ToString",
		"Building.java,@Id",
		"Building.java,@GeneratedValue",
		"Building.java,@ManyToOne",
		"City.java,@Entity",
		"City.java,@NoArgsConstructor",
		"City.java,@Getter",
		"City.java,@Setter",
		"City.java,@ToString", 
		"City.java,@Id",
		"City.java,@GeneratedValue",
		"City.java,@ToString\\.Exclude",
		"City.java,@OneToMany\\(mappedBy"
	})
	// @formatter:on
	void anotaceTest(String file, String annotation) throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()), new TextFileContains(file, annotation));
	}

	@TestMethodOrder(OrderAnnotation.class)
	@Nested
	class JpaConnectorTests {

		private static JpaConnector connector;

		@BeforeAll
		static void initAll() {
			try {
				connector = new JpaConnector();
			} catch (Exception e) {
				e.printStackTrace();
			}
		}

		@BeforeEach
		void init() {
			connector.getEntityManager().clear();
			if (connector.getEntityManager().getTransaction().isActive()) {
				connector.getEntityManager().getTransaction().rollback();
			}
		}

		@AfterAll
		static void cleanUpAll() {
			connector.stop();
		}

		private boolean same(Country e1, Country e2) throws IllegalAccessException, InvocationTargetException {
			// @formatter:off
			return Objects.equals(helper.executeGetter(e1, "getId"), helper.executeGetter(e2, "getId"))
					&& Objects.equals(helper.executeGetter(e1, "getName"), helper.executeGetter(e1, "getName"));
			// @formatter:on
		}

		private boolean same(Building e1, Building e2) throws IllegalAccessException, InvocationTargetException {
			// @formatter:off
			return Objects.equals(helper.executeGetter(e1, "getId"), helper.executeGetter(e2, "getId"))
					&& Objects.equals(helper.executeGetter(e1, "getName"), helper.executeGetter(e1, "getName"))
					&& Objects.equals(helper.executeGetter(e1, "getArea"), helper.executeGetter(e1, "getArea"))
					&& Objects.equals(helper.executeGetter(e1, "getFloorsCount"), helper.executeGetter(e1, "getFloorsCount"));
			// @formatter:on
		}

		private boolean same(City e1, City e2) throws IllegalAccessException, InvocationTargetException {
			// @formatter:off
			return Objects.equals(helper.executeGetter(e1, "getId"), helper.executeGetter(e2, "getId"))
					&& Objects.equals(helper.executeGetter(e1, "getName"), helper.executeGetter(e1, "getName"));
			// @formatter:on
		}

		private boolean same(List<?> l1, List<?> l2) {
			if (l1 == null) {
				l1 = Collections.emptyList();
			}
			if (l2 == null) {
				l2 = Collections.emptyList();
			}
			return Objects.equals(l1, l2);
		}

		@Test
		@Order(100)
		void jpaCountryInsertTest() throws IllegalAccessException, InvocationTargetException {
			Country entity = Country.generate();
			Country saved = connector.save(entity);
			assertTrue(same(entity, saved), "Countries are not same after save.");
		}

		@Test
		@Order(120)
		void jpaCityInsertTest() throws IllegalAccessException, InvocationTargetException {
			City entity = City.generate();
			City saved = connector.save(entity);
			assertTrue(same(entity, saved), "Cities are not same after save.");
		}

		@Test
		@Order(200)
		void jpaCountryReadTest() {
			List<Country> savedEntities = connector.getAllCountries().stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}

		@Test
		@Order(210)
		void jpaCityReadTest() {
			List<City> savedEntities = connector.getAllCities().stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}

		@Test
		@Order(220)
		void jpaBuildingInsertTest() throws IllegalAccessException, InvocationTargetException {
			Building entity = Building.generate(connector.getAllCities());
			Building saved = connector.save(entity);
			assertTrue(same(entity, saved), "Cities are not same after save.");
		}

		@Test
		@Order(230)
		void jpaBuildingReadTest() {
			List<Building> savedEntities = connector.getAllBuildings().stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}

		@Test
		@Order(250)
		void jpaBuildingCityContainCityTest() throws IllegalAccessException, InvocationTargetException {
			connector.getEntityManager().clear();
			List<Building> savedEntities = connector.getAllBuildings().stream().toList();
			for (Building entity : savedEntities) {
				assertTrue(
						((List<Building>) helper.executeGetter(helper.executeGetter(entity, "getCity"), "getBuildings"))
								.stream().anyMatch(b -> {
									try {
										return this.same(b, entity);
									} catch (IllegalAccessException | InvocationTargetException e) {
										e.printStackTrace();
										return false;
									}
								}),
						"Building is not in list of buildings of building.city.");
			}
		}

		@Test
		@Order(270)
		void jpaCityToStringTest() {
			connector.getEntityManager().clear();
			List<City> savedEntities = connector.getAllCities();
			for (City city : savedEntities) {
				assertThat(city.toString(), Matchers.notNullValue());
			}
		}

		@Test
		@Order(300)
		void jpaCountryDeleteTest() {
			List<Country> savedEntities = connector.getAllCountries();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
			for (Country entity : savedEntities) {
				connector.delete(entity);
			}
			List<Country> modifiedEntities = connector.getAllCountries();
			assertThat(modifiedEntities, Matchers.hasSize(0));
		}

		@Test
		@Order(400)
		void jpaFindByFloorCountTest() throws IllegalAccessException, InvocationTargetException {
			connector.save(City.generate());
			List<City> cities = connector.getAllCities();
			for (Building building : Stream.generate(() -> Building.generate(cities)).limit(100).toList()) {
				connector.save(building);
			}

			List<Building> buildings = connector
					.executeCriteriaQuery(connector.createCriteriaQueryForBuildingsWithFloorRange(200, 300));
			for (Building building : buildings) {
				assertThat((int)helper.executeGetter(building, "getFloorsCount"),
						Matchers.allOf(Matchers.greaterThanOrEqualTo(200), Matchers.lessThanOrEqualTo(300)));
			}
		}

	}

}
