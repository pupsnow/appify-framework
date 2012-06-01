package be.appify.repository.jpa;

import javax.enterprise.context.ConversationScoped;
import javax.enterprise.inject.Produces;
import javax.inject.Singleton;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;

@Singleton
public class DefaultEntityManagerProducer implements EntityManagerProducer {
	private static EntityManagerFactory factory;

	@Produces
	public EntityManagerFactory getEntityManagerFactory() {
		if (factory == null) {
			factory = Persistence.createEntityManagerFactory("appify");
		}
		return factory;
	}

	@Produces
	@ConversationScoped
	public EntityManager produceEntityManager() {
		return getEntityManagerFactory().createEntityManager();
	}
}
