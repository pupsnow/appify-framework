package be.appify.stereotype.core.operation;

import java.util.Map;

import be.appify.stereotype.core.beans.AbstractBean;

public interface SpawningOperation<B extends AbstractBean> extends GenericOperation<B> {
	B execute(Map<String, Object> namedParameters);

}
