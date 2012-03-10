package be.appify.util.collections.parallel;

import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import org.junit.Test;

import be.appify.util.collections.CollectionExtender;
import be.appify.util.collections.parallel.ParallelList;

import com.google.common.base.Function;

public class TestParallelList {

    @Test
    public void forEachShouldExecuteFunctionOnEachElement() {
        List<Long> bases = Arrays.asList(14L, 20L, 18L, 9L, 12L);
        ParallelList<Long> parallel = CollectionExtender.parallel(bases);

        Function<Long, Long> factorial = new Function<Long, Long>() {
            @Override
            public Long apply(Long input) {
                if (input > 1) {
                    return input * apply(input - 1);
                }
                try {
                    Thread.sleep(10);
                } catch (InterruptedException e) {
                    Thread.currentThread().interrupt();
                }
                return input;
            }
        };

        Collection<Long> factorials = parallel.forEach(factorial);

        assertTrue(factorials.contains(87178291200L));
        assertTrue(factorials.contains(2432902008176640000L));
        assertTrue(factorials.contains(6402373705728000L));
        assertTrue(factorials.contains(362880L));
        assertTrue(factorials.contains(479001600L));
    }
}
