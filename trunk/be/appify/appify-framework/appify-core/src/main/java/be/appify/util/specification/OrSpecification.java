package be.appify.util.specification;

final class OrSpecification<T> implements Specification<T> {
    private final Specification<T> one;
    private final Specification<T> other;

    public OrSpecification(Specification<T> one, Specification<T> other) {
        this.one = one;
        this.other = other;
    }

    @Override
    public boolean isSatisfiedBy(T object) {
        return one.isSatisfiedBy(object) || other.isSatisfiedBy(object);
    }

}
