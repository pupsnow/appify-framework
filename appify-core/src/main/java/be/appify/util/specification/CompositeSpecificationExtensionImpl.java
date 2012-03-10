package be.appify.util.specification;

final class CompositeSpecificationExtensionImpl<T> implements CompositeSpecificationExtension<T> {

    private final Specification<T> specification;

    public CompositeSpecificationExtensionImpl(Specification<T> specification) {
        this.specification = specification;
    }

    @Override
    public CompositeSpecification<T> and(Specification<T> other) {
        return SpecificationExtender.composite(new AndSpecification<T>(specification, other));
    }

    @Override
    public CompositeSpecification<T> or(Specification<T> other) {
        return SpecificationExtender.composite(new OrSpecification<T>(specification, other));
    }

    @Override
    public CompositeSpecification<T> not() {
        return SpecificationExtender.composite(new NotSpecification<T>(specification));
    }

}
