package com.saicone.types.parser;

import org.junit.jupiter.api.Test;

import java.math.BigDecimal;
import java.math.BigInteger;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class NumberParserTest {

    @Test
    public void testParseLiteral() {
        assertEquals((byte) 50, NumberParser.BYTE.parse((byte) 50));
        assertEquals((short) 50, NumberParser.SHORT.parse((short) 50));
        assertEquals((int) 50, NumberParser.INTEGER.parse((int) 50));
        assertEquals((long) 50L, NumberParser.LONG.parse((long) 50L));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse(BigInteger.valueOf(50L)));

        assertEquals((float) 0.05f, NumberParser.FLOAT.parse((float) 0.05f));
        assertEquals((double) 0.05d, NumberParser.DOUBLE.parse((double) 0.05d));
        assertEquals(BigDecimal.valueOf(0.05d), NumberParser.BIG_DECIMAL.parse(BigDecimal.valueOf(0.05d)));
    }

    @Test
    public void testParseInteger() {
        assertEquals((byte) 50, NumberParser.BYTE.parse((byte) 50));
        assertEquals((byte) 50, NumberParser.BYTE.parse((short) 50));
        assertEquals((byte) 50, NumberParser.BYTE.parse((int) 50));
        assertEquals((byte) 50, NumberParser.BYTE.parse((long) 50L));
        assertEquals((byte) 50, NumberParser.BYTE.parse(BigInteger.valueOf(50L)));
        assertEquals((byte) 50, NumberParser.BYTE.parse((float) 50f));
        assertEquals((byte) 50, NumberParser.BYTE.parse((double) 50d));
        assertEquals((byte) 50, NumberParser.BYTE.parse(BigDecimal.valueOf(50d)));

        assertEquals((short) 50, NumberParser.SHORT.parse((byte) 50));
        assertEquals((short) 50, NumberParser.SHORT.parse((short) 50));
        assertEquals((short) 50, NumberParser.SHORT.parse((int) 50));
        assertEquals((short) 50, NumberParser.SHORT.parse((long) 50L));
        assertEquals((short) 50, NumberParser.SHORT.parse(BigInteger.valueOf(50L)));
        assertEquals((short) 50, NumberParser.SHORT.parse((float) 50f));
        assertEquals((short) 50, NumberParser.SHORT.parse((double) 50d));
        assertEquals((short) 50, NumberParser.SHORT.parse(BigDecimal.valueOf(50d)));

        assertEquals((int) 50, NumberParser.INTEGER.parse((byte) 50));
        assertEquals((int) 50, NumberParser.INTEGER.parse((short) 50));
        assertEquals((int) 50, NumberParser.INTEGER.parse((int) 50));
        assertEquals((int) 50, NumberParser.INTEGER.parse((long) 50L));
        assertEquals((int) 50, NumberParser.INTEGER.parse(BigInteger.valueOf(50L)));
        assertEquals((int) 50, NumberParser.INTEGER.parse((float) 50f));
        assertEquals((int) 50, NumberParser.INTEGER.parse((double) 50d));
        assertEquals((int) 50, NumberParser.INTEGER.parse(BigDecimal.valueOf(50d)));

        assertEquals((long) 50L, NumberParser.LONG.parse((byte) 50));
        assertEquals((long) 50L, NumberParser.LONG.parse((short) 50));
        assertEquals((long) 50L, NumberParser.LONG.parse((int) 50));
        assertEquals((long) 50L, NumberParser.LONG.parse((long) 50L));
        assertEquals((long) 50L, NumberParser.LONG.parse(BigInteger.valueOf(50L)));
        assertEquals((long) 50L, NumberParser.LONG.parse((float) 50f));
        assertEquals((long) 50L, NumberParser.LONG.parse((double) 50d));
        assertEquals((long) 50L, NumberParser.LONG.parse(BigDecimal.valueOf(50d)));

        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse((byte) 50));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse((short) 50));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse((int) 50));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse((long) 50L));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse(BigInteger.valueOf(50L)));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse((float) 50f));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse((double) 50d));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse(BigDecimal.valueOf(50d)));
    }

    final Float fl = new BigDecimal("0.05").floatValue();

    @Test
    public void testParseDecimal() {
        assertEquals((float) 50f, NumberParser.FLOAT.parse((byte) 50));
        assertEquals((float) 50f, NumberParser.FLOAT.parse((short) 50));
        assertEquals((float) 50f, NumberParser.FLOAT.parse((int) 50));
        assertEquals((float) 50f, NumberParser.FLOAT.parse((long) 50L));
        assertEquals((float) 50f, NumberParser.FLOAT.parse(BigInteger.valueOf(50L)));
        assertEquals((float) 0.05f, NumberParser.FLOAT.parse((float) 0.05f));
        assertEquals((float) 0.05f, NumberParser.FLOAT.parse((double) 0.05d));
        assertEquals((float) 0.05f, NumberParser.FLOAT.parse(BigDecimal.valueOf(0.05d)));

        assertEquals((double) 50d, NumberParser.DOUBLE.parse((byte) 50));
        assertEquals((double) 50d, NumberParser.DOUBLE.parse((short) 50));
        assertEquals((double) 50d, NumberParser.DOUBLE.parse((int) 50));
        assertEquals((double) 50d, NumberParser.DOUBLE.parse((long) 50L));
        assertEquals((double) 50d, NumberParser.DOUBLE.parse(BigInteger.valueOf(50L)));
        assertEquals((double) 0.05d, NumberParser.DOUBLE.parse(fl));
        assertEquals((double) 0.05d, NumberParser.DOUBLE.parse((double) 0.05d));
        assertEquals((double) 0.05d, NumberParser.DOUBLE.parse(BigDecimal.valueOf(0.05d)));

        // BigDecimal with scale of 0
        assertEquals(new BigDecimal("50"), NumberParser.BIG_DECIMAL.parse((byte) 50));
        assertEquals(new BigDecimal("50"), NumberParser.BIG_DECIMAL.parse((short) 50));
        assertEquals(new BigDecimal("50"), NumberParser.BIG_DECIMAL.parse((int) 50));
        assertEquals(new BigDecimal("50"), NumberParser.BIG_DECIMAL.parse((long) 50L));
        assertEquals(new BigDecimal("50"), NumberParser.BIG_DECIMAL.parse(BigInteger.valueOf(50L)));
        // BigDecimal with scale of 2
        assertEquals(BigDecimal.valueOf(0.05d), NumberParser.BIG_DECIMAL.parse((float) 0.05f));
        assertEquals(BigDecimal.valueOf(0.05d), NumberParser.BIG_DECIMAL.parse((double) 0.05d));
        assertEquals(BigDecimal.valueOf(0.05d), NumberParser.BIG_DECIMAL.parse(BigDecimal.valueOf(0.05d)));
    }

    @Test
    public void testParseBoolean() {
        assertEquals((byte) 0, NumberParser.BYTE.parse(false));
        assertEquals((short) 0, NumberParser.SHORT.parse(false));
        assertEquals((int) 0, NumberParser.INTEGER.parse(false));
        assertEquals((long) 0L, NumberParser.LONG.parse(false));
        assertEquals(BigInteger.valueOf(0L), NumberParser.BIG_INTEGER.parse(false));
        assertEquals((float) 0f, NumberParser.FLOAT.parse(false));
        assertEquals((double) 0d, NumberParser.DOUBLE.parse(false));
        assertEquals(new BigDecimal("0"), NumberParser.BIG_DECIMAL.parse(false)); // BigDecimal with scale of 0

        assertEquals((byte) 1, NumberParser.BYTE.parse(true));
        assertEquals((short) 1, NumberParser.SHORT.parse(true));
        assertEquals((int) 1, NumberParser.INTEGER.parse(true));
        assertEquals((long) 1L, NumberParser.LONG.parse(true));
        assertEquals(BigInteger.valueOf(1L), NumberParser.BIG_INTEGER.parse(true));
        assertEquals((float) 1f, NumberParser.FLOAT.parse(true));
        assertEquals((double) 1d, NumberParser.DOUBLE.parse(true));
        assertEquals(new BigDecimal("1"), NumberParser.BIG_DECIMAL.parse(true)); // BigDecimal with scale of 0
    }

    @Test
    public void testParseEnum() {
        for (EnumA value : EnumA.values()) {
            assertEquals((byte) value.ordinal(), NumberParser.BYTE.parse(value));
            assertEquals((short) value.ordinal(), NumberParser.SHORT.parse(value));
            assertEquals((int) value.ordinal(), NumberParser.INTEGER.parse(value));
            assertEquals((long) value.ordinal(), NumberParser.LONG.parse(value));
            assertEquals(BigInteger.valueOf(value.ordinal()), NumberParser.BIG_INTEGER.parse(value));
            assertEquals((float) value.ordinal(), NumberParser.FLOAT.parse(value));
            assertEquals((double) value.ordinal(), NumberParser.DOUBLE.parse(value));
            assertEquals(BigDecimal.valueOf(value.ordinal()), NumberParser.BIG_DECIMAL.parse(value));
        }
    }

    @Test
    public void testParseStringAsInteger() {
        assertEquals((byte) 50, NumberParser.BYTE.parse("50"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("50u"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("50U"));
        assertEquals((byte) -50, NumberParser.BYTE.parse("-50"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("0b110010"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("0B110010"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("0x32"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("0X32"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("#32"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("0o62"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("0O62"));
        assertEquals((byte) 50, NumberParser.BYTE.parse("062"));

        assertEquals((short) 50, NumberParser.SHORT.parse("50"));
        assertEquals((short) 50, NumberParser.SHORT.parse("50u"));
        assertEquals((short) 50, NumberParser.SHORT.parse("50U"));
        assertEquals((short) -50, NumberParser.SHORT.parse("-50"));
        assertEquals((short) 50, NumberParser.SHORT.parse("0b110010"));
        assertEquals((short) 50, NumberParser.SHORT.parse("0B110010"));
        assertEquals((short) 50, NumberParser.SHORT.parse("0x32"));
        assertEquals((short) 50, NumberParser.SHORT.parse("0X32"));
        assertEquals((short) 50, NumberParser.SHORT.parse("#32"));
        assertEquals((short) 50, NumberParser.SHORT.parse("0o62"));
        assertEquals((short) 50, NumberParser.SHORT.parse("0O62"));
        assertEquals((short) 50, NumberParser.SHORT.parse("062"));

        assertEquals((int) 50, NumberParser.INTEGER.parse("50"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("50u"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("50U"));
        assertEquals((int) -50, NumberParser.INTEGER.parse("-50"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("0b110010"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("0B110010"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("0x32"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("0X32"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("#32"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("0o62"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("0O62"));
        assertEquals((int) 50, NumberParser.INTEGER.parse("062"));

        assertEquals((long) 50L, NumberParser.LONG.parse("50"));
        assertEquals((long) 50L, NumberParser.LONG.parse("50u"));
        assertEquals((long) 50L, NumberParser.LONG.parse("50U"));
        assertEquals((long) -50L, NumberParser.LONG.parse("-50"));
        assertEquals((long) 50L, NumberParser.LONG.parse("0b110010"));
        assertEquals((long) 50L, NumberParser.LONG.parse("0B110010"));
        assertEquals((long) 50L, NumberParser.LONG.parse("0x32"));
        assertEquals((long) 50L, NumberParser.LONG.parse("0X32"));
        assertEquals((long) 50L, NumberParser.LONG.parse("#32"));
        assertEquals((long) 50L, NumberParser.LONG.parse("0o62"));
        assertEquals((long) 50L, NumberParser.LONG.parse("0O62"));
        assertEquals((long) 50L, NumberParser.LONG.parse("062"));

        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("50"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("50u"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("50U"));
        assertEquals(BigInteger.valueOf(-50L), NumberParser.BIG_INTEGER.parse("-50"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("0b110010"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("0B110010"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("0x32"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("0X32"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("#32"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("0o62"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("0O62"));
        assertEquals(BigInteger.valueOf(50L), NumberParser.BIG_INTEGER.parse("062"));
    }

    @Test
    public void testParseStringAsDecimal() {
        assertEquals((float) 0.05f, NumberParser.FLOAT.parse("0.05"));
        assertEquals((float) -0.05f, NumberParser.FLOAT.parse("-0.05"));

        assertEquals((double) 0.05d, NumberParser.DOUBLE.parse("0.05"));
        assertEquals((double) -0.05d, NumberParser.DOUBLE.parse("-0.05"));

        assertEquals(BigDecimal.valueOf(0.05d), NumberParser.BIG_DECIMAL.parse("0.05"));
        assertEquals(BigDecimal.valueOf(-0.05d), NumberParser.BIG_DECIMAL.parse("-0.05"));
    }

    @Test
    public void testParseStringAsNumber() {
        assertEquals((byte) 50, NumberParser.NUMBER.parse("50b"));
        assertEquals((byte) 50, NumberParser.NUMBER.parse("50B"));
        assertEquals((byte) -50, NumberParser.NUMBER.parse("-50b"));
        assertEquals((byte) -50, NumberParser.NUMBER.parse("-50B"));

        assertEquals((short) 50, NumberParser.NUMBER.parse("50s"));
        assertEquals((short) 50, NumberParser.NUMBER.parse("50S"));
        assertEquals((short) -50, NumberParser.NUMBER.parse("-50s"));
        assertEquals((short) -50, NumberParser.NUMBER.parse("-50S"));

        assertEquals((int) 50, NumberParser.NUMBER.parse("50i"));
        assertEquals((int) 50, NumberParser.NUMBER.parse("50I"));
        assertEquals((int) -50, NumberParser.NUMBER.parse("-50i"));
        assertEquals((int) -50, NumberParser.NUMBER.parse("-50I"));

        assertEquals((long) 50L, NumberParser.NUMBER.parse("50l"));
        assertEquals((long) 50L, NumberParser.NUMBER.parse("50L"));
        assertEquals((long) -50L, NumberParser.NUMBER.parse("-50l"));
        assertEquals((long) -50L, NumberParser.NUMBER.parse("-50L"));
        assertEquals((long) 50L, NumberParser.NUMBER.parse("50"));
        assertEquals((long) -50L, NumberParser.NUMBER.parse("-50"));

        assertEquals((float) 0.05f, NumberParser.NUMBER.parse("0.05f"));
        assertEquals((float) 0.05f, NumberParser.NUMBER.parse("0.05F"));
        assertEquals((float) -0.05f, NumberParser.NUMBER.parse("-0.05f"));
        assertEquals((float) -0.05f, NumberParser.NUMBER.parse("-0.05F"));

        assertEquals((double) 0.05d, NumberParser.NUMBER.parse("0.05d"));
        assertEquals((double) 0.05d, NumberParser.NUMBER.parse("0.05D"));
        assertEquals((double) -0.05d, NumberParser.NUMBER.parse("-0.05d"));
        assertEquals((double) -0.05d, NumberParser.NUMBER.parse("-0.05D"));
        assertEquals((double) 0.05d, NumberParser.NUMBER.parse("0.05"));
        assertEquals((double) -0.05d, NumberParser.NUMBER.parse("-0.05"));
    }

    public enum EnumA {
        ONE,
        TWO,
        THREE,
        FOUR,
        FIVE,
        SIX,
        SEVEN;
    }
}
