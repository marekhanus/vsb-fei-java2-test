package lab.data;

import java.util.List;

import lab.Tools;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString
@Builder(toBuilder = true)
public class Game implements MyEntity{

	private Long id;
	private String name;
	private List<Score> scores;

	public static Game generate() {
		return new Game(null, Tools.randomGameName(), null);
	}

	public static Game generateAny() {
		return switch (Tools.RANDOM.nextInt(3)) {
		case 0 -> generate();
		case 1 -> PlatformGame.generate();
		case 2 -> FirstPersonShooter.generate();
		default -> generate();
		};
	}
}
