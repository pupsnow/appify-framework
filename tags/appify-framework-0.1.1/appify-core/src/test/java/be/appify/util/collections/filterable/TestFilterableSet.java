package be.appify.util.collections.filterable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.HashSet;
import java.util.Set;

import org.junit.Test;

import be.appify.util.collections.CollectionExtender;
import be.appify.util.collections.filterable.FilterableSet;
import be.appify.util.specification.Specification;

public class TestFilterableSet {
    @Test
    public void filterShouldReturnOnlyMatchingInstances() {
        Set<String> fruits = new HashSet<String>(Arrays.asList("apple", "pear", "banana", "lemon", "orange", "strawberry"));
        FilterableSet<String> filterableSet = CollectionExtender.filterable(fruits);

        Specification<String> containsAnO = new Specification<String>() {
            @Override
            public boolean isSatisfiedBy(String object) {
                return object.contains("o");
            }
        };

        Set<String> filtered = filterableSet.filter(containsAnO);
        assertNotNull(filtered);
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains("lemon"));
        assertTrue(filtered.contains("orange"));
    }
}
