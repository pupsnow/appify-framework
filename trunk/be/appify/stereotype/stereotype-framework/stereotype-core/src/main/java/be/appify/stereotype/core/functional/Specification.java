package be.appify.stereotype.core.functional;

public interface Specification<T> {
	boolean isSatisfiedBy(T object);
}
