package be.appify.stereotype.core.operation;

import be.appify.stereotype.core.beans.AbstractBean;
import be.appify.stereotype.core.beans.BeanModel;

public interface GenericOperation<B extends AbstractBean> {
	<N extends AbstractBean> GenericOperation<N> createNew(BeanModel<N> beanModel);
}
