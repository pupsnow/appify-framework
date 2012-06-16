package be.appify.stereotype.core.beans;

import java.util.Collection;
import java.util.Map;

import be.appify.stereotype.core.beans.validation.ValidatorFactory;

import com.google.common.collect.Maps;

public class SimpleBeanModelRegistry implements BeanModelRegistry {

	private Map<Class<?>, BeanModel<?>> models;

	public SimpleBeanModelRegistry(ValidatorFactory validatorFactory, Class<?>... classes) {
		BeanAnalyzer analyzer = new BeanAnalyzer(validatorFactory);
		this.models = Maps.newHashMap();
		for(Class<?> c : classes) {
			BeanModel<?> beanModel = analyzer.analyze(c);
			models.put(c, beanModel);
		}
	}

	@SuppressWarnings("unchecked")
	@Override
	public <T> BeanModel<T> getBeanModel(Class<T> beanClass) {
		BeanModel<?> beanModel = models.get(beanClass);
		if(beanModel == null) {
			for(Class<?> c : models.keySet()) {
				if(c.isAssignableFrom(beanClass)) {
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
