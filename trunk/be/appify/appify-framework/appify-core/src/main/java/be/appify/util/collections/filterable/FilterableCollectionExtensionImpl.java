package be.appify.util.collections.filterable;

import java.util.Collection;

import be.appify.util.specification.Specification;

abstract class FilterableCollectionExtensionImpl<T, C extends Collection<T>> {

    private final C collection;

    public FilterableCollectionExtensionImpl(C collection) {
        this.collection = collection;
    }

    protected final C filterInternal(Specification<T> specification) {
        C filtered = createCollection();
        for (T item : collection) {
            if (specification.isSatisfiedBy(item)) {
                filtered.add(item);
            }
        }
        return filtered;
    }

    protected abstract C createCollection();

}
