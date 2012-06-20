package be.appify.stereotype.core.beans;

import java.util.Collection;

public interface BeanModelRegistry {
	<T extends AbstractBean> BeanModel<T> getBeanModel(Class<T> beanClass);

	Collection<BeanModel<? extends AbstractBean>> getAllRegisteredBeanModels();

	void initialize(Class<?>... classes);
}
