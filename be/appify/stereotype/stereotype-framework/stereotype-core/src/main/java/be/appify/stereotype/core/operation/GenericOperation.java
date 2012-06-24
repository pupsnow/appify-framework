package be.appify.stereotype.core.operation;

import be.appify.stereotype.core.beans.Bean;
import be.appify.stereotype.core.beans.BeanModel;

public interface GenericOperation<B extends Bean> {
	<N extends Bean> GenericOperation<N> createNew(BeanModel<N> beanModel);
}
