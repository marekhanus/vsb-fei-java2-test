package jez04.structure.test;

import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import javafx.geometry.Rectangle2D;

class ClassStructureTest {

	@Test
	void ufoClassEexistanceTest() {
		Set<String> allClasses = getNameOfAllClasses();
		assertTrue(allClasses.stream().anyMatch(c -> c.endsWith("Ufo")), "Class Ufo not found");
	}

	@Test
	void ufoClassPropertiesTest() {
		Set<String> allClasses = getNameOfAllClasses();
		String ufoClassName = allClasses.stream().filter(c -> c.endsWith("Ufo")).findAny().orElse(null);
		assertNotNull(ufoClassName, "Class Ufo not found");
		try {
			Class<?> ufoClass = Class.forName(ufoClassName);
			List<Method> methods = Arrays.asList(ufoClass.getMethods());
			assertTrue(methods.stream().anyMatch(m -> m.getName().contains("draw")), "No method draw");
			assertTrue(methods.stream().anyMatch(m -> m.getName().contains("simulate")), "No method simulate");
			assertTrue(methods.stream().anyMatch(m -> m.getName().contains("getBoundingBox")),
					"No method getBoundingBox");
			assertTrue(
					methods.stream().filter(m -> m.getName().contains("getBoundingBox"))
							.anyMatch(m -> m.getReturnType().equals(Rectangle2D.class)),
					"Method getBoundingBox not return Rectangle2D");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void worldClassPropertyTest() {
		Set<String> allClasses = getNameOfAllClasses();
		String ufoClassName = allClasses.stream().filter(c -> c.endsWith("Ufo")).findAny().orElse(null);
		String worldClassName = allClasses.stream().filter(c -> c.endsWith("World")).findAny().orElse(null);
		assertNotNull(worldClassName, "Class World not found");
		try {
			Class<?> worldClass = Class.forName(worldClassName);
			List<Field> fields = Arrays.asList(worldClass.getDeclaredFields());
			Class<?> ufoClass = Class.forName(ufoClassName);
			assertTrue(
					fields.stream()
							.anyMatch(f -> f.getType().isArray() && f.getType().getComponentType().equals(ufoClass)),
					"No array of Ufo in World class");
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	@Test
	void drawingClassExistenceTest() {
		Set<String> allClasses = getNameOfAllClasses();
		int detectedClassCount = 0;
		for (String className : allClasses) {
			try {
				Class<?> clazz = Class.forName(className);
				List<Constructor<?>> constructorList = Arrays.asList(clazz.getConstructors());
				List<Method> methodList = Arrays.asList(clazz.getMethods());
				long drawMethodCount = methodList.stream().filter(m -> m.getName().contains("draw")).count();
				long simulateMethodCount = methodList.stream().filter(m -> m.getName().contains("sim")).count();
				if (!constructorList.isEmpty() && (drawMethodCount > 0 || simulateMethodCount > 0)) {
					System.out.println("DETECT:" + className);
					detectedClassCount++;
				}
			} catch (ClassNotFoundException e) {
				System.out.println(String.format("Class '%s' cannot be loaded: %s", className, e.getMessage()));
			}
		}
		assertTrue(detectedClassCount >= 2, "");
	}

	private Set<String> getNameOfAllClasses() {
		List<String> initClassesName = Arrays.asList("lab.Routines", "lab.App", "lab.DrawingThread");
		for (String className : initClassesName) {
			try {
				Class.forName(className);
			} catch (ClassNotFoundException e) {
				System.out.println(String.format("Class '%s' cannot be loaded: %s", className, e.getMessage()));
			}
		}
		Set<String> allClasses = new HashSet<>();
		for (Package p : Package.getPackages()) {
			if (p.getName().startsWith("java.") || p.getName().startsWith("com.") || p.getName().startsWith("jdk.")
					|| p.getName().startsWith("javafx.") || p.getName().startsWith("org.")
					|| p.getName().startsWith("sun.") || p.getName().startsWith("javax.")
					|| p.getName().startsWith("javassist")) {
				continue;
			}
			System.out.println(p.getName());
			Configuration conf = new ConfigurationBuilder().addScanners(Scanners.SubTypes.filterResultsBy(pc -> true))
					.forPackages(p.getName());
			Reflections reflections = new Reflections(conf);
			allClasses.addAll(reflections.getAll(Scanners.SubTypes.filterResultsBy(c -> true)));
		}
		return allClasses;
	}
}
