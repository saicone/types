package com.saicone.types.parser;

import org.junit.jupiter.api.Test;

import java.util.regex.Pattern;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PatternParserTest {

    private static final Pattern PATTERN = Pattern.compile("test|asd|123");

    @Test
    public void testParseLiteral() {
        assertEquals(PATTERN, PatternParser.INSTANCE.parse(PATTERN));
    }

    @Test
    public void testParseString() {
        assertEquals("test|asd|123", PatternParser.INSTANCE.parse("test|asd|123").toString());
    }
}
