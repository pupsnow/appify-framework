package be.appify.stereotype.core.beans;

import java.util.Collection;

public interface BeanModelRegistry {
	<T> BeanModel<T> getBeanModel(Class<T> beanClass);

	Collection<BeanModel<?>> getAllRegisteredBeanModels();
}
