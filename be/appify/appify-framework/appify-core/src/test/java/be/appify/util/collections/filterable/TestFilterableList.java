package be.appify.util.collections.filterable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;

import be.appify.util.collections.CollectionExtender;
import be.appify.util.collections.filterable.FilterableList;
import be.appify.util.specification.Specification;

public class TestFilterableList {
    @Test
    public void filterShouldReturnOnlyMatchingInstances() {
        List<String> fruits = Arrays.asList("apple", "pear", "banana", "lemon", "orange", "strawberry");
        FilterableList<String> filterableList = CollectionExtender.filterable(fruits);

        Specification<String> containsAnO = new Specification<String>() {
            @Override
            public boolean isSatisfiedBy(String object) {
                return object.contains("o");
            }
        };

        List<String> filtered = filterableList.filter(containsAnO);
        assertNotNull(filtered);
        assertEquals(2, filtered.size());
        assertTrue(filtered.contains("lemon"));
        assertTrue(filtered.contains("orange"));
    }
}
