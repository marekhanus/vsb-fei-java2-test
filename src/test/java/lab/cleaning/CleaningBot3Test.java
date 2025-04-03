package lab.cleaning;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.lang.reflect.Method;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.regex.Pattern;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.apache.logging.log4j.Level;
import org.apache.logging.log4j.core.LogEvent;
import org.apache.logging.log4j.core.Logger;
import org.apache.logging.log4j.core.LoggerContext;
import org.apache.logging.log4j.core.config.Configurator;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

import cz.vsb.fei.kelvin.unittest.LogContains;
import cz.vsb.fei.kelvin.unittest.OutputContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TestAppender;

class CleaningBot3Test {

	StructureHelper helper = StructureHelper.getInstance(CleaningBot3Test.class);

	@Test
	void testInsertThrowsException() throws NoSuchMethodException {
		Method insert = helper.getMethod(Shelf.class, "insert", void.class);
		List<String> exceptionNames = List.of(insert.getExceptionTypes()).stream().map(Class::getSimpleName).toList();
		assertThat(exceptionNames, Matchers.containsInRelativeOrder(NotEnoughSpaceException.class.getSimpleName()));
	}

	@Test
	void testInsertThrowException() {
		Shelf shelf = new Shelf(2);
		try {
			shelf.insert(List.of(new Box(0, 0, 10, 1)));
			Assertions.fail("Method insert shout raise an exception");
		} catch (Exception ex) {
			assertEquals(NotEnoughSpaceException.class, ex.getClass(),
					"Method insert sdhould raise exception of type NotEnoughSpaceException");
		}
	}

	@Test
	void testInsert() throws Exception {
		Shelf shelf = new Shelf(20);
		Box b1 = new Box(0, 0, 2, 2);
		Box b2 = new Box(0, 0, 1, 1);
		shelf.insert(List.of(b1, b2));
		assertTrue(2 == b2.getX() && 0 == b2.getY(), "Box should have position [2,0]");
	}

	@Test
	void testGenerateMess() {
		assertThat(new CleaningBot().generateMess(), Matchers.hasSize(10));
	}

	@Test
	void testCleanLogAll() {
		Configurator.setRootLevel(Level.INFO);
		final TestAppender appender = new TestAppender("unittest", null, null, false, null);
		appender.start();
		LoggerContext context = LoggerContext.getContext(false);
		Logger logger = context.getRootLogger();
		logger.addAppender(appender);

		try {
			new CleaningBot().clean(new Shelf(2), List.of(new Box(0, 0, 30, 1)));
			Map<Level, List<LogEvent>> logMap = appender.getEvents().stream()
					.collect(Collectors.toMap(LogEvent::getLevel, e -> new LinkedList<>(List.of(e)), (value, n) -> {
						value.addAll(n);
						return value;
					}));

			assertTrue(
					logMap.getOrDefault(Level.INFO, Collections.emptyList()).size()
							+ logMap.getOrDefault(Level.DEBUG, Collections.emptyList()).size() >= 1,
					"At least 1 message in proper level for informativ log shout be outputed.");
			assertTrue(
					logMap.getOrDefault(Level.WARN, Collections.emptyList()).size()
							+ logMap.getOrDefault(Level.ERROR, Collections.emptyList()).size() >= 1,
					"At least 1 message in proper level for exception log shout be outputed.");
			/*
			if (!Stream
					.concat(logMap.getOrDefault(Level.WARN, Collections.emptyList()).stream(),
							logMap.getOrDefault(Level.ERROR, Collections.emptyList()).stream())
					.anyMatch(e -> e.getThrown() != null)) {
				Assertions.fail("At least one log have to contains foll exception stacktrace");
			}
			*/
		} finally {
			logger.removeAppender(appender);
			appender.stop();
		}

	}

	@Test
	void testCleanLogComputeMessageOnlyIfReallyPrintedException() {
		Configurator.setRootLevel(Level.OFF);
		new CleaningBot().clean(new Shelf(2), List.of(new Box(0, 0, 30, 1) {
			@Override
			public String toString() {
				throw new UnsupportedOperationException("toString is call on Box even log is not printed.");
			}
		}));
	}

}
