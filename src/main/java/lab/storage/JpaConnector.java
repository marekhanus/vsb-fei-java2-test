package lab.storage;

import java.util.List;
import java.util.function.Consumer;

import jakarta.persistence.EntityManager;
import jakarta.persistence.EntityManagerFactory;
import jakarta.persistence.Persistence;
import lab.data.MyEntity;
import lab.data.Score;

public class JpaConnector {

	private EntityManagerFactory emf;
	private EntityManager em;

	public JpaConnector() {
		emf = Persistence.createEntityManagerFactory("java2");
		em = emf.createEntityManager();
	}

	public<T extends MyEntity> List<T> getAll(Class<T> clazz) {
		return null;
	}

	public void init() {
	}

	public<T extends MyEntity> T save(T score) {
		return null;
	}

	public void delete(List<? extends MyEntity> e) {
	}

	public void stop() {
		em.close();
		emf.close();
	}

	public EntityManager getEntityManager() {
		//return entity manager. Type Object is there because of compilation of empty task assignment
		return em;
	}

	public<T> T find(long id, Class<T> clazz) {
		return em.find(clazz, id);
	}
	
	public void modifyNoPersistOrMerge(long id, Consumer<Score> motificator) {
		em.getTransaction().begin();
		Score score = em.find(Score.class, id);
		motificator.accept(score);
		em.getTransaction().commit();
	}

	
	public List<Score> findBy(String partialName, Score.Difficult difficult){
		return null;
	}
	
}
