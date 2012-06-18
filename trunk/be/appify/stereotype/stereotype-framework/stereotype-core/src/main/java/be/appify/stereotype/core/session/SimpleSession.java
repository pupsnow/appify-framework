package be.appify.stereotype.core.session;

import java.util.UUID;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.operation.GenericOperation;
import be.appify.stereotype.core.operation.ManipulatingOperation;
import be.appify.stereotype.core.operation.SpawningOperation;
import be.appify.stereotype.core.persistence.Persistence;

public class SimpleSession implements Session {

	private final BeanModelRegistry beanModelRegistry;
	private final Persistence persistence;

	public SimpleSession(Persistence persistence, BeanModelRegistry beanModelRegistry) {
		this.persistence = persistence;
		this.beanModelRegistry = beanModelRegistry;
	}

	@Override
	public <B, O extends GenericOperation<?>> SpawningOperation<B> newSpawningOperation(Class<B> beanClass,
			Class<O> operationClass) {
		BeanModel<B> model = beanModelRegistry.getBeanModel(beanClass);
		return (SpawningOperation<B>) model.newOperation(operationClass);
	}

	@Override
	public <B> UUID getID(B bean) {
		return persistence.getID(bean);
	}

	@Override
	public <B, O extends GenericOperation<?>> ManipulatingOperation<B> newManipulatingOperation(Class<B> beanClass,
			Class<O> operationClass) {
		BeanModel<B> model = beanModelRegistry.getBeanModel(beanClass);
		return (ManipulatingOperation<B>) model.newOperation(operationClass);
	}

}
