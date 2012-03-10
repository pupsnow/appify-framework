package be.appify.util.specification;

import be.appify.util.Extender;

public final class SpecificationExtender {
    private SpecificationExtender() {
    }

    @SuppressWarnings("unchecked")
    public static <T> CompositeSpecification<T> composite(Specification<T> specification) {
        return Extender.extend(CompositeSpecification.class, specification, new CompositeSpecificationExtensionImpl<T>(specification));
    }

    public static <T> CompositeSpecification<T> and(Specification<T> one, Specification<T> other) {
        return composite(new AndSpecification<T>(one, other));
    }

    public static <T> CompositeSpecification<T> or(Specification<T> one, Specification<T> other) {
        return composite(new OrSpecification<T>(one, other));
    }

    public static <T> CompositeSpecification<T> not(Specification<T> specification) {
        return composite(new NotSpecification<T>(specification));
    }
}
