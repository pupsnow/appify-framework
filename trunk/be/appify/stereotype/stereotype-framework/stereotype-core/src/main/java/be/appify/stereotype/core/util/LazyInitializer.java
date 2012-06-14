package be.appify.stereotype.core.util;

public abstract class LazyInitializer<T> {
	private volatile T object;

	public T get() {
		T result = object;

		if (result == null) {
			synchronized (this) {
				result = object;
				if (result == null) {
					object = result = initialize();
				}
			}
		}

		return result;
	}

	protected abstract T initialize();
}