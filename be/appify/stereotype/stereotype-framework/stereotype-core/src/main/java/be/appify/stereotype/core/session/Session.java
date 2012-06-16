package be.appify.stereotype.core.session;

import java.util.UUID;

import be.appify.stereotype.core.operation.SpawningOperation;

public interface Session {

	<B, O extends SpawningOperation<?>> SpawningOperation<B> newOperation(Class<B> beanClass, Class<O> operationClass);

	<B> UUID getID(B bean);

}