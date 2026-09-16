package com.saicone.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.saicone.types.parser.EnumParser;
import com.saicone.types.parser.MapParser;
import com.saicone.types.parser.NumberParser;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class TypeParserTest {

    @Test
    public void testObject() {
        final TypeParser<String> parser = String::valueOf;
        assertEquals("1234", parser.parse(1234));
    }

    @Test
    public void testFirst() {
        final TypeParser<String> parser = TypeParser.first(String::valueOf);
        assertEquals("1234", parser.parse(1234));
        assertEquals("test", parser.parse(ImmutableList.of("test", "1234")));
        assertEquals("1234", parser.parse(new int[] { 1234, 55, 4 }));
    }

    @Test
    public void testNumber() {
        final TypeParser<Integer> parser = NumberParser.of(Integer.class, 'i', Integer.MIN_VALUE, Integer.MAX_VALUE, Number::intValue, Integer::parseInt);
        assertEquals(1234, parser.parse("1234"));
        assertEquals(15, parser.parse(ImmutableList.of(15.2, 14.42)));
        assertEquals(1234, parser.parse(new double[] { 1234, 55, 4 }));
    }

    @Test
    public void testCollection() {
        final TypeParser<List<Integer>> parser = Types.INTEGER.collection(List.class, capacity -> new ArrayList<>(capacity));
        final List<Integer> expected = ImmutableList.of(1234, 55, 4, 20);
        assertEquals(expected, parser.parse(ImmutableList.of("1234", "55", "4", "20")));
        assertEquals(expected, parser.parse(new double[] { 1234.1, 55.2, 4.5, 20.22 }));
    }

    @Test
    public void testEnum() {
        TypeParser<EnumType> parser = EnumParser.of(EnumType.class);
        assertEquals(EnumType.SECOND, parser.parse("SECOND"));

        parser = EnumParser.of(EnumType.class, EnumType.values());
        assertEquals(EnumType.SECOND, parser.parse(1));
        assertEquals(EnumType.FIRST, parser.parse("0"));
    }

    @Test
    public void testMap() {
        final TypeParser<Map<Integer, Boolean>> parser = MapParser.of(Types.INTEGER, Types.BOOLEAN);
        final Map<Integer, Boolean> expected = ImmutableMap.of(1, true, 4, false);
        final Map<String, String> actual = ImmutableMap.of("1", "true", "4", "false");

        assertEquals(expected, parser.parse(actual));
    }

    @Test
    public void testArray() {
        final String[] expected = new String[] { "1.5", "1.3", "1.2", "1.1" };
        assertArrayEquals(expected, Types.STRING.<String[]>array().parse(ImmutableList.of(1.5, 1.3, 1.2, 1.1)));

        final int[] primitve = new int[] { 15, 1234, 14, 1 };
        assertArrayEquals(primitve, Types.of(int.class).<int[]>array().parse(ImmutableList.of(15.2, "1234", 14.8, true)));
    }

    @Test
    public void testChaining() {
        TypeParser<Boolean> parser = Types.STRING.andThen(Types.BOOLEAN);
        assertEquals(true, parser.parse(1));
        assertEquals(false, parser.parse(0));

        parser = Types.STRING.andThen(Boolean.class, str -> str.equals("1"));
        assertEquals(true, parser.parse(1));
        assertEquals(false, parser.parse(0));
    }

    enum EnumType {
        FIRST,
        SECOND,
        THIRD;
    }
}
