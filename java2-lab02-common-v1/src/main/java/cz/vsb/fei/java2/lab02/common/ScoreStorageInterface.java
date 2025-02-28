package cz.vsb.fei.java2.lab02.common;

import java.util.List;

public interface ScoreStorageInterface {

	List<Score> getAll();

	List<Score> getFirstTen();

	void init();

	void insertScore(Score score);

}