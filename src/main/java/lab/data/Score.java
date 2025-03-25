package lab.data;

import java.util.List;

import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lab.Tools;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Score implements MyEntity {

	private Long id;
	private int points;
	@Enumerated(EnumType.STRING)
	private Difficult difficult;
	private Player player;
	private Game game;

	public static Score generate(List<Player> players, List<Game> games) {
		return new Score(null, Tools.RANDOM.nextInt(100, 2000), Tools.random(Difficult.values()),
				Tools.randomElementFrom(players), Tools.randomElementFrom(games));
	}

	public enum Difficult {
		EASY, MEDIUM, HARD;
	}
}
