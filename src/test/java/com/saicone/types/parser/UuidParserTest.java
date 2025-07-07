package com.saicone.types.parser;

import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UuidParserTest {

    private static final UUID ID = UUID.fromString("7ca003dc-175f-4f1f-b490-5651045311ad");

    @Test
    public void testParseLiteral() {
        assertEquals(ID, UuidParser.INSTANCE.parse(ID));
    }

    @Test
    public void testParseString() {
        assertEquals(ID, UuidParser.INSTANCE.parse("7ca003dc-175f-4f1f-b490-5651045311ad"));
        assertEquals(ID, UuidParser.INSTANCE.parse("7ca003dc175f4f1fb4905651045311ad"));
    }

    @Test
    public void testParseIntArray() {
        assertEquals(ID, UuidParser.INSTANCE.parse(new int[] { 2090861532, 392122143, -1265609135, 72552877 }));
    }

    @Test
    public void testParseLongArray() {
        assertEquals(ID, UuidParser.INSTANCE.parse(new long[] { 8980181900796579615L, -5435749844271296083L }));
    }
}
