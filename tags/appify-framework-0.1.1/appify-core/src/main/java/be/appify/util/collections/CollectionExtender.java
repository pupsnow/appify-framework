package be.appify.util.collections;

import java.util.List;
import java.util.Map;
import java.util.Set;

import be.appify.util.Extender;
import be.appify.util.collections.filterable.FilterableList;
import be.appify.util.collections.filterable.FilterableListExtensionImpl;
import be.appify.util.collections.filterable.FilterableMap;
import be.appify.util.collections.filterable.FilterableMapExtensionImpl;
import be.appify.util.collections.filterable.FilterableSet;
import be.appify.util.collections.filterable.FilterableSetExtensionImpl;
import be.appify.util.collections.parallel.ParallelCollectionExtensionImpl;
import be.appify.util.collections.parallel.ParallelList;
import be.appify.util.collections.parallel.ParallelMap;
import be.appify.util.collections.parallel.ParallelMapExtensionImpl;
import be.appify.util.collections.parallel.ParallelSet;

public final class CollectionExtender {
    private CollectionExtender() {
    }

    @SuppressWarnings("unchecked")
    public static <T> ParallelList<T> parallel(List<T> list) {
        return Extender.extend(ParallelList.class, list, new ParallelCollectionExtensionImpl<T>(list));
    }

    @SuppressWarnings("unchecked")
    public static <T> ParallelSet<T> parallel(Set<T> set) {
        return Extender.extend(ParallelSet.class, set, new ParallelCollectionExtensionImpl<T>(set));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> ParallelMap<K, V> parallel(Map<K, V> map) {
        return Extender.extend(ParallelMap.class, map, new ParallelMapExtensionImpl<K, V>(map));
    }

    @SuppressWarnings("unchecked")
    public static <T> FilterableList<T> filterable(List<T> list) {
        return Extender.extend(FilterableList.class, list, new FilterableListExtensionImpl<T>(list));
    }

    @SuppressWarnings("unchecked")
    public static <T> FilterableSet<T> filterable(Set<T> set) {
        return Extender.extend(FilterableSet.class, set, new FilterableSetExtensionImpl<T>(set));
    }

    @SuppressWarnings("unchecked")
    public static <K, V> FilterableMap<K, V> filterable(Map<K, V> map) {
        return Extender.extend(FilterableMap.class, map, new FilterableMapExtensionImpl<K, V>(map));
    }
}
