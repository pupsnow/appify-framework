package be.appify.stereotype.core.operation;

import java.util.Map;

public interface ManipulatingOperation<B> extends GenericOperation<B> {
	void execute(Map<String, Object> namedParameters);

}
