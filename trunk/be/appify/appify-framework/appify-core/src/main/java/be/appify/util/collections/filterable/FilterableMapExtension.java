package be.appify.util.collections.filterable;

import java.util.Map;

import be.appify.util.specification.Specification;

interface FilterableMapExtension<K, V> {
    Map<K, V> filterByKeys(Specification<K> specification);

    Map<K, V> filterByValues(Specification<V> specification);
}
