package be.appify.util.specification;

final class AndSpecification<T> implements Specification<T> {

    private final Specification<T> one;
    private final Specification<T> other;

    public AndSpecification(Specification<T> one, Specification<T> other) {
        this.one = one;
        this.other = other;
    }

    @Override
    public boolean isSatisfiedBy(T object) {
        return one.isSatisfiedBy(object) && other.isSatisfiedBy(object);
    }

}
