package be.appify.stereotype.core.operation;

import be.appify.stereotype.core.beans.BeanModel;

public interface GenericOperation<B> {
	<N> GenericOperation<N> createNew(BeanModel<N> beanModel);
}
