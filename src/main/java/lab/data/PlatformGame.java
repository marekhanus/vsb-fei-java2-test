package lab.data;

import java.util.List;

import lab.Tools;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class PlatformGame extends Game {

	private boolean retro;
	private PlatformType platformType;

	enum PlatformType {
		SINGLE_SCREEN, SCROLLING;
	}

	public PlatformGame(Long id, String name, List<Score> scores, boolean retro, PlatformType platformType) {
		super(id, name, scores);
		this.retro = retro;
		this.platformType = platformType;
	}

	public static PlatformGame generate() {
		return new PlatformGame(null, Tools.randomGameName(), null, Tools.RANDOM.nextBoolean(),
				Tools.random(PlatformType.values()));
	}

}
