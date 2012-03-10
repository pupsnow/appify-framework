package be.appify.util.specification;

interface CompositeSpecificationExtension<T> {
    CompositeSpecification<T> and(Specification<T> other);

    CompositeSpecification<T> or(Specification<T> other);

    CompositeSpecification<T> not();
}
