package jez04.structure.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.allOf;
import static org.hamcrest.Matchers.empty;
import static org.hamcrest.Matchers.endsWith;
import static org.hamcrest.Matchers.not;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.Matchers.startsWith;
import static org.hamcrest.Matchers.stringContainsInOrder;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.net.URISyntaxException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;

import org.junit.jupiter.api.Test;

class ClassStructureTest {

	StructureHelper helper = StructureHelper.getInstance();

	@Test
	void dataImporterTest() throws URISyntaxException, IOException, IllegalAccessException, InvocationTargetException {
		assertThat("", new ClassExist(".*Data.*", false, true));
		Class<?> c = helper.getClassRegexp(".*Data.*");
	}

	@Test
	void dataImporterDownloadTest() throws URISyntaxException, IOException, IllegalAccessException, InvocationTargetException {
		assertThat("", new ClassExist(".*Data.*", false, true));
		Class<?> d = helper.getClassRegexp(".*Data.*");
		assertThat(d,  new HasMethod(".*down.*", String.class).useRegExp(true).caseSensitive(false));
		Method download = helper.getMethod(d, ".*down.*", false, String.class);
		String downloadedText = (String) download.invoke(null);
		assertThat(downloadedText, allOf(
				endsWith("}]}"),
				startsWith("{\"status\":\"OK\""),
				stringContainsInOrder("firstname"),
				stringContainsInOrder("lastname"),
				stringContainsInOrder("birthday")
				));
		
	}

	@Test
	void dataImporterParseAllTest() throws URISyntaxException, IOException, IllegalAccessException, InvocationTargetException {
		assertThat("", new ClassExist(".*Data.*", false, true));
		Class<?> d = helper.getClassRegexp(".*Data.*");
		helper.hasMethod(d, ".*parse.*", false, List.class, String.class);
		Method parse = helper.getMethod(d, ".*parse.*", false, List.class, String.class);
		List<String> texts = (List<String>) parse.invoke(null, "{\"id\":1,\"firstname\":\"Tatum\",\"lastname\":\"Block\",\"email\":\"lonnie.bergstrom@stoltenberg.com\",\"phone\":\"+12397191764\",\"birthday\":\"1946-11-06\",\"gender\":\"male\",\"address\":{");
		assertThat(texts, not(empty()));
	}

	@Test
	void personTest() throws URISyntaxException, IOException, IllegalAccessException, InvocationTargetException {
		assertThat("", new ClassExist("Person", false, false));
		Class<?> p = helper.getClass("Person", false);
	}

	@Test
	void dataImporterParsePersonTest() throws URISyntaxException, IOException, IllegalAccessException, InvocationTargetException {
		assertThat("", new ClassExist(".*Data.*", false, true));
		Class<?> d = helper.getClassRegexp(".*Data.*");

		assertThat("", new ClassExist("Person", false, false));
		Class<?> p = helper.getClass("Person", false);
		helper.hasMethod(d, ".*parse.*", false, p, String.class);
		Method parsePerson = helper.getMethod(d, ".*parse.*", false, p, String.class);
		Object person = parsePerson.invoke(null, "{\"id\":1,\"firstname\":\"Tatum\",\"lastname\":\"Block\",\"email\":\"lonnie.bergstrom@stoltenberg.com\",\"phone\":\"+12397191764\",\"birthday\":\"1946-11-06\",\"gender\":\"male\",\"address\":{");
		assertThat(person, notNullValue());
	}

	@Test
	void personAgeTest() throws URISyntaxException, IOException, IllegalAccessException, InvocationTargetException {
		assertThat("", new ClassExist(".*Data.*", false, true));
		Class<?> d = helper.getClassRegexp(".*Data.*");

		assertThat("", new ClassExist("Person", false, false));
		Class<?> p = helper.getClass("Person", false);
		helper.hasMethod(d, ".*parse.*", false, p, String.class);
		Method parsePerson = helper.getMethod(d, ".*parse.*", false, p, String.class);
		Object person = parsePerson.invoke(null, "{\"id\":1,\"firstname\":\"Tatum\",\"lastname\":\"Block\",\"email\":\"lonnie.bergstrom@stoltenberg.com\",\"phone\":\"+12397191764\",\"birthday\":\"1946-11-06\",\"gender\":\"male\",\"address\":{");
		assertThat(person, notNullValue());

		Method age = helper.getMethod(p, ".*age.*", false, int.class);
		int result = (int)age.invoke(person);
		assertEquals(78, result , "Calculate wrong age.");
	}

	@Test
	void person50birthdayTest() throws URISyntaxException, IOException, IllegalAccessException, InvocationTargetException {
		assertThat("", new ClassExist(".*Data.*", false, true));
		Class<?> d = helper.getClassRegexp(".*Data.*");

		assertThat("", new ClassExist("Person", false, false));
		Class<?> p = helper.getClass("Person", false);
		helper.hasMethod(d, ".*parse.*", false, p, String.class);
		Method parsePerson = helper.getMethod(d, ".*parse.*", false, p, String.class);
		Object person = parsePerson.invoke(null, "{\"id\":1,\"firstname\":\"Tatum\",\"lastname\":\"Block\",\"email\":\"lonnie.bergstrom@stoltenberg.com\",\"phone\":\"+12397191764\",\"birthday\":\"1946-11-06\",\"gender\":\"male\",\"address\":{");
		assertThat(person, notNullValue());
		
		Method birth50 = helper.getMethod(p, ".*50.*", false, LocalDate.class);
		LocalDate ldBirth50 = (LocalDate)birth50.invoke(person);
		assertEquals(LocalDate.of(1996, 11, 06), ldBirth50 , "Calculate wrong 50th birthday.");
		
	}

	@Test
	void personNextBirthdayTest() throws URISyntaxException, IOException, IllegalAccessException, InvocationTargetException {
		assertThat("", new ClassExist(".*Data.*", false, true));
		Class<?> d = helper.getClassRegexp(".*Data.*");

		assertThat("", new ClassExist("Person", false, false));
		Class<?> p = helper.getClass("Person", false);
		helper.hasMethod(d, ".*parse.*", false, p, String.class);
		Method parsePerson = helper.getMethod(d, ".*parse.*", false, p, String.class);
		LocalDate bod =LocalDate.now().plusDays(338).minusYears(10);
		Object person = parsePerson.invoke(null, "{\"id\":1,\"firstname\":\"Tatum\",\"lastname\":\"Block\",\"email\":\"lonnie.bergstrom@stoltenberg.com\",\"phone\":\"+12397191764\",\"birthday\":\""+ bod.format(DateTimeFormatter.ISO_DATE) +"\",\"gender\":\"male\",\"address\":{");
		assertThat(person, notNullValue());

		Method daysM = helper.getMethod(p, ".*days.*", false, long.class);
		long days= (long)daysM.invoke(person);
		assertEquals(338, days , "Calculate wrong days to next birthday.");
	}

}
