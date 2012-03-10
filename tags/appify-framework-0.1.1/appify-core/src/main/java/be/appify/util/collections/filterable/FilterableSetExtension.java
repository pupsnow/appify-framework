package be.appify.util.collections.filterable;

import be.appify.util.specification.Specification;

public interface FilterableSetExtension<T> {
    FilterableSet<T> filter(Specification<T> specification);
}
