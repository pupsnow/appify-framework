package be.appify.stereotype.core.beans;

import java.util.Collection;

public interface BeanModelRegistry {
	<T extends Bean> BeanModel<T> getBeanModel(Class<T> beanClass);

	Collection<BeanModel<? extends Bean>> getAllRegisteredBeanModels();

	void initialize(Class<?>... classes);
}
