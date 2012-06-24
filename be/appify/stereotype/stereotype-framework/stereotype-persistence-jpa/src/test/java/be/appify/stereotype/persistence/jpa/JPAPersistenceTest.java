package be.appify.stereotype.persistence.jpa;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
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
		Transaction transaction = persistence.startTransaction();
		transaction.deleteAll(Advice.class);
		transaction.commit();
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

	@Test
	public void findAllShouldReturnAllInstances() {
		Advice advice1 = new Advice("name1");
		advice1.setDescription("description1");
		Advice advice2 = new Advice("name2");
		advice2.setDescription("description2");
		Transaction transaction = persistence.startTransaction();
		transaction.save(advice1);
		transaction.save(advice2);
		transaction.commit();

		transaction = persistence.startTransaction();
		List<Advice> loadedAdvices = transaction.findAll(Advice.class);
		transaction.rollback();
		assertNotNull(loadedAdvices);
		assertEquals(2, loadedAdvices.size());
		assertTrue(loadedAdvices.contains(advice1));
		assertTrue(loadedAdvices.contains(advice2));
	}

	@Test
	public void findAllContainingShouldReturnAllInstancesContainingSearchString() {
		Advice advice1 = new Advice("name1");
		advice1.setDescription("description1");
		Advice advice2 = new Advice("name2");
		advice2.setDescription("description2");
		Advice advice3 = new Advice("something else");
		advice3.setDescription("description2");
		Transaction transaction = persistence.startTransaction();
		transaction.save(advice1);
		transaction.save(advice2);
		transaction.save(advice3);
		transaction.commit();

		transaction = persistence.startTransaction();
		List<Advice> loadedAdvices = transaction.findAllContaining(Advice.class, "name", "Name");
		transaction.rollback();
		assertNotNull(loadedAdvices);
		assertEquals(2, loadedAdvices.size());
		assertTrue(loadedAdvices.contains(advice1));
		assertTrue(loadedAdvices.contains(advice2));
		assertFalse(loadedAdvices.contains(advice3));
	}
}
