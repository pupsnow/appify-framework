package be.appify.stereotype.persistence.jpa;

import java.util.Map;

import javax.inject.Named;
import javax.persistence.Cache;
import javax.persistence.EntityManager;
import javax.persistence.EntityManagerFactory;
import javax.persistence.Persistence;
import javax.persistence.PersistenceUnitUtil;
import javax.persistence.criteria.CriteriaBuilder;
import javax.persistence.metamodel.Metamodel;

@Named
public class StereotypeEntityManagerFactory implements EntityManagerFactory {
	private final EntityManagerFactory delegate;

	public StereotypeEntityManagerFactory() {
		this("stereotype");
	}

	public StereotypeEntityManagerFactory(String persistenceUnitName) {
		delegate = Persistence.createEntityManagerFactory(persistenceUnitName);
	}

	@Override
	public void close() {
		delegate.close();
	}

	@Override
	public EntityManager createEntityManager() {
		return delegate.createEntityManager();
	}

	@Override
	public EntityManager createEntityManager(@SuppressWarnings("rawtypes") Map map) {
		return delegate.createEntityManager(map);
	}

	@Override
	public Cache getCache() {
		return delegate.getCache();
	}

	@Override
	public CriteriaBuilder getCriteriaBuilder() {
		return delegate.getCriteriaBuilder();
	}

	@Override
	public Metamodel getMetamodel() {
		return delegate.getMetamodel();
	}

	@Override
	public PersistenceUnitUtil getPersistenceUnitUtil() {
		return delegate.getPersistenceUnitUtil();
	}

	@Override
	public Map<String, Object> getProperties() {
		return delegate.getProperties();
	}

	@Override
	public boolean isOpen() {
		return delegate.isOpen();
	}

}
