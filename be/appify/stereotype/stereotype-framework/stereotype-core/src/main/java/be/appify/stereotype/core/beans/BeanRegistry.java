package be.appify.stereotype.core.beans;

import java.util.Collection;

public interface BeanRegistry {
	<T> BeanModel<T> getBeanModel(Class<T> beanClass);

	Collection<BeanModel<?>> getAllRegisteredBeans();
}
