package be.appify.stereotype.core.operation;

import java.util.Map;

import be.appify.stereotype.core.beans.Bean;

public interface ManipulatingOperation<B extends Bean> extends GenericOperation<B> {
	void execute(Map<String, Object> namedParameters);

}
