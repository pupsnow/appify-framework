package be.appify.stereotype.core.session;

import java.util.UUID;

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
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public <B> UUID getID(B bean) {
		// TODO Auto-generated method stub
		return null;
	}

}
