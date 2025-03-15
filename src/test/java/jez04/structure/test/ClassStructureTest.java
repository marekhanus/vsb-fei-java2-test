package jez04.structure.test;

import static org.hamcrest.MatcherAssert.assertThat;

import java.net.URISyntaxException;
import java.nio.file.Path;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import cz.vsb.fei.kelvin.unittest.XmlFileContains;

class ClassStructureTest {

	StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

	@Test
	void lombokAsDependencyTest() throws URISyntaxException {
		XmlFileContains xmlFileContains = new XmlFileContains("pom.xml",
				"/project/dependencies/dependency/artifactId[text() = 'lombok']");
		Path root = TextFileContains.getProjectRoot(getClass());
		assertThat(root, xmlFileContains);
	}

	@Test
	void lombokAsAnnotationProcessorTest() throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()), new XmlFileContains("pom.xml",
				"/project/build/plugins/plugin/artifactId[text() = 'maven-compiler-plugin']"));
		assertThat(TextFileContains.getProjectRoot(getClass()), new XmlFileContains("pom.xml",
				"/project/build/plugins/plugin/configuration/annotationProcessorPaths/path/artifactId[text() = 'lombok']"));
	}

	@Test
	void moduleInfoTest() throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()), new TextFileContains("module-info.java", "lombok;"));
	}

	@CsvSource({
		"@Getter,1",
		"@Setter,1",
		"@Log.*,1",
		"@.*ArgsConstructor,1",
		"@ToString,1",
		"@Getter,3",
		"@Setter,3",
		"@Log.*,3",
		"@.*ArgsConstructor,2",
		"@ToString,3",
		"@Log.*,5",
		"@ToString,5" })
	@ParameterizedTest(name = "use Lombok Annotation {0} {1} times")
	void useLombokConfigTest(String text, int count) throws URISyntaxException, ClassNotFoundException {
		assertThat(TextFileContains.getProjectRoot(getClass()),
				new TextFileContains(".*\\.java", text).count(count).useRegExpForName(true));
	}

}
