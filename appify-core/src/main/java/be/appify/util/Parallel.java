package be.appify.util;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;


import be.appify.util.collections.CollectionExtender;
import be.appify.util.collections.parallel.ParallelList;

import com.google.common.base.Function;

public final class Parallel {
    private Parallel() {
    }

    public static <T, R> Collection<R> executeInParallel(final T parameter, Function<T, R>... functions) {
        List<Function<T, R>> list = new ArrayList<Function<T, R>>(Arrays.asList(functions));
        ParallelList<Function<T, R>> parallelList = CollectionExtender.parallel(list);
        return parallelList.forEach(new Function<Function<T, R>, R>() {

            @Override
            public R apply(Function<T, R> input) {
                return input.apply(parameter);
            }
        });
    }

    public static <T, R> Collection<R> executeInParallel(Function<T, R>... functions) {
        return executeInParallel(null, functions);
    }
}
