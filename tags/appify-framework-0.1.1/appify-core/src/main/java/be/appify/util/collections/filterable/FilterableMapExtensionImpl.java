package be.appify.util.collections.filterable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;


import be.appify.util.LazyInitializer;
import be.appify.util.specification.Specification;

import com.google.common.collect.BiMap;
import com.google.common.collect.HashBiMap;

public final class FilterableMapExtensionImpl<K, V> implements FilterableMapExtension<K, V> {

    private final Map<K, V> map;
    private final LazyInitializer<Map<V, K>> inverted;

    public FilterableMapExtensionImpl(final Map<K, V> map) {
        this.map = map;

        inverted = new LazyInitializer<Map<V, K>>() {
            @Override
            protected Map<V, K> initialize() {
                BiMap<K, V> biMap = HashBiMap.create();
                biMap.putAll(map);
                return biMap.inverse();
            }
        };
    }

    @Override
    public Map<K, V> filterByKeys(Specification<K> specification) {
        Set<K> filteredKeys = new FilterableSetExtensionImpl<K>(map.keySet()).filter(specification);
        Map<K, V> filteredMap = new HashMap<K, V>();
        for (K key : filteredKeys) {
            filteredMap.put(key, map.get(key));
        }
        return filteredMap;
    }

    @Override
    public Map<K, V> filterByValues(Specification<V> specification) {
        List<V> filteredValues = new FilterableListExtensionImpl<V>(new ArrayList<V>(map.values())).filter(specification);
        Map<K, V> filteredMap = new HashMap<K, V>();
        for (V value : filteredValues) {
            filteredMap.put(inverted.get().get(value), value);
        }
        return filteredMap;
    }
}
