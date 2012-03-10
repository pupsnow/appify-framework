package be.appify.util.collections.filterable;

import be.appify.util.specification.Specification;

public interface FilterableListExtension<T> {
    FilterableList<T> filter(Specification<T> specification);
}
