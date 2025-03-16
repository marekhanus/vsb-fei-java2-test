package lab.storage;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Stream;

import lab.data.Score;

public class FileStorage implements ScoreStorageInterface {

	private static final String SCORE_FILE_NAME = "scores.csv";

	@Override
	public List<Score> getAll() {
		if (Files.exists(Paths.get(SCORE_FILE_NAME))) {
			try (Stream<String> lines = Files.lines(Paths.get(SCORE_FILE_NAME))) {
				List<Score> result = lines.map(line -> line.split(";"))
						.map(parts -> new Score(null, parts[0], Integer.parseInt(parts[1]), null)).toList();
				return new ArrayList<>(result);
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
		return new ArrayList<>();
	}

	@Override
	public List<Score> getFirstTen() {
		List<Score> all = getAll();
		Collections.sort(all, Comparator.comparing(Score::getPoints).reversed());
		return all.subList(0, 10);
	}

	@Override
	public void init() {
		/* noting to do */
	}

	@Override
	public Score save(Score score) {
		List<Score> all = getAll();
		all.add(score);
		storeAll(all);
		return score;
	}

	private void storeAll(List<Score> all) {
		List<String> lines = all.stream().map(s -> String.format("%s;%d", s.getName(), s.getPoints())).toList();
		try {
			Files.write(Paths.get(SCORE_FILE_NAME), lines, StandardOpenOption.TRUNCATE_EXISTING);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void delete(List<Score> scores) {
		List<Score> all = getAll();
		all.removeAll(scores);
		storeAll(all);
		
	}

	@Override
	public void stop() {
		/*nothing to do*/
	}

}
