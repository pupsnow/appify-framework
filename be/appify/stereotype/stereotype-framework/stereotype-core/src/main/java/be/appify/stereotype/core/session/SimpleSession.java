package be.appify.stereotype.core.session;

import be.appify.stereotype.core.beans.Bean;
import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.beans.BeanModelRegistry;
import be.appify.stereotype.core.operation.GenericOperation;
import be.appify.stereotype.core.operation.ListingOperation;
import be.appify.stereotype.core.operation.ManipulatingOperation;
import be.appify.stereotype.core.operation.SpawningOperation;

public class SimpleSession implements Session {

	private final BeanModelRegistry beanModelRegistry;

	public SimpleSession(BeanModelRegistry beanModelRegistry) {
		this.beanModelRegistry = beanModelRegistry;
	}

	@Override
	public <B extends Bean, O extends GenericOperation<?>> SpawningOperation<B> newSpawningOperation(
			Class<B> beanClass, Class<O> operationClass) {
		BeanModel<B> model = beanModelRegistry.getBeanModel(beanClass);
		return (SpawningOperation<B>) model.newOperation(operationClass);
	}

	@Override
	public <B extends Bean, O extends GenericOperation<?>> ManipulatingOperation<B> newManipulatingOperation(
			Class<B> beanClass,
			Class<O> operationClass) {
		BeanModel<B> model = beanModelRegistry.getBeanModel(beanClass);
		return (ManipulatingOperation<B>) model.newOperation(operationClass);
	}

	@Override
	public <B extends Bean, O extends GenericOperation<?>> ListingOperation<B> newListingOperation(
			Class<B> beanClass, Class<O> operationClass) {
		BeanModel<B> model = beanModelRegistry.getBeanModel(beanClass);
		return (ListingOperation<B>) model.newOperation(operationClass);
	}

}
