package be.appify.stereotype.persistence.jpa;

import static org.junit.Assert.assertEquals;

import java.util.UUID;

import javax.persistence.EntityManagerFactory;

import org.junit.Before;
import org.junit.Test;

import be.appify.stereotype.core.persistence.Persistence;
import be.appify.stereotype.core.persistence.Transaction;

public class JPAPersistenceTest {
	private Persistence persistence;

	@Before
	public void before() {
		EntityManagerFactory entityManagerFactory = new StereotypeEntityManagerFactory();
		persistence = new JPAPersistence(entityManagerFactory);
	}

	@Test
	public void saveShouldStoreBean() {
		Advice advice = new Advice("name");
		advice.setDescription("description");
		UUID id = advice.getID();
		Transaction transaction = persistence.startTransaction();
		transaction.save(advice);
		transaction.commit();

		transaction = persistence.startTransaction();
		Advice loadedAdvice = transaction.findByID(Advice.class, id);
		transaction.rollback();
		assertEquals("name", loadedAdvice.getName());
		assertEquals("description", loadedAdvice.getDescription());
		assertEquals(id, loadedAdvice.getID());
	}
}
