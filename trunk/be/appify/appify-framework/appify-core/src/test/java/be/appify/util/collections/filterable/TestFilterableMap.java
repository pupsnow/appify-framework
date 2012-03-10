package be.appify.util.collections.filterable;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;

import java.util.HashMap;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;

import be.appify.util.collections.CollectionExtender;
import be.appify.util.collections.filterable.FilterableMap;
import be.appify.util.specification.Specification;

public class TestFilterableMap {

    private FilterableMap<String, Integer> filterableMap;

    @Before
    public void before() {
        Map<String, Integer> fruits = new HashMap<String, Integer>();
        fruits.put("apple", 10);
        fruits.put("pear", 15);
        fruits.put("banana", 20);
        fruits.put("lemon", 25);
        fruits.put("orange", 30);
        fruits.put("strawberry", 35);
        filterableMap = CollectionExtender.filterable(fruits);
    }

    @Test
    public void filterByKeysShouldReturnOnlyMatchingInstances() {
        Specification<String> containsAnO = new Specification<String>() {
            @Override
            public boolean isSatisfiedBy(String object) {
                return object.contains("o");
            }
        };

        Map<String, Integer> filtered = filterableMap.filterByKeys(containsAnO);
        assertNotNull(filtered);
        assertEquals(2, filtered.size());
        assertTrue(filtered.containsKey("lemon"));
        assertTrue(filtered.containsKey("orange"));
    }

    @Test
    public void filterByValuesShouldReturnOnlyMatchingInstances() {
        Specification<Integer> between10And20 = new Specification<Integer>() {
            @Override
            public boolean isSatisfiedBy(Integer object) {
                return object >= 10 && object <= 20;
            }
        };

        Map<String, Integer> filtered = filterableMap.filterByValues(between10And20);
        assertNotNull(filtered);
        assertEquals(3, filtered.size());
        assertTrue(filtered.containsKey("apple"));
        assertTrue(filtered.containsKey("pear"));
        assertTrue(filtered.containsKey("banana"));
    }
}
