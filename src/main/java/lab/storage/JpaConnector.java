package lab.storage;

import java.util.Collections;
import java.util.List;
import java.util.function.Consumer;

import lab.data.Score;

public class JpaConnector implements ScoreStorageInterface {


	public JpaConnector() {
	}

	@Override
	public List<Score> getAll() {
		return Collections.emptyList();
	}

	@Override
	public List<Score> getFirstTen() {
		return Collections.emptyList();
	}

	@Override
	public void init() {
	}

	@Override
	public Score save(Score score) {
		return null;
	}

	@Override
	public void delete(List<Score> scores) {
	}

	@Override
	public void stop() {
	}

	public Object getEntityManager() {
		//return entity manager. Type Object is there because of compilation of empty task assignment
		return null;
	}

	public Score find(long id) {
		return null;
	}
	
	public void modifyNoPersistOrMerge(long id, Consumer<Score> motificator) {
	}
	
}
