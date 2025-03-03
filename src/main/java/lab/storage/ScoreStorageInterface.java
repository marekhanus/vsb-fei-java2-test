package lab.storage;

import java.util.List;

import lab.data.Score;

public interface ScoreStorageInterface {

	List<Score> getAll();

	List<Score> getFirstTen();

	void init();

	void insertScore(Score score);

}