package jez04.structure.test;

import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.PrintStream;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.FileVisitResult;
import java.nio.file.FileVisitor;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.reflections.Configuration;
import org.reflections.Reflections;
import org.reflections.scanners.Scanners;
import org.reflections.util.ConfigurationBuilder;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;

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
	void gameControllerActionMethodTest() {
		helper.classExist("GameController");
		Class<?> c = helper.getClass("GameController");
		helper.hasMethodRegexp(c, ".*", void.class, ActionEvent.class);
	}

	@Test
	void gameControllerLambdasTest() {
		helper.classExist("GameController");
		Class<?> c = helper.getClass("GameController");
		long lamdaCount = helper.countMethodRegexp(c, "lambda\\$.*");
		long innerClasscount = helper.countClassesRegexp(".*GameController\\$.*");
		assertTrue(lamdaCount + innerClasscount >= 2,
				"At least 2 inner classes or lamdas required for GameController but only "
						+ (lamdaCount + innerClasscount) + " found.");
	}

	@Test
	void hitListenerExistenceTest() {
		helper.classExist("HitListener");
	}

	@Test
	void hitListenerEventMethodTest() {
		helper.classExist("HitListener");
		Class<?> c = helper.getClass("HitListener");
		helper.hasMethod(c, "ufoDestroyed");
	}

	@Test
	void sceneCollectionTest() {
		helper.classExist("World");
		Class<?> c = helper.getClass("World");
		long collectionCount = Arrays.asList(c.getDeclaredFields()).stream()
				.filter(f -> Collection.class.isAssignableFrom(f.getType())).count();
		assertTrue(collectionCount >= 3, "lab.Scene require atleast 3 filed of type/subtype Collection, but only "
				+ collectionCount + " found.");
	}

	@Test
	void worldMethodAddTest() {
		helper.classExist("World");
		Class<?> c = helper.getClass("World");
		helper.hasMethod(c, "add", void.class, helper.getClass("DrawableSimulable"));
		;
	}

	@Test
	void worldMethodRemoveTest() {
		helper.classExist("World");
		Class<?> c = helper.getClass("World");
		helper.hasMethod(c, "remove", void.class, helper.getClass("DrawableSimulable"));
		;
	}

	@Test
	void bulletAnimatedMethodAddTest() {
		helper.classExist("BulletAnimated");
		Class<?> c = helper.getClass("BulletAnimated");
		Class<?> l = helper.getClass("HitListener");
		helper.hasMethodRegexp(c, "add.*", List.of(void.class, boolean.class), l);
	}

	@Test
	void bulletAnimatedMethodRemoveTest() {
		helper.classExist("Ufo");
		Class<?> c = helper.getClass("BulletAnimated");
		Class<?> l = helper.getClass("HitListener");
		helper.hasMethodRegexp(c, "remove.*", List.of(void.class, boolean.class), l);
	}

	@Test
	void bulletAnimatedMethodFireTest() {
		helper.classExist("BulletAnimated");
		Class<?> c = helper.getClass("BulletAnimated");
		assertTrue(helper.countMethodRegexp(c, "fire.*") > 0, "Method fire.* in LochNess not found.");
	}

}
