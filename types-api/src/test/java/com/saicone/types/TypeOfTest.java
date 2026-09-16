package com.saicone.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeOfTest {

    private static final TypeOf<String> STRING_TYPE = new TypeOf<String>(){};
    private static final TypeOf<Map<Integer, Boolean>> MAP_TYPE = new TypeOf<Map<Integer, Boolean>>(){};
    private static final TypeOf<List<Double>> LIST_TYPE = new TypeOf<List<Double>>(){};
    private static final TypeOf<String[]> OBJECT_ARRAY_TYPE = new TypeOf<String[]>(){};
    private static final TypeOf<int[]> PRIMITIVE_ARRAY_TYPE = new TypeOf<int[]>(){};
    private static final TypeOf<EnumType> ENUM_TYPE = new TypeOf<EnumType>() {};

    @Test
    public void testObject() {
        assertEquals("test", STRING_TYPE.parse("test"));
        assertEquals("1234", STRING_TYPE.parse(1234));
        assertEquals("true", STRING_TYPE.parse(true));

        assertEquals("test", new TypeOf<String>(){}.parse("test"));
        assertEquals(1234, new TypeOf<Integer>(){}.parse("1234"));
        assertEquals(true, new TypeOf<Boolean>(){}.parse("true"));
    }

    @Test
    public void testEnum() {
        assertEquals(EnumType.SECOND, ENUM_TYPE.parse("SECOND"));

        assertEquals(EnumType.SECOND, ENUM_TYPE.parse(1));
        assertEquals(EnumType.FIRST, ENUM_TYPE.parse("0"));
    }

    @Test
    public void testMap() {
        final Map<Integer, Boolean> expected = ImmutableMap.of(1, true, 4, false);
        final Map<String, String> actual = ImmutableMap.of("1", "true", "4", "false");

        assertEquals(expected, MAP_TYPE.parse(actual));
        assertEquals(actual, new TypeOf<Map<String, String>>(){}.parse(expected));
    }

    @Test
    public void testList() {
        final List<Double> expected = ImmutableList.of(1.5, 1.3, 1.2, 1.1);
        final List<String> actual = ImmutableList.of("1.5", "1.3", "1.2", "1.1");

        assertEquals(expected, LIST_TYPE.parse(actual));
        assertEquals(actual, new TypeOf<List<String>>(){}.parse(expected));
    }

    @Test
    public void testObjectArray() {
        final String[] expected = new String[] { "1.5", "1.3", "1.2", "1.1" };

        assertArrayEquals(expected, OBJECT_ARRAY_TYPE.parse(ImmutableList.of(1.5, 1.3, 1.2, 1.1)));
        assertArrayEquals(new Double[] { 1.5, 1.3, 1.2, 1.1 }, new TypeOf<Double[]>(){}.parse(expected));
    }

    @Test
    public void testPrimitiveArray() {
        final int[] expected = new int[] { 15, 1234, 14, 1 };

        assertArrayEquals(expected, PRIMITIVE_ARRAY_TYPE.parse(ImmutableList.of(15.2, "1234", 14.8, true)));
        assertArrayEquals(new boolean[] { false, false, false, true }, new TypeOf<boolean[]>(){}.parse(expected));
    }

    enum EnumType {
        FIRST,
        SECOND,
        THIRD;
    }
}
