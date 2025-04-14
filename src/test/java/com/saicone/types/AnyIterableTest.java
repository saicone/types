package com.saicone.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.*;

public class AnyIterableTest {

    @Test
    public void testSingle() {
        final String expected = "test";
        // Read-only
        int count = 0;
        for (String s : AnyIterable.<String>ofAny(expected)) {
            assertEquals("test", s);
            count++;
        }
        assertEquals(1, count);
        count = 0;

        // Replaceable
        final ReplaceableValue<String> replaceable = new ReplaceableValue<>("test");
        assertEquals(expected, replaceable.getValue());
        final Iterator<String> iterator = replaceable.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            iterator.remove();
            count++;
        }
        assertEquals(1, count);
        assertNull(replaceable.getValue());
    }

    @Test
    public void testObjectArray() {
        final String[] expected = new String[] {"0", "1", "2", "3"};
        // Read-only
        int count = 0;
        for (String s : AnyIterable.<String>ofAny(expected)) {
            assertEquals(String.valueOf(count), s);
            count++;
        }
        assertEquals(4, count);
        count = 0;

        // Replaceable
        final ReplaceableValue<String> replaceable = new ReplaceableValue<>(new String[] {"0", "1", "2", "3", "4", "5"});
        assertNotEquals(expected, replaceable.getValue());
        final Iterator<String> iterator = replaceable.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            if (count > 3) {
                iterator.remove();
            }
            count++;
        }
        assertEquals(6, count);
        assertArrayEquals(expected, (String[]) replaceable.getValue());
    }

    @Test
    public void testPrimitiveArray() {
        final int[] expected = new int[] {0, 1, 2, 3};
        // Read-only
        int count = 0;
        for (int i : AnyIterable.<Integer>ofAny(expected)) {
            assertEquals(count, i);
            count++;
        }
        assertEquals(4, count);
        count = 0;

        // Replaceable
        final ReplaceableValue<Integer> replaceable = new ReplaceableValue<>(new int[] {0, 1, 2, 3, 4, 5});
        assertNotEquals(expected, replaceable.getValue());
        final Iterator<Integer> iterator = replaceable.iterator();
        while (iterator.hasNext()) {
            iterator.next();
            if (count > 3) {
                iterator.remove();
            }
            count++;
        }
        assertEquals(6, count);
        assertArrayEquals(expected, (int[]) replaceable.getValue());
    }

    @Test
    public void testIterable() {
        final List<String> expected = ImmutableList.of("0", "1", "2", "3");
        final List<String> actual = new ArrayList<>(ImmutableList.of("0", "1", "2", "3", "4", "5"));
        int count = 0;
        final Iterator<String> iterator = AnyIterable.of(actual).iterator();
        while (iterator.hasNext()) {
            final String s = iterator.next();
            assertEquals(String.valueOf(count), s);
            if (count > 3) {
                iterator.remove();
            }
            count++;
        }
        assertEquals(6, count);
        assertEquals(expected, actual);
    }

    @Test
    public void testMap() {
        final Map<String, Integer> expected = ImmutableMap.of(
                "0", 0,
                "1", 1,
                "2", 2,
                "3", 3);
        final Map<String, Integer> actual = new HashMap<>(ImmutableMap.of(
                "0", 0,
                "1", 1,
                "2", 2,
                "3", 3,
                "4", 4,
                "5", 5));
        int count = 0;
        final Iterator<Map.Entry<String, Integer>> iterator = AnyIterable.of(actual).iterator();
        while (iterator.hasNext()) {
            final Map.Entry<String, Integer> entry = iterator.next();
            assertEquals(String.valueOf(count), entry.getKey());
            assertEquals(count, entry.getValue());
            if (count > 3) {
                iterator.remove();
            }
            count++;
        }
        assertEquals(6, count);
        assertEquals(expected, actual);
    }

    static class ReplaceableValue<T> implements AnyIterable<T> {

        private Object value;

        ReplaceableValue(Object value) {
            this.value = value;
        }

        @Override
        public Object getValue() {
            return value;
        }

        @Override
        public Object setValue(Object value) {
            this.value = value;
            return value;
        }
    }
}
