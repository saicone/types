package com.saicone.types;

import org.junit.jupiter.api.Test;

import java.util.Objects;

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
        assertEquals(35.6, Types.NUMBER.parse("35.6"));
        assertEquals(1234, Types.INTEGER.parse("1234"));
        assertEquals(26, Types.INTEGER.parse("0b11010"));
        assertEquals(70, Types.INTEGER.parse("0x46"));
        assertEquals(70, Types.INTEGER.parse("#46"));
        assertEquals(5, Types.INTEGER.parse("075"));
        assertEquals(3f, Types.FLOAT.parse('3'));
        assertEquals(5.7f, Types.FLOAT.parse("5.7f"));
        assertEquals(1L, Types.LONG.parse(true));
        assertEquals(20.5D, Types.DOUBLE.parse(20.5D));
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
