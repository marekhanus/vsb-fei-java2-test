package lab.storage;

import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lab.data.Score;

public class JpaConnector implements ScoreStorageInterface {

	private EntityManagerFactory emf;
	private EntityManager em;

	public JpaConnector() {
		emf = Persistence.createEntityManagerFactory("java2");
		em = emf.createEntityManager();
	}

	@Override
	public List<Score> getAll() {
		return em.createQuery("select s from Score s", Score.class).getResultList();
	}

	@Override
	public List<Score> getFirstTen() {
		return em.createQuery("select s from Score s order by s.points DESC", Score.class).setMaxResults(10)
				.getResultList();
	}

	@Override
	public void init() {
	}

	@Override
	public Score save(Score score) {
		Score result;
		em.getTransaction().begin();
		if (score.getId() == null || score.getId() == 0) {
			em.persist(score);
			result = score;
		} else {
			result = em.merge(score);
		}
		em.getTransaction().commit();
		return result;
	}

	@Override
	public void delete(List<Score> scores) {
		em.getTransaction().begin();
		for (Score score : scores) {
			em.remove(score);
		}
		em.getTransaction().commit();
	}

	@Override
	public void stop() {
		em.close();
		emf.close();
	}

	public Object getEntityManager() {
		//return entity manager. Type Object is there because of compilation of empty task assignment
		return em;
	}

	public Score find(long id) {
		return em.find(Score.class, id);
	}
	
	public void modifyNoPersistOrMerge(long id, Consumer<Score> motificator) {
		em.getTransaction().begin();
		Score score = em.find(Score.class, id);
		motificator.accept(score);
		em.getTransaction().commit();
	}
	
}
