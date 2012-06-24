package be.appify.stereotype.core.beans;

import java.util.Collection;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Named;

import be.appify.stereotype.core.beans.validation.ValidatorFactory;
import be.appify.stereotype.core.operation.OperationFactory;

import com.google.common.collect.Maps;

@Named
public class SimpleBeanModelRegistry implements BeanModelRegistry {

	private final Map<Class<?>, BeanModel<?>> models;
	private final BeanAnalyzer analyzer;

	@Inject
	public SimpleBeanModelRegistry(ValidatorFactory validatorFactory, OperationFactory operationFactory) {
		this.analyzer = new BeanAnalyzer(validatorFactory, operationFactory);
		this.models = Maps.newHashMap();
	}

	@Override
	public void initialize(Class<?>... classes) {
		for (Class<?> c : classes) {
			@SuppressWarnings("unchecked")
			BeanModel<?> beanModel = analyzer.analyze((Class<? extends Bean>) c);
			models.put(c, beanModel);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T extends Bean> BeanModel<T> getBeanModel(Class<T> beanClass) {
		BeanModel<?> beanModel = models.get(beanClass);
		if (beanModel == null) {
			for (Class<?> c : models.keySet()) {
				if (c.isAssignableFrom(beanClass)) {
					beanModel = models.get(c);
					break;
				}
			}
		}
		return (BeanModel<T>) beanModel;
	}

	@Override
	public Collection<BeanModel<?>> getAllRegisteredBeanModels() {
		return models.values();
	}

}
