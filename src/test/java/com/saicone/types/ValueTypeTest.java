package com.saicone.types;

import com.google.common.collect.ImmutableList;
import com.google.common.collect.ImmutableMap;
import com.google.common.collect.ImmutableSet;
import org.junit.jupiter.api.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertArrayEquals;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class ValueTypeTest {

    @Test
    public void testString() {
        assertEquals("test", ValueType.of("test").asString());
        assertEquals("t", ValueType.of('t').asString());
        assertEquals("true", ValueType.of(true).asString());
        assertEquals("1234", ValueType.of(1234).asString());
    }

    @Test
    public void testCharacter() {
        assertEquals('t', ValueType.of("test").asChar());
        assertEquals('a', ValueType.of('a').asChar());
        assertEquals('t', ValueType.of(true).asChar());
        assertEquals('Ӓ', ValueType.of(1234).asChar());
    }

    @Test
    public void testBoolean() {
        assertEquals(true, ValueType.of("true").asBoolean());
        assertEquals(true, ValueType.of('y').asBoolean());
        assertEquals(true, ValueType.of(true).asBoolean());
        assertEquals(true, ValueType.of(1).asBoolean());
    }

    @Test
    public void testNumber() {
        assertEquals(1234, ValueType.of("1234").asInt());
        assertEquals(3f, ValueType.of('3').asFloat());
        assertEquals(1L, ValueType.of(true).asLong());
        assertEquals(20.5D, ValueType.of(20.5D).asDouble());
    }

    @Test
    public void testUniqueId() {
        UUID expected = UUID.fromString("7ca003dc-175f-4f1f-b490-5651045311ad");
        assertEquals(expected, ValueType.of("7ca003dc-175f-4f1f-b490-5651045311ad").asUniqueId());
        assertEquals(expected, ValueType.of("7ca003dc175f4f1fb4905651045311ad").asUniqueId());
        assertEquals(expected, ValueType.of(new int[] { 2090861532, 392122143, -1265609135, 72552877 }).asUniqueId());
        assertEquals(expected, ValueType.of(new long[] { 8980181900796579615L, -5435749844271296083L }).asUniqueId());
    }

    @Test
    public void testList() {
        assertEquals("test", ValueType.of(ImmutableList.of("test")).asString());
        assertEquals("1234", ValueType.of(ImmutableList.of(1234)).asString());
        assertEquals(ImmutableList.of("test"), ValueType.of("test").asList(Types.STRING));
        assertEquals(ImmutableList.of(1234), ValueType.of("1234").asList(Types.INTEGER));
        assertEquals(ImmutableList.of("true", "true", "false"), ValueType.of(ImmutableList.of(true, true, false)).asList(Types.STRING));
        assertEquals(ImmutableList.of(1, 2, 3), ValueType.of(ImmutableList.of("1", "2", "3")).asList(Types.INTEGER));
    }

    @Test
    public void testSet() {
        assertEquals(ImmutableSet.of("true", "false"), ValueType.of(ImmutableList.of(true, true, false)).asSet(Types.STRING));
        assertEquals(ImmutableSet.of(1, 2, 3), ValueType.of(ImmutableList.of("1", "2", "2", "3", "3", "3")).asSet(Types.INTEGER));
    }

    @Test
    public void testEnum() {
        assertEquals(EnumType.SECOND, ValueType.of("SECOND").asEnum(EnumType.class));
        assertEquals(EnumType.SECOND, ValueType.of(1).asEnum(EnumType.class, EnumType.values()));
    }

    @Test
    public void testMap() {
        final Map<Integer, Boolean> expected = ImmutableMap.of(1, true, 4, false);
        final Map<String, String> actual = ImmutableMap.of("1", "true", "4", "false");

        assertEquals(expected, ValueType.of(actual).asMap(Types.INTEGER, Types.BOOLEAN, new HashMap<>()));
    }

    @Test
    public void testArray() {
        final String[] expected = new String[] { "1.5", "1.3", "1.2", "1.1" };
        assertArrayEquals(expected, ValueType.of(ImmutableList.of(1.5, 1.3, 1.2, 1.1)).asArray(Types.STRING));

        final int[] primitve = new int[] { 15, 1234, 14, 1 };
        assertArrayEquals(primitve, ValueType.of(ImmutableList.of(15.2, "1234", 14.8, true)).asArray(Types.of(int.class)));
    }

    enum EnumType {
        FIRST,
        SECOND,
        THIRD;
    }
}
