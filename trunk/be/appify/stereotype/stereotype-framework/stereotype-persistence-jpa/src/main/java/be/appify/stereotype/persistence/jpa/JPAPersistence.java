package be.appify.stereotype.persistence.jpa;

import javax.inject.Inject;
import javax.inject.Named;
import javax.persistence.EntityManagerFactory;

import be.appify.stereotype.core.persistence.Persistence;
import be.appify.stereotype.core.persistence.Transaction;

@Named
public class JPAPersistence implements Persistence {
	private final EntityManagerFactory entityManagerFactory;

	@Inject
	public JPAPersistence(EntityManagerFactory entityManagerFactory) {
		this.entityManagerFactory = entityManagerFactory;
	}

	@Override
	public Transaction startTransaction() {
		return new JPATransaction(entityManagerFactory.createEntityManager());
	}

}
