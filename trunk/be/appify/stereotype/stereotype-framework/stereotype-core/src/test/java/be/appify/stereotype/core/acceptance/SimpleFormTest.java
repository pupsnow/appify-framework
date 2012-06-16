package be.appify.stereotype.core.acceptance;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;

import java.util.Map;
import java.util.UUID;

import org.junit.Before;
import org.junit.Test;

import be.appify.stereotype.core.Advice;
import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.beans.SimpleBeanModelRegistry;
import be.appify.stereotype.core.beans.validation.MaxLengthValidator;
import be.appify.stereotype.core.beans.validation.MinLengthValidator;
import be.appify.stereotype.core.beans.validation.RequiredValidator;
import be.appify.stereotype.core.beans.validation.Validator;
import be.appify.stereotype.core.beans.validation.ValidatorFactory;
import be.appify.stereotype.core.operation.CreateOperation;
import be.appify.stereotype.core.operation.FindByIDOperation;
import be.appify.stereotype.core.operation.SpawningOperation;
import be.appify.stereotype.core.session.Session;
import be.appify.stereotype.core.session.SessionManager;
import be.appify.stereotype.core.session.inmemory.InMemorySessionManager;

import com.google.common.collect.Maps;
import com.google.common.collect.Sets;

public class SimpleFormTest {
	private SessionManager sessionManager;

	@Before
	public void before() {
		BeanModelRegistry beanModelRegistry = new SimpleBeanModelRegistry(new ValidatorFactory(Sets.<Validator<?>> newHashSet(
				new MaxLengthValidator(),
				new MinLengthValidator(),
				new RequiredValidator())), Advice.class);
		sessionManager = new InMemorySessionManager(beanModelRegistry);
	}
	
	@Test
	public void shouldFindCreatedEntityById() {
		Session session = sessionManager.newSession();
		
		SpawningOperation<Advice> create = session.newOperation(Advice.class, CreateOperation.class);
		
		Map<String, Object> values = Maps.newHashMap();
		values.put("name", "Carpool to work");
		values.put("description", "Carpooling reduces environmental impact, traffic congestions and stress.");
		Advice createdAdvice = create.execute(values);
		assertNotNull(createdAdvice);
		assertEquals("Carpool to work", createdAdvice.getName());
		assertEquals("Carpooling reduces environmental impact, traffic congestions and stress.", createdAdvice.getDescription());
		
		UUID id = session.getID(createdAdvice);
		
		SpawningOperation<Advice> find = session.newOperation(Advice.class, FindByIDOperation.class);
		
		Advice adviceFound = find.execute(id);
		assertNotNull(adviceFound);
		assertEquals("Carpool to work", adviceFound.getName());
		assertEquals("Carpooling reduces environmental impact, traffic congestions and stress.", adviceFound.getDescription());
	}
}
