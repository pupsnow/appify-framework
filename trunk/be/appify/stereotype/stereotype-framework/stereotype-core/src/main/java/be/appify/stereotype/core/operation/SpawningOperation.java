package be.appify.stereotype.core.operation;

import java.util.Map;

public interface SpawningOperation<B> extends GenericOperation<B> {
	B execute(Map<String, Object> namedParameters);

}
