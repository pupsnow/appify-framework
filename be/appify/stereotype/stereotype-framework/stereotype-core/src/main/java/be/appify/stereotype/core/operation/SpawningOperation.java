package be.appify.stereotype.core.operation;

import java.util.Map;

public interface SpawningOperation<B> extends GenericOperation<B> {
	B execute(Object... parameters);

	B execute(Map<String, Object> namedParameters);

}
