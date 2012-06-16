package be.appify.stereotype.core.session;

import java.util.UUID;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.operation.SpawningOperation;
import be.appify.stereotype.core.persistence.Persistence;

public class SimpleSession implements Session {

	private BeanModelRegistry beanModelRegistry;
	private Persistence persistence;

	public SimpleSession(Persistence persistence, BeanModelRegistry beanModelRegistry) {
		this.persistence = persistence;
		this.beanModelRegistry = beanModelRegistry;
	}

	@Override
	public <B, O extends SpawningOperation<?>> SpawningOperation<B> newOperation(
			Class<B> beanClass, Class<O> operationClass) {
		BeanModel<B> model = beanModelRegistry.getBeanModel(beanClass);
		return model.newOperation(operationClass);
	}

	@Override
	public <B> UUID getID(B bean) {
		return persistence.getID(bean);
	}

}
