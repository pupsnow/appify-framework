package be.appify.gae.repository.jpa;

import javax.decorator.Decorator;
import javax.decorator.Delegate;
import javax.inject.Inject;
import javax.jdo.JDOEnhancer;
import javax.jdo.JDOHelper;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;

import be.appify.repository.jpa.EntityManagerProducer;

@Decorator
public class JDOEntityManagerProducerDecorator implements EntityManagerProducer {

	private EntityManagerProducer delegate;
	private boolean enhanced = false;

	@Inject
	public JDOEntityManagerProducerDecorator(
			@Delegate EntityManagerProducer delegate) {
		this.delegate = delegate;
	}

	@Override
	public EntityManagerFactory getEntityManagerFactory() {
		if (!enhanced) {
			JDOEnhancer enhancer = JDOHelper.getEnhancer();
			enhancer.setVerbose(true);
			enhancer.addPersistenceUnit("appify");
			enhancer.enhance();
			enhanced = true;
		}
		return delegate.getEntityManagerFactory();
	}

	@Override
	public EntityManager produceEntityManager() {
		return delegate.produceEntityManager();
	}
}
