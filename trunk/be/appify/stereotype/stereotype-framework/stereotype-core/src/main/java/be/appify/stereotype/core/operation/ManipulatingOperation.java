package be.appify.stereotype.core.operation;

import java.util.Map;

import be.appify.stereotype.core.beans.AbstractBean;

public interface ManipulatingOperation<B extends AbstractBean> extends GenericOperation<B> {
	void execute(Map<String, Object> namedParameters);

}
