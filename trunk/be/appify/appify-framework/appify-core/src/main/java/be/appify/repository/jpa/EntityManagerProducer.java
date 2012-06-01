package be.appify.repository.jpa;

import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

public interface EntityManagerProducer {
	EntityManagerFactory getEntityManagerFactory();
	
	EntityManager produceEntityManager();
}
