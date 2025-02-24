package lab;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.List;
import java.util.regex.MatchResult;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

public class DataImporter {
	private static final Pattern personTextPattern = Pattern.compile("\\{[^\\{\\}]*address");
	private static final Pattern firstNamePattern = Pattern.compile("\"firstname\":\"([\\p{IsAlphabetic}']+)\"");
	private static final Pattern lastNamePattern = Pattern.compile("\"lastname\":\"([\\p{IsAlphabetic}']+)\"");
	private static final Pattern datePattern = Pattern.compile("\"birthday\":\"(\\d{4}-\\d{2}-\\d{2})\"");
	private static final DateTimeFormatter czechDate = DateTimeFormatter.ofPattern("dd. MM. YYYY");

	public static void main(String[] args) {
		importPlayers();
	}

	public static void importPlayers() {
		List<Person> persons = parseData(downloadText()).stream().map(DataImporter::parsePerson).toList();
		
		for (Person person : persons) {
			System.out.printf("%s %s (%s):  age=%d, 50th: %s , next in: %d%n"
					, person.getFirstName(), person.getLastName()
					, person.getDayOfBirth().format(czechDate)
					, person.getAge()
					, person.get50thBirthDay().format(czechDate)
					, person.getDaysToBirthday());
		}
	}

	public static String downloadText() {
		try {
			URL url = new URI("https://fakerapi.it/api/v2/persons?_quantity=20").toURL();
			try (InputStream is = url.openStream()) {
				BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(is));
				return bufferedReader.lines().collect(Collectors.joining("\n"));
			}
		} catch (URISyntaxException | IOException e) {
			e.printStackTrace();
			return "";
		}
	}

	public static List<String> parseData(String data) {
		Matcher matcher = personTextPattern.matcher(data);
		return matcher.results().map(MatchResult::group).toList();
	}

	public static Person parsePerson(String personText) {
		String firstName = firstNamePattern.matcher(personText).results().map(result -> result.group(1)).findAny()
				.orElse("");
		String lastName = lastNamePattern.matcher(personText).results().map(result -> result.group(1)).findAny()
				.orElse("");
		LocalDate date = datePattern.matcher(personText).results().map(result -> result.group(1)).map(text -> LocalDate.parse(text, DateTimeFormatter.ISO_DATE)).findAny()
				.orElse(LocalDate.EPOCH);
		return new Person(firstName, lastName, date);
	}
}
