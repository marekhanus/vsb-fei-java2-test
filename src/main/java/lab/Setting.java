package lab;

import lab.storage.DbConnector;
import lab.storage.ScoreStorageInterface;

public class Setting {

	private static Setting instance;

	private ScoreStorageInterface scoreStorageInterface;
	private double gravity;
	private double normalBulletSpeed;
	private int numberOfUfos;
	private double ufoMinPercentageHeight;
	private double ufoMinSpeed;
	private double ufoMaxSpeed;
	private double bulletMinSpeed;
	private double bulletMaxSpeed;

	public static void configure(Setting setting) {
		instance = setting;
	}

	private Setting(ScoreStorageInterface scoreStorageInterface, double gravity, double normalBulletSpeed,
			int numberOfUfos, double ufoMinPercentageHeight, double ufoMinSpeed, double ufoMaxSpeed,
			double bulletMinSpeed, double bulletMaxSpeed) {
		super();
		this.scoreStorageInterface = scoreStorageInterface;
		this.gravity = gravity;
		this.normalBulletSpeed = normalBulletSpeed;
		this.numberOfUfos = numberOfUfos;
		this.ufoMinPercentageHeight = ufoMinPercentageHeight;
		this.ufoMinSpeed = ufoMinSpeed;
		this.ufoMaxSpeed = ufoMaxSpeed;
		this.bulletMinSpeed = bulletMinSpeed;
		this.bulletMaxSpeed = bulletMaxSpeed;
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

	public static Builder builder() {
		return new Builder();
	}

	public static Setting getInstanceForHardcoreGame() {
		return builder().numberOfUfos(50).ufoMinPercentageHeight(0.9).ufoMinSpeed(200).ufoMaxSpeed(500).build();
	}

	public static class Builder {
		private ScoreStorageInterface scoreStorageInterface = new DbConnector();
		private double gravity = 9.81;
		private double normalBulletSpeed = 30;
		private int numberOfUfos = 3;
		private double ufoMinPercentageHeight = 0.3;
		private double ufoMinSpeed = 70;
		private double ufoMaxSpeed = 150;
		private double bulletMinSpeed = 30;
		private double bulletMaxSpeed = 300;

		public Builder bulletMaxSpeed(double bulletMaxSpeed) {
			this.bulletMaxSpeed = bulletMaxSpeed;
			return this;
		}

		public Builder scoreStorageInterface(ScoreStorageInterface scoreStorageInterface) {
			this.scoreStorageInterface = scoreStorageInterface;
			return this;
		}

		public Builder gravity(double gravity) {
			this.gravity = gravity;
			return this;
		}

		public Builder normalBulletSpeed(double normalBulletSpeed) {
			this.normalBulletSpeed = normalBulletSpeed;
			return this;
		}

		public Builder numberOfUfos(int numberOfUfos) {
			this.numberOfUfos = numberOfUfos;
			return this;
		}

		public Builder ufoMinPercentageHeight(double ufoMinPercentageHeight) {
			this.ufoMinPercentageHeight = ufoMinPercentageHeight;
			return this;
		}

		public Builder ufoMinSpeed(double ufoMinSpeed) {
			this.ufoMinSpeed = ufoMinSpeed;
			return this;
		}

		public Builder ufoMaxSpeed(double ufoMaxSpeed) {
			this.ufoMaxSpeed = ufoMaxSpeed;
			return this;
		}

		public Builder bulletMinSpeed(double bulletMinSpeed) {
			this.bulletMinSpeed = bulletMinSpeed;
			return this;
		}

		public Setting build() {
			return new Setting(scoreStorageInterface, gravity, normalBulletSpeed, numberOfUfos, ufoMinPercentageHeight,
					ufoMinSpeed, ufoMaxSpeed, bulletMinSpeed, bulletMaxSpeed);
		}
	}
}
