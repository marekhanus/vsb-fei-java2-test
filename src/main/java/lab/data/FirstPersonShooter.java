package lab.data;

import java.util.List;

import jakarta.persistence.Entity;
import lab.Tools;
import lab.data.PlatformGame.PlatformType;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@ToString(callSuper = true)
public class FirstPersonShooter extends Game{

	private FPSType fpsType;
	private int playersCount;

	enum FPSType{
		DEATHMATCH, TEAM_DEATHMATCH, KING_OF_THE_HILL, ASSAULT, BATTLE_ROYALE, INFECTION;
	}

	public FirstPersonShooter(Long id, String name, List<Score> scores, FPSType fpsType, int playersCount) {
		super(id, name, scores);
		this.fpsType = fpsType;
		this.playersCount = playersCount;
	}

	public static FirstPersonShooter generate() {
		return new FirstPersonShooter(null, Tools.randomGameName(), null, Tools.random(FPSType.values()), Tools.random(new Integer[]{1,2,5,10,20,50,100}));
	}
	
}
