package jez04.structure.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.List;

import org.junit.jupiter.api.Test;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

class ClassStructureTest {

	StructureHelper helper = new StructureHelper();

	@Test
	void gameControllerExistenceTest() {
		helper.classExist("GameController");
	}

	@Test
	void gameControllerFxmlTest() {
		helper.classExist("GameController");
		Class<?> c = helper.getClass("GameController");
		helper.hasPropertyWithAnnotation(c, ".*", FXML.class);
	}

	@Test
	void gameControllerButtonActionMethodTest() {
		helper.classExist("GameController");
		Class<?> c = helper.getClass("GameController");
		long count = helper.countMethodRegexp(c, ".*", void.class, ActionEvent.class);
		assertTrue(count > 1, "Only " + count+ " method handling button click found. Expected more then 1");
	}

	@Test
	void gameControllerTableViewTest() {
		helper.classExist("GameController");
		Class<?> c = helper.getClass("GameController");
		helper.hasProperty(c, ".*", TableView.class, false);
	}
	
	@Test
	void gameControllerTableColumnTest() {
		helper.classExist("GameController");
		Class<?> c = helper.getClass("GameController");
		helper.hasProperty(c, ".*", TableColumn.class, false);
	}

	@Test
	void dbConnectorTest() {
		helper.classExistRegexp(".*[Dd][Bb][cC]on.*");
	}

	@Test
	void dbConnectorGetAllTest() {
		helper.classExistRegexp(".*[Dd][Bb][cC]on.*");
		Class<?> scoreClass = helper.getClassRegexp(".*[sS]core.*");
		Class<?> c = helper.getClassRegexp(".*[Dd][Bb][cC]on.*");
		helper.hasMethodRegexp(c, ".*[aA]ll.*", List.class);
		helper.hasMethodRegexp(c, ".*[iI]nsert.*", void.class, scoreClass);
	}
	@Test
	void dbConnectorInsertTest() {
		helper.classExistRegexp(".*[Dd][Bb][cC]on.*");
		Class<?> scoreClass = helper.getClassRegexp(".*[sS]core.*");
		Class<?> c = helper.getClassRegexp(".*[Dd][Bb][cC]on.*");
		helper.hasMethodRegexp(c, ".*[iI]nsert.*", void.class, scoreClass);
	}
	@Test
	void dbConnectorContainsJdbcTest() throws URISyntaxException, IOException {
		helper.classExistRegexp(".*[Dd][Bb][cC]on.*");
		Class<?> c = helper.getClassRegexp(".*[Dd][Bb][cC]on.*");
		String src = helper.getSourceCode(c);
		assertTrue(src.contains("jdbc:"), "No usage of jdbc detect.");
	}

	@Test
	void dbConnectorContainsDriverManagerTest() throws URISyntaxException, IOException {
		helper.classExistRegexp(".*[Dd][Bb][cC]on.*");
		Class<?> c = helper.getClassRegexp(".*[Dd][Bb][cC]on.*");
		String src = helper.getSourceCode(c);
		assertTrue(src.contains("DriverManager"), "No usage of DriverManager detect.");
	}

	@Test
	void dbConnectorContainsSqlTest() throws URISyntaxException, IOException {
		helper.classExistRegexp(".*[Dd][Bb][cC]on.*");
		Class<?> c = helper.getClassRegexp(".*[Dd][Bb][cC]on.*");
		String src = helper.getSourceCode(c).toLowerCase();
		assertTrue(src.contains("create "), "No usage of SQL create.");
		assertTrue(src.contains("select "), "No usage of SQL select.");
		assertTrue(src.contains("insert "), "No usage of SQL table.");
		assertTrue(src.contains(" from "), "No usage of SQL from.");
		assertTrue(src.contains(" table"), "No usage of SQL table.");
	}
}
