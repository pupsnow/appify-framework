package be.appify.stereotype.core.operation;

import java.util.Map;

import be.appify.stereotype.core.beans.BeanModel;

public interface SpawningOperation<B> {
	B execute(Object... parameters);
	
	B execute(Map<String, Object> namedParameters);
	
	<N> SpawningOperation<N> createNew(BeanModel<N> beanModel);
}
