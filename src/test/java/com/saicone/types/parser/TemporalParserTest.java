package com.saicone.types.parser;

import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TemporalParserTest {

    private static final LocalDate LOCAL_DATE = LocalDate.of(2025, 1, 2);
    private static final LocalTime LOCAL_TIME1 = LocalTime.of(7, 8);
    private static final LocalTime LOCAL_TIME2 = LocalTime.of(7, 8, 9);
    private static final LocalTime LOCAL_TIME3 = LocalTime.of(7, 8, 9, 10);
    private static final LocalDateTime LOCAL_DATE_TIME1 = LocalDateTime.of(2025, 1, 2, 7, 8);
    private static final LocalDateTime LOCAL_DATE_TIME2 = LocalDateTime.of(2025, 1, 2, 7, 8, 9);
    private static final LocalDateTime LOCAL_DATE_TIME3 = LocalDateTime.of(2025, 1, 2, 7, 8, 9, 10);

    @Test
    public void testParseLiteral() {
        assertEquals(LOCAL_DATE, TemporalParser.LOCAL_DATE.parse(LOCAL_DATE));
        assertEquals(LOCAL_TIME1, TemporalParser.LOCAL_TIME.parse(LOCAL_TIME1));
        assertEquals(LOCAL_TIME2, TemporalParser.LOCAL_TIME.parse(LOCAL_TIME2));
        assertEquals(LOCAL_TIME3, TemporalParser.LOCAL_TIME.parse(LOCAL_TIME3));
        assertEquals(LOCAL_DATE_TIME1, TemporalParser.LOCAL_DATE_TIME.parse(LOCAL_DATE_TIME1));
        assertEquals(LOCAL_DATE_TIME2, TemporalParser.LOCAL_DATE_TIME.parse(LOCAL_DATE_TIME2));
        assertEquals(LOCAL_DATE_TIME3, TemporalParser.LOCAL_DATE_TIME.parse(LOCAL_DATE_TIME3));
    }

    @Test
    public void testParseLocalDate() {
        assertEquals(LOCAL_DATE, TemporalParser.LOCAL_DATE.parse(LOCAL_DATE.toEpochDay()));
        assertEquals(LOCAL_DATE, TemporalParser.LOCAL_DATE.parse(new int[] { 2025, 2 }));
        assertEquals(LOCAL_DATE, TemporalParser.LOCAL_DATE.parse(new int[] { 2025, 1, 2 }));
        assertEquals(LOCAL_DATE, TemporalParser.LOCAL_DATE.parse("2025-01-02"));
    }

    @Test
    public void testParseLocalTime() {
        assertEquals(LOCAL_TIME2, TemporalParser.LOCAL_TIME.parse(LOCAL_TIME2.toSecondOfDay()));
        assertEquals(LOCAL_TIME1, TemporalParser.LOCAL_TIME.parse(new int[] { 7, 8 }));
        assertEquals(LOCAL_TIME2, TemporalParser.LOCAL_TIME.parse(new int[] { 7, 8, 9 }));
        assertEquals(LOCAL_TIME3, TemporalParser.LOCAL_TIME.parse(new int[] { 7, 8, 9, 10 }));
        assertEquals(LOCAL_TIME1, TemporalParser.LOCAL_TIME.parse("07:08"));
        assertEquals(LOCAL_TIME2, TemporalParser.LOCAL_TIME.parse("07:08:09"));
        assertEquals(LOCAL_TIME3, TemporalParser.LOCAL_TIME.parse("07:08:09.000000010"));
    }

    @Test
    public void testParseLocalDateTime() {
        assertEquals(LOCAL_DATE_TIME2, TemporalParser.LOCAL_DATE_TIME.parse(1735823289L));
        assertEquals(LOCAL_DATE_TIME1, TemporalParser.LOCAL_DATE_TIME.parse(new int[] { 2025, 1, 2, 7, 8 }));
        assertEquals(LOCAL_DATE_TIME2, TemporalParser.LOCAL_DATE_TIME.parse(new int[] { 2025, 1, 2, 7, 8, 9 }));
        assertEquals(LOCAL_DATE_TIME3, TemporalParser.LOCAL_DATE_TIME.parse(new int[] { 2025, 1, 2, 7, 8, 9, 10 }));
        assertEquals(LOCAL_DATE_TIME1, TemporalParser.LOCAL_DATE_TIME.parse("2025-01-02T07:08"));
        assertEquals(LOCAL_DATE_TIME2, TemporalParser.LOCAL_DATE_TIME.parse("2025-01-02T07:08:09"));
        assertEquals(LOCAL_DATE_TIME3, TemporalParser.LOCAL_DATE_TIME.parse("2025-01-02T07:08:09.000000010"));
    }
}
