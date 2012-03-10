package be.appify.util.collections.filterable;

import java.util.HashSet;
import java.util.Set;

import be.appify.util.collections.CollectionExtender;
import be.appify.util.specification.Specification;

public class FilterableSetExtensionImpl<T> extends FilterableCollectionExtensionImpl<T, Set<T>> implements FilterableSetExtension<T> {

    public FilterableSetExtensionImpl(Set<T> collection) {
        super(collection);
    }

    @Override
    protected Set<T> createCollection() {
        return new HashSet<T>();
    }

    @Override
    public FilterableSet<T> filter(Specification<T> specification) {
        return CollectionExtender.filterable(filterInternal(specification));
    }
}
