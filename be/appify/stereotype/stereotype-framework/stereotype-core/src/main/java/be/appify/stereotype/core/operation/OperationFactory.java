package be.appify.stereotype.core.operation;

import java.util.Map;

import be.appify.stereotype.core.beans.BeanModel;
import be.appify.stereotype.core.beans.BeanModelRegistry;

import com.google.common.collect.Maps;

public class OperationFactory {
	private BeanModelRegistry beanModelRegistry;
	private Map<Class<?>, SpawningOperation<?>> spawingOperations;
	
	public OperationFactory(BeanModelRegistry beanModelRegistry) {
		this.beanModelRegistry = beanModelRegistry;
		this.spawingOperations = Maps.newHashMap();
		spawingOperations.put(SpawningOperation.class, CreateOperation.prototype());
		spawingOperations.put(FindByIDOperation.class, FindByIDOperation.prototype());
	}

	public <B, O extends SpawningOperation<B>> SpawningOperation<B> createSpawningOperation(
			Class<B> beanClass, Class<O> operationClass) {
		BeanModel<B> beanModel = beanModelRegistry.getBeanModel(beanClass);
		SpawningOperation<?> prototype = spawingOperations.get(operationClass);
		return prototype.createNew(beanModel);
	}
}
