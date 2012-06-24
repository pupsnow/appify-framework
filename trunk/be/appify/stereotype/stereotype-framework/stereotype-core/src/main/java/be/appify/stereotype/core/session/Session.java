package be.appify.stereotype.core.session;

import be.appify.stereotype.core.beans.Bean;
import be.appify.stereotype.core.operation.GenericOperation;
import be.appify.stereotype.core.operation.ListingOperation;
import be.appify.stereotype.core.operation.ManipulatingOperation;
import be.appify.stereotype.core.operation.SpawningOperation;

public interface Session {

	<B extends Bean, O extends GenericOperation<?>> SpawningOperation<B> newSpawningOperation(
			Class<B> beanClass, Class<O> operationClass);

	<B extends Bean, O extends GenericOperation<?>> ManipulatingOperation<B> newManipulatingOperation(
			Class<B> beanClass, Class<O> operationClass);

	<B extends Bean, O extends GenericOperation<?>> ListingOperation<B> newListingOperation(
			Class<B> beanClass, Class<O> operationClass);

}