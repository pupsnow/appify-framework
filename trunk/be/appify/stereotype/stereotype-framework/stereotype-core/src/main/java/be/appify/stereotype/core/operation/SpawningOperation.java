package be.appify.stereotype.core.operation;

import java.util.Map;

import be.appify.stereotype.core.beans.Bean;

public interface SpawningOperation<B extends Bean> extends GenericOperation<B> {
	B execute(Map<String, Object> namedParameters);

}
