package be.appify.stereotype.core.operation;

import java.util.Collection;
import java.util.Map;

import com.google.common.collect.Maps;

public class OperationFactory {
	private final Map<Class<?>, GenericOperation<?>> operations;

	public OperationFactory(Collection<GenericOperation<?>> operations) {
		this.operations = Maps.newHashMap();
		for (GenericOperation<?> operation : operations) {
			this.operations.put(operation.getClass(), operation);
		}
	}

	public <T extends GenericOperation<?>> T getOperation(Class<T> operationClass) {
		GenericOperation<?> operation = operations.get(operationClass);
		return operationClass.cast(operation);
	}
}