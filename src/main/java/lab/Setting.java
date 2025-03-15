package lab;

import lab.storage.DbConnector;
import lab.storage.ScoreStorageInterface;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Builder.Default;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

@Getter
@Builder(toBuilder = true)
@EqualsAndHashCode
@ToString
@AllArgsConstructor
public class Setting {

	@Getter
	private static Setting instance;


	@Default
	private ScoreStorageInterface scoreStorageInterface = new DbConnector();
	@Default
	private double gravity = 9.81;
	@Default
	private double normalBulletSpeed = 30;
	@Default
	private int numberOfUfos = 3;
	@Default
	private double ufoMinPercentageHeight = 0.3;
	@Default
	private double ufoMinSpeed = 70;
	@Default
	private double ufoMaxSpeed = 150;
	@Default
	private double bulletMinSpeed = 30;
	@Default
	private double bulletMaxSpeed = 300;

	public static void configure(Setting setting) {
		instance = setting;
	}

	public static Setting getInstanceForHardcoreGame() {
		return builder().numberOfUfos(50).ufoMinPercentageHeight(0.9).ufoMinSpeed(200).ufoMaxSpeed(500).build();
	}


}
