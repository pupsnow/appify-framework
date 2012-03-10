package be.appify.util.specification;

final class NotSpecification<T> implements Specification<T> {

    private final Specification<T> delegate;

    public NotSpecification(Specification<T> delegate) {
        this.delegate = delegate;
    }

    @Override
    public boolean isSatisfiedBy(T object) {
        return !delegate.isSatisfiedBy(object);
    }

}
