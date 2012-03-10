package be.appify.util.collections.parallel;

import java.util.Collection;

import com.google.common.base.Function;

interface ParallelCollectionExtension<T> {
    /**
     * <p>
     * Applies the function on each element in the collection. When multiple
     * cores are available, functions are applied in parallel. This method will
     * wait until the function has been applied to each element.
     * </p>
     * 
     * <p>
     * Note that because of parallel execution, the order in which functions are
     * applied is not guaranteed.
     * </p>
     * 
     * <p>
     * Also note that there is a small performance hit for synchronization.
     * </p>
     * 
     * @param function
     * @return a collection of results of the invocations of the function on
     *         each item
     */
    <R> Collection<R> forEach(Function<T, R> function);
}
