package com.saicone.types;

import com.saicone.types.util.WrappedList;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TypeWrapperTest {

    @Test
    public void testWrapper() {
        final List<Integer> list = new ArrayList<>();
        list.add(1);
        list.add(2);
        list.add(3);
        list.add(4);

        final List<String> wrapped = new WrappedList<>(list, TypeWrapper.of(Types.INTEGER, Types.STRING));
        assertTrue(wrapped.contains("3"));
        wrapped.add("5");
        assertEquals("5", wrapped.get(4));
    }
}
