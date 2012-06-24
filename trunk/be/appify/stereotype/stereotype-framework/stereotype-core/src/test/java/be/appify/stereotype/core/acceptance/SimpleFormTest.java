package be.appify.stereotype.core.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.List;
import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import be.appify.stereotype.core.Advice;
import be.appify.stereotype.core.beans.Bean;
import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.beans.SimpleBeanModelRegistry;
import be.appify.stereotype.core.beans.validation.MaxLengthValidator;
import be.appify.stereotype.core.beans.validation.MinLengthValidator;
import be.appify.stereotype.core.beans.validation.RequiredValidator;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.beans.validation.ValidatorFactory;
import be.appify.stereotype.core.operation.CreateOperation;
import be.appify.stereotype.core.operation.FindAllContainingOperation;
import be.appify.stereotype.core.operation.FindAllOperation;
import be.appify.stereotype.core.operation.FindByIDOperation;
import be.appify.stereotype.core.operation.GenericOperation;
import be.appify.stereotype.core.operation.ListingOperation;
import be.appify.stereotype.core.operation.ManipulatingOperation;
import be.appify.stereotype.core.operation.OperationFactory;
import be.appify.stereotype.core.operation.SpawningOperation;
import be.appify.stereotype.core.operation.UpdateOperation;
import be.appify.stereotype.core.persistence.inmemory.InMemoryPersistence;
import be.appify.stereotype.core.session.GenericSessionManager;
import be.appify.stereotype.core.session.Session;
import be.appify.stereotype.core.session.SessionManager;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SimpleFormTest {
	private SessionManager sessionManager;

	@Before
	public void before() {
		InMemoryPersistence persistence = new InMemoryPersistence();
		BeanModelRegistry beanModelRegistry = new SimpleBeanModelRegistry(new ValidatorFactory(
				Sets.<Validator<?>> newHashSet(
						new MaxLengthValidator(),
						new MinLengthValidator(),
						new RequiredValidator())),
				new OperationFactory(Sets.<GenericOperation<?>> newHashSet(
						new CreateOperation<Bean>(persistence),
						new FindByIDOperation<Bean>(persistence),
						new UpdateOperation<Bean>(persistence),
						new FindAllOperation<Bean>(persistence),
						new FindAllContainingOperation<Bean>(persistence))));
		beanModelRegistry.initialize(Advice.class);
		persistence.setBeanModelRegistry(beanModelRegistry);
		sessionManager = new GenericSessionManager(beanModelRegistry);
	}

	@Test
	public void shouldFindCreatedEntityById() {
		Session session = sessionManager.newSession();
		SpawningOperation<Advice> create = session.newSpawningOperation(Advice.class, CreateOperation.class);
		Map<String, Object> values = Maps.newHashMap();
		values.put("name", "Carpool to work");
		values.put("description", "Carpooling reduces environmental impact, traffic congestions and stress.");
		Advice createdAdvice = create.execute(values);
		assertNotNull(createdAdvice);
		assertEquals("Carpool to work", createdAdvice.getName());
		assertEquals("Carpooling reduces environmental impact, traffic congestions and stress.",
				createdAdvice.getDescription());

		UUID id = createdAdvice.getID();
		SpawningOperation<Advice> find = session.newSpawningOperation(Advice.class, FindByIDOperation.class);
		values = Maps.newHashMap();
		values.put("id", id);
		Advice adviceFound = find.execute(values);
		assertNotNull(adviceFound);
		assertEquals("Carpool to work", adviceFound.getName());
		assertEquals("Carpooling reduces environmental impact, traffic congestions and stress.",
				adviceFound.getDescription());
	}

	@Test
	public void shouldFindAllCreatedEntities() {
		Session session = sessionManager.newSession();
		SpawningOperation<Advice> create = session.newSpawningOperation(Advice.class, CreateOperation.class);
		Map<String, Object> values = Maps.newHashMap();
		values.put("name", "Carpool to work");
		values.put("description", "Carpooling reduces environmental impact, traffic congestions and stress.");
		Advice createdAdvice1 = create.execute(values);

		values = Maps.newHashMap();
		values.put("name", "Bike to work");
		values.put("description", "Biking to work is good for the environment and for your health.");
		Advice createdAdvice2 = create.execute(values);

		ListingOperation<Advice> list = session.newListingOperation(Advice.class, FindAllOperation.class);
		List<Advice> advicesFound = list.execute(Maps.<String, Object> newHashMap());
		assertNotNull(advicesFound);
		assertEquals(2, advicesFound.size());
		assertTrue(advicesFound.contains(createdAdvice1));
		assertTrue(advicesFound.contains(createdAdvice2));

	}

	@Test
	public void shouldFindAllCreatedEntitiesContainingSubstring() {
		Session session = sessionManager.newSession();
		SpawningOperation<Advice> create = session.newSpawningOperation(Advice.class, CreateOperation.class);
		Map<String, Object> values = Maps.newHashMap();
		values.put("name", "Carpool to work");
		values.put("description", "Carpooling reduces environmental impact, traffic congestions and stress.");
		Advice createdAdvice1 = create.execute(values);

		values = Maps.newHashMap();
		values.put("name", "Bike to work");
		values.put("description", "Biking to work is good for the environment and for your health.");
		Advice createdAdvice2 = create.execute(values);

		values = Maps.newHashMap();
		values.put("name", "Stop your engine at the traffic light");
		values.put("description", "Keeping your engine running while stationary unnecessarily consumes fuel.");
		Advice createdAdvice3 = create.execute(values);

		ListingOperation<Advice> list = session.newListingOperation(Advice.class, FindAllContainingOperation.class);
		values = Maps.newHashMap();
		values.put("search", "Work");
		List<Advice> advicesFound = list.execute(values);
		assertNotNull(advicesFound);
		assertEquals(2, advicesFound.size());
		assertTrue(advicesFound.contains(createdAdvice1));
		assertTrue(advicesFound.contains(createdAdvice2));
		assertFalse(advicesFound.contains(createdAdvice3));

	}

	@Test
	public void shouldUpdateEntity() {
		Session session = sessionManager.newSession();
		SpawningOperation<Advice> create = session.newSpawningOperation(Advice.class, CreateOperation.class);
		Map<String, Object> values = Maps.newHashMap();
		values.put("name", "Carpool to work");
		values.put("description", "Carpooling reduces environmental impact, traffic congestions and stress.");
		Advice createdAdvice = create.execute(values);
		assertNotNull(createdAdvice);
		assertEquals("Carpool to work", createdAdvice.getName());
		assertEquals("Carpooling reduces environmental impact, traffic congestions and stress.",
				createdAdvice.getDescription());

		UUID id = createdAdvice.getID();
		ManipulatingOperation<Advice> manipulatingOperation = session.newManipulatingOperation(Advice.class,
				UpdateOperation.class);
		values = Maps.newHashMap();
		values.put("id", id);
		values.put("description", "Updated description");
		manipulatingOperation.execute(values);

		SpawningOperation<Advice> find = session.newSpawningOperation(Advice.class, FindByIDOperation.class);
		values = Maps.newHashMap();
		values.put("id", id);
		Advice adviceFound = find.execute(values);
		assertNotNull(adviceFound);
		assertEquals("Carpool to work", adviceFound.getName());
		assertEquals("Updated description", adviceFound.getDescription());
	}
}
