package be.appify.util.collections.parallel;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import com.google.common.base.Function;

public final class ParallelMapExtensionImpl<K, V> implements ParallelMapExtension<K, V> {
    private final Map<K, V> map;

    public ParallelMapExtensionImpl(Map<K, V> map) {
        this.map = new HashMap<K, V>(map);
    }

    @Override
    public <R> Collection<R> forEachKey(Function<K, R> function) {
        return new ParallelCollectionExtensionImpl<K>(map.keySet()).forEach(function);
    }

    @Override
    public <R> Collection<R> forEachValue(Function<V, R> function) {
        return new ParallelCollectionExtensionImpl<V>(map.values()).forEach(function);
    }
}
