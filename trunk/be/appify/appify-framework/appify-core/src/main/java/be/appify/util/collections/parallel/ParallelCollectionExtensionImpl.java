package be.appify.util.collections.parallel;

import java.util.ArrayList;
import java.util.Collection;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutionException;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;


import be.appify.util.collections.ExceptionInParallelExecution;

import com.google.common.base.Function;

public final class ParallelCollectionExtensionImpl<T> implements ParallelCollectionExtension<T> {

    private final Collection<T> collection;
    private static final int NUMBER_OF_PROCESSORS = Runtime.getRuntime().availableProcessors();

    public ParallelCollectionExtensionImpl(Collection<T> collection) {
        this.collection = new ArrayList<T>(collection);
    }

    @Override
    public <R> Collection<R> forEach(final Function<T, R> function) {
        ExecutorService executor = Executors.newFixedThreadPool(NUMBER_OF_PROCESSORS);
        Collection<Future<R>> futures = new ArrayList<Future<R>>();
        for (final T item : collection) {
            Callable<R> task = new Callable<R>() {
                @Override
                public R call() throws Exception {
                    return function.apply(item);
                }
            };
            futures.add(executor.submit(task));
        }
        Collection<R> results = new ArrayList<R>();
        for (Future<R> future : futures) {
            try {
                results.add(future.get());
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt();
            } catch (ExecutionException e) {
                throw new ExceptionInParallelExecution(e.getMessage(), e);
            }
        }
        return results;
    }
}
