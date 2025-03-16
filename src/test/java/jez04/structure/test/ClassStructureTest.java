package jez04.structure.test;

import static org.hamcrest.MatcherAssert.assertThat;

import java.io.IOException;
import java.lang.reflect.InvocationTargetException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.Objects;
import java.util.regex.Pattern;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;

import cz.vsb.fei.kelvin.unittest.ProjectContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import cz.vsb.fei.kelvin.unittest.XmlFileContains;
import lab.data.Level;
import lab.data.Score;
import lab.storage.JpaConnector;

class ClassStructureTest {

	StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

	@Test
	void jakartaAndHibernateAsDependencyTest() throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()), new XmlFileContains("pom.xml",
				"/project/dependencies/dependency/artifactId[text() = 'jakarta.persistence-api']"));
		assertThat(TextFileContains.getProjectRoot(getClass()), new XmlFileContains("pom.xml",
				"/project/dependencies/dependency/artifactId[text() = 'hibernate-core']"));
	}

	@Test
	void jakartaInModulInfoTest() throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()),
				new TextFileContains("module-info.java", "jakarta.persistence;"));
		assertThat(TextFileContains.getProjectRoot(getClass()),
				new TextFileContains("module-info.java", "opens\\slab.data;"));
	}

	@Test
	void pesistenceXmlTest() throws URISyntaxException {
		ProjectContains projectContains = new ProjectContains("persistence.xml");
		assertThat(TextFileContains.getProjectRoot(getClass()), projectContains);
		assertThat(projectContains.getFoundFiles(), Matchers.not(Matchers.hasSize(0)));
		Path persistenceXml = projectContains.getFoundFiles().getFirst();
		assertThat(persistenceXml.toAbsolutePath().toString(),
				Matchers.endsWith(Paths.get("resources", "META-INF", "persistence.xml").toString()));
	}

	@Test
	void useEnumeratedTest() throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()),
				new TextFileContains("Score.java", "@Enumerated\\(\\s*EnumType.STRING\\s*\\)"));
	}

	@TestMethodOrder(OrderAnnotation.class)
	@Nested
	class JpaConnectorTests {

		private Score template = new Score(null, "Tester", 100, Level.EASY);
		private JpaConnector connector;

		@BeforeEach
		void init() {
			connector = new JpaConnector();
		}

		@AfterEach
		void cleanUp() {
			connector.stop();
		}

		boolean same(Score s1, Score s2) {
			return s1.getLevel() == s2.getLevel() && s1.getPoints() == s2.getPoints()
					&& Objects.equals(s1.getName(), s2.getName());
		}

		@Test
		@Order(100)
		void jpaScoreInsertTest() {
			Score savedScore = connector.save(template);
			assertThat(savedScore.getId(), Matchers.notNullValue());
		}

		@Test
		@Order(200)
		void jpaScoreReadTest() {
			List<Score> savedScores = connector.getAll().stream().filter(s -> same(s, template)).toList();
			assertThat(savedScores, Matchers.not(Matchers.hasSize(0)));
		}

		@Test
		@Order(250)
		void jpaReadSortedTest() {
			for (int i = 0; i < 20; i++) {
				connector.save(template.toBuilder().points(i).build());
			}
			List<Score> result = connector.getFirstTen();
			assertThat(result, Matchers.hasSize(10));
			for (int i = 0; i < result.size()-1; i++) {
				assertThat(result.get(i).getPoints(), Matchers.greaterThanOrEqualTo(result.get(i+1).getPoints()));
			}
		}

		@Test
		@Order(300)
		void jpaScoreModifyTest() {
			List<Score> savedScores = connector.getAll().stream().filter(s -> same(s, template)).toList();
			List<Long> savedIds = savedScores.stream().map(Score::getId).toList();
			assertThat(savedScores, Matchers.not(Matchers.hasSize(0)));
			for (Score score : savedScores) {
				score.setLevel(Level.HARD);
				connector.save(score);
			}
			List<Score> modifiedScores = connector.getAll().stream().filter(s -> savedIds.contains(s.getId())).toList();
			assertThat(modifiedScores, Matchers.not(Matchers.hasSize(0)));
			List<Score> originalDataScores = connector.getAll().stream().filter(s -> same(s, template)).toList();
			assertThat(originalDataScores, Matchers.hasSize(0));
		}

		@Test
		@Order(400)
		void jpaScoreDeleteTest() {
			template.setLevel(Level.HARD);
			List<Score> savedScores = connector.getAll().stream().filter(s -> same(s, template)).toList();
			List<Long> savedIds = savedScores.stream().map(Score::getId).toList();
			assertThat(savedScores, Matchers.not(Matchers.hasSize(0)));
			connector.delete(savedScores);
			List<Score> modifiedScores = connector.getAll().stream().filter(s -> savedIds.contains(s.getId())).toList();
			assertThat(modifiedScores, Matchers.hasSize(0));
			List<Score> originalDataScores = connector.getAll().stream().filter(s -> same(s, template)).toList();
			assertThat(originalDataScores, Matchers.hasSize(0));
		}

		@Test
		@Order(400)
		void jpaMergeTest() {
			connector.save(template);
			connector.find(template.getId());
			Score copy = template.toBuilder().points(500).build();
			Score result = connector.save(copy);
			assertThat(result, Matchers.not(Matchers.sameInstance(copy)));
		}

		@Test
		@Order(500)
		void jpamodifyNoPersistNoMergeTest() throws URISyntaxException, IOException, IllegalAccessException, InvocationTargetException, NoSuchMethodException, SecurityException {
			template.setName("aaa");
			connector.save(template);
			connector.modifyNoPersistOrMerge(template.getId(), score -> score.setName("ok"));
			Object em = connector.getEntityManager();
			em.getClass().getMethod("clear").invoke(em);
			
			assertThat(connector.find(template.getId()).getName(), Matchers.equalTo("ok"));
			TextFileContains textFileContains = new TextFileContains("JpaConnector.java",
					"void\\s+modifyNoPersistOrMerge[\\s\\S]*}").multiline(true);
			assertThat(TextFileContains.getProjectRoot(ClassStructureTest.class), textFileContains);
			Path score = textFileContains.getFoundFiles().getFirst();
			String src = Files.readString(score);
			String method = Pattern.compile("void\\s+modifyNoPersistOrMerge[\\s\\S]*}").matcher(src).results().findFirst().get().group();
			assertThat(method, Matchers.not(Matchers.containsString("persist")));
			assertThat(method, Matchers.not(Matchers.containsString("merge")));
		}
	}
}
