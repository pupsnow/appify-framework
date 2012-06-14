package be.appify.stereotype.core.bootstrapping;

public interface Context {
	<T> T getUniqueBean(Class<T> type);
}
