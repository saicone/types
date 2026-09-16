package com.saicone.types.parser;

import com.google.common.collect.ImmutableList;
import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class BooleanParserTest {

    private static final List<String> VALID_DECLARATION = ImmutableList.of(
            "true",
            "t",
            "yes",
            "on",
            "y",
            "false",
            "f",
            "no",
            "off",
            "n"
    );

    @Test
    public void testValidString() {
        for (String s : VALID_DECLARATION) {
            assertTrue(BooleanParser.isValid(s));
            assertTrue(BooleanParser.isValid(" " + s + " "));
            assertFalse(BooleanParser.isValid(s + "-"));
        }
    }

    @Test
    public void testParseLiteral() {
        assertTrue(BooleanParser.INSTANCE.parse(true));
        assertFalse(BooleanParser.INSTANCE.parse(false));
    }

    private static final List<String> TRUE_STRING = ImmutableList.of(
            "true",
            "t",
            "yes",
            "on",
            "y"
    );

    private static final List<String> FALSE_STRING = ImmutableList.of(
            "false",
            "f",
            "no",
            "off",
            "n"
    );

    @Test
    public void testParseString() {
        for (String s : TRUE_STRING) {
            assertEquals(Boolean.TRUE, BooleanParser.INSTANCE.parse(s));
            assertEquals(Boolean.TRUE, BooleanParser.INSTANCE.parse(" " + s + " "));
            assertNull(BooleanParser.INSTANCE.parse(s + "-"));
        }
        for (String s : FALSE_STRING) {
            assertEquals(Boolean.FALSE, BooleanParser.INSTANCE.parse(s));
            assertEquals(Boolean.FALSE, BooleanParser.INSTANCE.parse(" " + s + " "));
            assertNull(BooleanParser.INSTANCE.parse(s + "-"));
        }
    }

    private static final List<Number> TRUE_INTEGER = ImmutableList.of(
            (byte) 1,
            (short) 1,
            (int) 1,
            (long) 1,
            BigInteger.ONE
    );

    private static final List<Number> FALSE_INTEGER = ImmutableList.of(
            (byte) 0,
            (short) 0,
            (int) 0,
            (long) 0,
            BigInteger.ZERO
    );

    private static final List<Number> INVALID_INTEGER = ImmutableList.of(
            (byte) 2,
            (short) 4,
            (int) 8,
            (long) 16L,
            BigInteger.valueOf(30L)
    );

    @Test
    public void testParseInteger() {
        for (Number number : TRUE_INTEGER) {
            assertEquals(Boolean.TRUE, BooleanParser.INSTANCE.parse(number));
        }

        for (Number number : FALSE_INTEGER) {
            assertEquals(Boolean.FALSE, BooleanParser.INSTANCE.parse(number));
        }

        for (Number number : INVALID_INTEGER) {
            assertNull(BooleanParser.INSTANCE.parse(number));
        }
    }

    private static final List<Number> TRUE_DECIMAL = ImmutableList.of(
            (float) 1f,
            (double) 1d,
            BigDecimal.ONE,
            (float) 0.8f,
            (double) 0.16d,
            BigDecimal.valueOf(0.32d)
    );

    private static final List<Number> FALSE_DECIMAL = ImmutableList.of(
            (float) 0f,
            (double) 0d,
            BigDecimal.ZERO
    );

    private static final List<Number> INVALID_DECIMAL = ImmutableList.of(
            (float) 8f,
            (double) 16d,
            BigDecimal.TEN
    );

    @Test
    public void testParseDecimal() {
        for (Number number : TRUE_DECIMAL) {
            assertEquals(Boolean.TRUE, BooleanParser.INSTANCE.parse(number));
        }

        for (Number number : FALSE_DECIMAL) {
            assertEquals(Boolean.FALSE, BooleanParser.INSTANCE.parse(number));
        }

        for (Number number : INVALID_DECIMAL) {
            assertNull(BooleanParser.INSTANCE.parse(number));
        }
    }
}
