package lab;

import lab.storage.DbConnector;
import lab.storage.ScoreStorageInterface;

public class Setting {

	private static Setting instance;

	private ScoreStorageInterface scoreStorageInterface = new DbConnector();
	private double gravity = 9.81;
	private double normalBulletSpeed = 30;
	private int numberOfUfos = 3;
	private double ufoMinPercentageHeight = 0.3;
	private double ufoMinSpeed = 70;
	private double ufoMaxSpeed = 150;
	private double bulletMinSpeed = 30;
	private double bulletMaxSpeed = 300;

	public static void configure(Setting setting) {
		instance = setting;
	}

	public static Setting getInstance() {
		return instance;
	}

	public double getGravity() {
		return gravity;
	}

	public double getNormalBulletSpeed() {
		return normalBulletSpeed;
	}

	public int getNumberOfUfos() {
		return numberOfUfos;
	}

	public ScoreStorageInterface getScoreStorageInterface() {
		return scoreStorageInterface;
	}

	public double getUfoMinPercentageHeight() {
		return ufoMinPercentageHeight;
	}

	public double getUfoMinSpeed() {
		return ufoMinSpeed;
	}

	public double getUfoMaxSpeed() {
		return ufoMaxSpeed;
	}

	public double getBulletMinSpeed() {
		return bulletMinSpeed;
	}

	public double getBulletMaxSpeed() {
		return bulletMaxSpeed;
	}

}
