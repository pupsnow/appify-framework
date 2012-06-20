package be.appify.stereotype.core.session;

import be.appify.stereotype.core.beans.AbstractBean;
import be.appify.stereotype.core.operation.GenericOperation;
import be.appify.stereotype.core.operation.ManipulatingOperation;
import be.appify.stereotype.core.operation.SpawningOperation;

public interface Session {

	<B extends AbstractBean, O extends GenericOperation<?>> SpawningOperation<B> newSpawningOperation(
			Class<B> beanClass,
			Class<O> operationClass);

	<B extends AbstractBean, O extends GenericOperation<?>> ManipulatingOperation<B> newManipulatingOperation(
			Class<B> beanClass,
			Class<O> operationClass);

}