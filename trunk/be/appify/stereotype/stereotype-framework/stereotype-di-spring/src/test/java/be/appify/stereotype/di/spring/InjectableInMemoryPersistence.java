package be.appify.stereotype.di.spring;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.persistence.inmemory.InMemoryPersistence;

@Named
public class InjectableInMemoryPersistence extends InMemoryPersistence {

	@Inject
	@Override
	public void setBeanModelRegistry(BeanModelRegistry beanModelRegistry) {
		super.setBeanModelRegistry(beanModelRegistry);
	}

}
