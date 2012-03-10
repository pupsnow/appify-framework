package be.appify.util.collections.filterable;

import java.util.ArrayList;
import java.util.List;

import be.appify.util.collections.CollectionExtender;
import be.appify.util.specification.Specification;

public final class FilterableListExtensionImpl<T> extends FilterableCollectionExtensionImpl<T, List<T>> implements FilterableListExtension<T> {

    public FilterableListExtensionImpl(List<T> collection) {
        super(collection);
    }

    @Override
    protected List<T> createCollection() {
        return new ArrayList<T>();
    }

    @Override
    public FilterableList<T> filter(Specification<T> specification) {
        return CollectionExtender.filterable(filterInternal(specification));
    }
}
