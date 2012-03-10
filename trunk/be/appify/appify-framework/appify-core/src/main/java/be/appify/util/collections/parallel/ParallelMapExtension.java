package be.appify.util.collections.parallel;

import java.util.Collection;

import com.google.common.base.Function;

interface ParallelMapExtension<K, V> {
    /**
     * Applies the function on each key in the map. When multiple cores are
     * available, functions are applied in parallel. Note that because of this,
     * order of applying functions is not guaranteed. This method will wait
     * until the function has been applied to each key.
     * 
     * @param function
     * @return a collection of results of the invocations of the function on
     *         each key
     */
    <R> Collection<R> forEachKey(Function<K, R> function);

    /**
     * Applies the function on each value in the map. When multiple cores are
     * available, functions are applied in parallel. Note that because of this,
     * order of applying functions is not guaranteed. This method will wait
     * until the function has been applied to each value.
     * 
     * @param function
     * @return a collection of results of the invocations of the function on
     *         each value
     */
    <R> Collection<R> forEachValue(Function<V, R> function);
}
