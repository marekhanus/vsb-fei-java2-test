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
		//TODO
		return null;
	}

	public<T extends MyEntity> T save(T score) {
		//TODO
		return null;
	}

	public void delete(List<? extends MyEntity> e) {
		//TODO
	}

	public void stop() {
		em.close();
		emf.close();
	}

	public EntityManager getEntityManager() {
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
		//TODO
		return null;
	}
	
}
