package be.appify.stereotype.core.session;

import java.util.UUID;

import be.appify.stereotype.core.operation.GenericOperation;
import be.appify.stereotype.core.operation.ManipulatingOperation;
import be.appify.stereotype.core.operation.SpawningOperation;

public interface Session {

	<B, O extends GenericOperation<?>> SpawningOperation<B> newSpawningOperation(Class<B> beanClass,
			Class<O> operationClass);

	<B, O extends GenericOperation<?>> ManipulatingOperation<B> newManipulatingOperation(Class<B> beanClass,
			Class<O> operationClass);

	<B> UUID getID(B bean);

}