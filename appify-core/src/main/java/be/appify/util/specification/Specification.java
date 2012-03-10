package be.appify.util.specification;

public interface Specification<T> {
    boolean isSatisfiedBy(T object);
}
