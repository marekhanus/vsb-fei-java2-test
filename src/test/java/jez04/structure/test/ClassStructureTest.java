package jez04.structure.test;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.MethodOrderer.OrderAnnotation;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Order;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestMethodOrder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import cz.vsb.fei.kelvin.unittest.ProjectContains;
import cz.vsb.fei.kelvin.unittest.StructureHelper;
import cz.vsb.fei.kelvin.unittest.TextFileContains;
import cz.vsb.fei.kelvin.unittest.XmlFileContains;
import lab.data.FirstPersonShooter;
import lab.data.Game;
import lab.data.MyEntity;
import lab.data.PlatformGame;
import lab.data.Player;
import lab.data.Score;
import lab.data.Score.Difficult;
import lab.storage.JpaConnector;

class ClassStructureTest {

	StructureHelper helper = StructureHelper.getInstance(ClassStructureTest.class);

	// @formatting:off
	@ParameterizedTest
	@CsvSource({
		"Score.java,@Entity",
		"Score.java,@NoArgsConstructor",
		"Score.java,@EqualsAndHashCode",
		"Score.java,@EqualsAndHashCode\\(onlyExplicitlyIncluded\\s*=\\s*true\\)",
		"Score.java,@EqualsAndHashCode\\.Include",
		"Score.java,@ManyToOne",
		"Game.java,@Entity",
		"Game.java,@NoArgsConstructor",
		"Game.java,@EqualsAndHashCode",
		"Game.java,@EqualsAndHashCode\\(onlyExplicitlyIncluded\\s*=\\s*true\\)",
		"Game.java,@EqualsAndHashCode\\.Include",
		"Game.java,@OneToMany\\(mappedBy",
		"Game.java,@ToString\\.Exclude",
		"Player.java,@Entity",
		"Player.java,@NoArgsConstructor",
		"Player.java,@EqualsAndHashCode",
		"Player.java,@EqualsAndHashCode\\(onlyExplicitlyIncluded\\s*=\\s*true\\)",
		"Player.java,@EqualsAndHashCode\\.Include",
		"Player.java,@OneToMany\\(mappedBy",
		"Player.java,@ToString\\.Exclude",
		"FirstPersonShooter.java,@Entity",
		"FirstPersonShooter.java,@NoArgsConstructor",
		"PlatformGame.java,@Entity",
		"PlatformGame.java,@NoArgsConstructor",
	})
	// @formatting:on
	void anotaceTest(String file, String annotation) throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()),
				new TextFileContains(file, annotation));
	}

	// @formatting:off
	@ParameterizedTest
	@CsvSource({
		"Game.java,Score_.GAME",
		"Player.java, Score_.PLAYER",
	})
	// @formatting:on
	void mappedByTest(String file, String annotation) throws URISyntaxException {
		assertThat(TextFileContains.getProjectRoot(getClass()),
				new TextFileContains(file, annotation));
	}

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

	@TestMethodOrder(OrderAnnotation.class)
	@Nested
	class JpaConnectorTests {

		private Player player;
		private Game game;
		private PlatformGame platformGame;
		private FirstPersonShooter firstPersonShooter;
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
			player = Player.generate();
			game = Game.generate();
			platformGame = PlatformGame.generate();
			firstPersonShooter = FirstPersonShooter.generate();
			connector.getEntityManager().clear();
			if(connector.getEntityManager().getTransaction().isActive()) {
				connector.getEntityManager().getTransaction().rollback();
			}
		}

		@AfterAll
		static void cleanUpAll() {
			connector.stop();
		}

		boolean same(MyEntity e1, MyEntity e2) {
			if(e1 instanceof Player p1 && e2 instanceof Player p2) {
				return same(p1, p2);
			} else if(e1 instanceof PlatformGame pg1 && e2 instanceof PlatformGame pg2) {
				return same(pg1, pg2);
			} else if(e1 instanceof FirstPersonShooter fps1 && e2 instanceof FirstPersonShooter fps2) {
				return same(fps1, fps2);			
			} else if(e1 instanceof Game g1 && e2 instanceof Game g2) {
				return same(g1, g2);
			} else if(e1 instanceof Score s1 && e2 instanceof Score s2) {
				return same(s1, s2);
			}
			return false;
		}

		private boolean same(Score s1, Score s2) {
			// @formatting:off
			return s1.getDifficult() == s2.getDifficult() 
					&& s1.getPoints() == s2.getPoints()
					&& Objects.equals(s1.getId(), s2.getId())
					&& Objects.equals(s1.getPlayer().getId(), s2.getPlayer().getId())
					&& Objects.equals(s1.getGame().getId(), s2.getGame().getId());
			// @formatting:on
		}

		private boolean same(Player p1, Player p2) {
			// @formatting:off
			return Objects.equals(p1.getId(), p2.getId())
					&& Objects.equals(p1.getFirstName(), p2.getFirstName())
					&& Objects.equals(p1.getLastName(), p2.getLastName())
					&& Objects.equals(p1.getNick(), p2.getNick())
					&& same(p1.getScores(), p2.getScores());
			// @formatting:on
		}
		private boolean same(Game g1, Game g2) {
			// @formatting:off
			return Objects.equals(g1.getId(), g2.getId())
					&& Objects.equals(g1.getName(), g2.getName())
					&& same(g1.getScores(), g2.getScores());
			// @formatting:on
		}
		private boolean same(PlatformGame g1, PlatformGame g2) {
			// @formatting:off
			return same((Game)g1, (Game)g2)
					&& Objects.equals(g1.getPlatformType(), g2.getPlatformType())
					&& g1.isRetro() == g2.isRetro();
			// @formatting:on
		}
		private boolean same(FirstPersonShooter g1, FirstPersonShooter g2) {
			// @formatting:off
			return same((Game)g1, (Game)g2)
					&& Objects.equals(g1.getFpsType(), g2.getFpsType())
					&& g1.getPlayersCount() == g2.getPlayersCount();
			// @formatting:on
		}
		private boolean same(List<?> l1, List<?> l2) {
			if(l1 == null) {
				l1 = Collections.emptyList();
			}
			if(l2 == null) {
				l2 = Collections.emptyList();
			}
			return Objects.equals(l1, l2);
		}
		
		private void saveAndReadSame(MyEntity original) {
			MyEntity result = connector.save(original);
			assertThat(result.getId(), Matchers.notNullValue());
			connector.getEntityManager().clear();
			MyEntity saved = connector.getEntityManager().find(result.getClass(), result.getId());
			assertTrue(same(result, saved), "Saved and readed entities are not same.");
		}

		@Test
		@Order(100)
		void jpaPlayerInsertTest() {
			saveAndReadSame(player);
		}
		
		@Test
		@Order(120)
		void jpaGameInsertTest() {
			saveAndReadSame(game);
		}

		@Test
		@Order(130)
		void jpaPlatformGameInsertTest() {
			saveAndReadSame(platformGame);
		}

		@Test
		@Order(140)
		void jpaFirstPersonShooterInsertTest() {
			saveAndReadSame(firstPersonShooter);
		}

		@Test
		@Order(150)
		void jpaScoreInsertTest() {
			saveAndReadSame(Score.generate(connector.getAll(Player.class), connector.getAll(Game.class)));
		}

		@Test
		@Order(200)
		void jpaPlayerReadTest() {
			List<Player> savedEntities = connector.getAll(Player.class).stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}

		@Test
		@Order(210)
		void jpaGameReadTest() {
			List<Game> savedEntities = connector.getAll(Game.class).stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}
		@Test
		@Order(220)
		void jpaPlatformGameReadTest() {
			List<PlatformGame> savedEntities = connector.getAll(PlatformGame.class).stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}
		@Test
		@Order(230)
		void jpaFirstPersonShooterReadTest() {
			List<FirstPersonShooter> savedEntities = connector.getAll(FirstPersonShooter.class).stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}
		@Test
		@Order(240)
		void jpaScoreReadTest() {
			List<Score> savedEntities = connector.getAll(Score.class).stream().toList();
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
		}

		@Test
		@Order(250)
		void jpaGameScoresContainScoreTest() {
			List<Score> savedEntities = connector.getAll(Score.class).stream().toList();
			for (Score score : savedEntities) {
				assertThat(score.getGame().getScores(), Matchers.containsInRelativeOrder(score));
			}
		}
		@Test
		@Order(260)
		void jpaPlayerScoresContainScoreTest() {
			List<Score> savedEntities = connector.getAll(Score.class).stream().toList();
			for (Score score : savedEntities) {
				assertThat(score.getPlayer().getScores(), Matchers.containsInRelativeOrder(score));
			}
		}
		@Test
		@Order(270)
		void jpaScoreToStringTest() {
			List<Score> savedEntities = connector.getAll(Score.class);
			for (Score score : savedEntities) {
				assertThat(score.toString(), Matchers.notNullValue());
			}
		}
		@Test
		@Order(280)
		void jpaPlayerToStringTest() {
			List<Player> savedEntities = connector.getAll(Player.class);
			for (Player p : savedEntities) {
				assertThat(p.toString(), Matchers.notNullValue());
			}
		}
		
		private void deleteAllAndAssert(Class<? extends MyEntity> clazz) {
			List<? extends MyEntity> savedEntities = connector.getAll(clazz);
			assertThat(savedEntities, Matchers.not(Matchers.hasSize(0)));
			connector.delete(savedEntities);
			List<? extends MyEntity> modifiedEntities = connector.getAll(clazz);
			assertThat(modifiedEntities, Matchers.hasSize(0));
		}
		
		@Test
		@Order(300)
		void jpaScoreDeleteTest() {
			deleteAllAndAssert(Score.class);
		}
		@Test
		@Order(310)
		void jpaPlayerDeleteTest() {
			deleteAllAndAssert(Player.class);
		}

		@Test
		@Order(320)
		void jpaGameDeleteTest() {
			deleteAllAndAssert(Game.class);
		}
		
		@Test
		@Order(400)
		void jpaFindByNullNUllTest() {
			connector.save(player);
			game.setName("aa-test-bb");
			connector.save(game);
			Game g2 = Game.generate();
			g2.setName("aa--bb");
			connector.save(g2);
			connector.save(Score.builder().points(100).difficult(Difficult.EASY).player(player).game(game).build());
			connector.save(Score.builder().points(100).difficult(Difficult.HARD).player(player).game(game).build());
			connector.save(Score.builder().points(100).difficult(Difficult.EASY).player(player).game(g2).build());
			connector.save(Score.builder().points(100).difficult(Difficult.HARD).player(player).game(g2).build());
			assertThat(connector.findBy(null, null), Matchers.hasSize(4));
		}

		@Test
		@Order(410)
		void jpaFindByDiffTest() {
			assertThat(connector.findBy(null, Difficult.HARD), Matchers.hasSize(2));
		}

		@Test
		@Order(420)
		void jpaFindByNameTest() {
			assertThat(connector.findBy("test", null), Matchers.hasSize(2));
		}

		@Test
		@Order(430)
		void jpaFindByNameAndDiffTest() {
			assertThat(connector.findBy("test", Difficult.EASY), Matchers.hasSize(1));
		}

		@Test
		@Order(440)
		void jpaFindByNameAndDiff2Test() {
			assertThat(connector.findBy("--", Difficult.HARD), Matchers.hasSize(1));
		}
	}

}
