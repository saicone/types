package com.saicone.types.parser;

import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EnumParserTest {

    private static final EnumParser<EnumA> ENUM_A_PARSER = EnumParser.of(EnumA.class);
    private static final EnumParser<EnumB> ENUM_B_PARSER = EnumParser.of(EnumB.class, EnumB.values());

    @Test
    public void testParseLiteral() {
        assertEquals(EnumA.ONE, ENUM_A_PARSER.parse(EnumA.ONE));
        assertEquals(EnumA.TWO, ENUM_A_PARSER.parse(EnumA.TWO));
        assertEquals(EnumA.THREE, ENUM_A_PARSER.parse(EnumA.THREE));
        assertEquals(EnumA.FOUR, ENUM_A_PARSER.parse(EnumA.FOUR));

        assertEquals(EnumB.FIRST, ENUM_B_PARSER.parse(EnumB.FIRST));
        assertEquals(EnumB.SECOND, ENUM_B_PARSER.parse(EnumB.SECOND));
        assertEquals(EnumB.THIRD, ENUM_B_PARSER.parse(EnumB.THIRD));
        assertEquals(EnumB.FOURTH, ENUM_B_PARSER.parse(EnumB.FOURTH));
    }

    @Test
    public void testParseName() {
        assertEquals(EnumA.ONE, ENUM_A_PARSER.parse("ONE"));
        assertEquals(EnumA.TWO, ENUM_A_PARSER.parse("TWO"));
        assertEquals(EnumA.THREE, ENUM_A_PARSER.parse("THREE"));
        assertEquals(EnumA.FOUR, ENUM_A_PARSER.parse("FOUR"));

        assertEquals(EnumB.FIRST, ENUM_B_PARSER.parse("FIRST"));
        assertEquals(EnumB.SECOND, ENUM_B_PARSER.parse("SECOND"));
        assertEquals(EnumB.THIRD, ENUM_B_PARSER.parse("THIRD"));
        assertEquals(EnumB.FOURTH, ENUM_B_PARSER.parse("FOURTH"));
    }

    @Test
    public void testParseOrdinal() {
        assertEquals(EnumA.ONE, ENUM_A_PARSER.parse(EnumA.ONE.ordinal()));
        assertEquals(EnumA.TWO, ENUM_A_PARSER.parse(EnumA.TWO.ordinal()));
        assertEquals(EnumA.THREE, ENUM_A_PARSER.parse(EnumA.THREE.ordinal()));
        assertEquals(EnumA.FOUR, ENUM_A_PARSER.parse(EnumA.FOUR.ordinal()));

        assertEquals(EnumB.FIRST, ENUM_B_PARSER.parse(EnumB.FIRST.ordinal()));
        assertEquals(EnumB.SECOND, ENUM_B_PARSER.parse(EnumB.SECOND.ordinal()));
        assertEquals(EnumB.THIRD, ENUM_B_PARSER.parse(EnumB.THIRD.ordinal()));
        assertEquals(EnumB.FOURTH, ENUM_B_PARSER.parse(EnumB.FOURTH.ordinal()));
    }

    @Test
    public void testParseEnum() {
        assertEquals(EnumA.ONE, ENUM_A_PARSER.parse(EnumB.FIRST));
        assertEquals(EnumA.TWO, ENUM_A_PARSER.parse(EnumB.SECOND));
        assertEquals(EnumA.THREE, ENUM_A_PARSER.parse(EnumB.THIRD));
        assertEquals(EnumA.FOUR, ENUM_A_PARSER.parse(EnumB.FOURTH));

        assertEquals(EnumB.FIRST, ENUM_B_PARSER.parse(EnumA.ONE));
        assertEquals(EnumB.SECOND, ENUM_B_PARSER.parse(EnumA.TWO));
        assertEquals(EnumB.THIRD, ENUM_B_PARSER.parse(EnumA.THREE));
        assertEquals(EnumB.FOURTH, ENUM_B_PARSER.parse(EnumA.FOUR));
    }

    public enum EnumA {
        ONE,
        TWO,
        THREE,
        FOUR;
    }

    public enum EnumB {
        FIRST,
        SECOND,
        THIRD,
        FOURTH;
    }
}
