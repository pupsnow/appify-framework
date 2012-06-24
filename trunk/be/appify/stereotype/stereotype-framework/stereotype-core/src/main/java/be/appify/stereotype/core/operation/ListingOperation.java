package be.appify.stereotype.core.operation;

import java.util.List;
import java.util.Map;

import be.appify.stereotype.core.beans.Bean;

public interface ListingOperation<B extends Bean> extends GenericOperation<B> {
	List<B> execute(Map<String, Object> namedParameters);

}
