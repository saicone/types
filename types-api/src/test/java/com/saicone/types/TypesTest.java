package com.saicone.types;

import org.junit.jupiter.api.Test;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Objects;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

public class TypesTest {

    @Test
    public void testParse() {
        assertEquals(1234, Types.parse(int.class, "1234"));
        assertEquals(3f, Types.parse(float.class, '3'));
        assertEquals(1L, Types.parse(long.class, true));
        assertEquals(20.5D, Types.parse(double.class, 20.5D));
    }

    @Test
    public void testParser() {
        // Object
        assertEquals("TEST", Types.OBJECT.parse("TEST"));
        // String
        assertEquals("TEST", Types.STRING.parse("TEST"));
        assertEquals("1234", Types.STRING.parse(1234));
        assertEquals("5", Types.STRING.parse('5'));
        // Text
        assertEquals("[1, 2, 3, 4]", Types.TEXT.parse(new int[] { 1, 2, 3, 4 }));
        assertEquals("[true, false]", Types.TEXT.parse(new Boolean[] { true, false }));
        assertEquals("[one, two, three, four]", Types.TEXT.parse(new String[] { "one", "two", "three", "four" }));
        // Character
        assertEquals('Ӓ', Types.CHAR.parse(1234));
        assertEquals('T', Types.CHAR.parse("TEST"));
        // Boolean
        assertEquals(true, Types.BOOLEAN.parse("true"));
        assertEquals(true, Types.BOOLEAN.parse(1f));
        assertEquals(false, Types.BOOLEAN.parse("no"));
        // Number
        assertEquals(35.6, Types.NUMBER.parse("35.6"));
        assertEquals(10.5f, Types.NUMBER.parse("10.5f"));
        assertEquals(1234, Types.INTEGER.parse("1234"));
        assertEquals(26, Types.INTEGER.parse("0b11010"));
        assertEquals(70, Types.INTEGER.parse("0x46"));
        assertEquals(70, Types.INTEGER.parse("#46"));
        assertEquals(61, Types.INTEGER.parse("075"));
        assertEquals(3f, Types.FLOAT.parse('3'));
        assertEquals(5.7f, Types.FLOAT.parse("5.7f"));
        assertEquals(1L, Types.LONG.parse(true));
        assertEquals(20.5D, Types.DOUBLE.parse(20.5D));
        // Class
        assertEquals(Object.class, Types.CLASS.parse("java.lang.Object"));
        assertEquals(Object.class, Types.CLASS.parse("java/lang/Object.class"));
        assertEquals(Object.class, Types.CLASS.parse("Ljava/lang/Object;"));
        // UUID
        final UUID uniqueId = UUID.fromString("7ca003dc-175f-4f1f-b490-5651045311ad");
        assertEquals(uniqueId, Types.UUID.parse("7ca003dc-175f-4f1f-b490-5651045311ad"));
        assertEquals(uniqueId, Types.UUID.parse("7ca003dc175f4f1fb4905651045311ad"));
        assertEquals(uniqueId, Types.UUID.parse(new int[] { 2090861532, 392122143, -1265609135, 72552877 }));
        assertEquals(uniqueId, Types.UUID.parse(new long[] { 8980181900796579615L, -5435749844271296083L }));
        // URI
        try {
            final URI uri = new URI("Hello World");
            assertEquals(uri, Types.URI.parse("Hello World"));
        } catch (URISyntaxException ignored) { }
        // URL
        try {
            final URL url = new URL("someurl.com");
            assertEquals(url, Types.URL.parse("someurl.com"));
        } catch (MalformedURLException ignored) { }
        // Duration
        final Duration duration = Duration.ofMinutes(30);
        assertEquals(duration, Types.DURATION.parse(duration));
        assertEquals(duration, Types.DURATION.parse("30 MINUTE"));
        assertEquals(duration, Types.DURATION.parse("30 MINUTES"));
        assertEquals(duration, Types.DURATION.parse("1800 SECONDS"));
        assertEquals(duration, Types.DURATION.parse("1800000 MILLISECONDS"));
        // Date
        final LocalDate date = LocalDate.of(2024, 10, 24);
        assertEquals(date, Types.LOCAL_DATE.parse("2024-10-24"));
        assertEquals(date, Types.LOCAL_DATE.parse(new int[] { 2024, 298 }));
        assertEquals(date, Types.LOCAL_DATE.parse(new int[] { 2024, 10, 24 }));
        assertEquals(date, Types.LOCAL_DATE.parse(20020L));
        // Time
        final LocalTime time = LocalTime.of(10, 25, 50);
        assertEquals(time, Types.LOCAL_TIME.parse("10:25:50"));
        assertEquals(time, Types.LOCAL_TIME.parse(37550L));
        assertEquals(time, Types.LOCAL_TIME.parse(new int[] { 10, 25, 50 }));
        // Date time
        final LocalDateTime dateTime = LocalDateTime.of(2024, 10, 24, 10, 25, 50);
        assertEquals(dateTime, Types.LOCAL_DATE_TIME.parse("2024-10-24T10:25:50"));
        assertEquals(dateTime, Types.LOCAL_DATE_TIME.parse(new int[] { 2024, 10, 24, 10, 25, 50 }));
    }

    @Test
    public void testRegister() {
        assertTrue(Types.contains(String.class));
        assertTrue(Types.contains(int.class));
        assertTrue(Types.contains("text"));

        Types.put(MyObject.class, MyObject::new);
        assertTrue(Types.contains(MyObject.class));
        assertEquals(new MyObject("test"), Types.of(MyObject.class).parse("test"));

        Types.remove(MyObject.class);
        assertFalse(Types.contains(MyObject.class));
    }

    static class MyObject {

        private final Object object;

        MyObject(Object object) {
            this.object = object;
        }

        public Object getObject() {
            return object;
        }

        @Override
        public boolean equals(Object object1) {
            if (this == object1) return true;
            if (object1 == null || getClass() != object1.getClass()) return false;

            MyObject myObject = (MyObject) object1;

            return Objects.equals(object, myObject.object);
        }

        @Override
        public int hashCode() {
            return object != null ? object.hashCode() : 0;
        }
    }
}
