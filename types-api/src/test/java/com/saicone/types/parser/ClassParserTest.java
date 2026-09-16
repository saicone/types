package com.saicone.types.parser;

import com.google.common.collect.ImmutableMap;
import org.junit.jupiter.api.Test;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class ClassParserTest {

    private static final Map<Class<?>, Class<?>> LITERAL = ImmutableMap.of(
            byte.class, byte.class,
            byte[].class, byte[].class,
            byte[][].class, byte[][].class,
            String.class, String.class,
            String[].class, String[].class,
            String[][].class, String[][].class
    );

    @Test
    public void testParseLiteral() {
        for (Map.Entry<Class<?>, Class<?>> entry : LITERAL.entrySet()) {
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse(entry.getValue()));
        }
    }

    private static final Map<Class<?>, String> QUALIFIED = ImmutableMap.of(
            byte.class, byte.class.getName(),
            byte[].class, byte[].class.getName(),
            byte[][].class, byte[][].class.getName(),
            String.class, String.class.getName(),
            String[].class, String[].class.getName(),
            String[][].class, String[][].class.getName()
    );

    @Test
    public void testParseQualifiedName() {
        for (Map.Entry<Class<?>, String> entry : QUALIFIED.entrySet()) {
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse(entry.getValue()));
        }
    }

    private static final Map<Class<?>, String> FILE_PATH1 = ImmutableMap.of(
            byte.class, "byte.class",
            String.class, "java/lang/String.class",
            Integer.class, "java/lang/Integer.class",
            Map.class, "java/util/Map.class"
    );
    private static final Map<Class<?>, String> FILE_PATH2 = ImmutableMap.of(
            byte.class, "byte.java",
            String.class, "java/lang/String.java",
            Integer.class, "java/lang/Integer.java",
            Map.class, "java/util/Map.java"
    );

    @Test
    public void testParseFilePath() {
        for (Map.Entry<Class<?>, String> entry : FILE_PATH1.entrySet()) {
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse(entry.getValue()));
            // with separator prefix
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse("/" + entry.getValue()));
            // windows path
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse("C:\\" + entry.getValue().replace('/', '\\')));
        }
        for (Map.Entry<Class<?>, String> entry : FILE_PATH2.entrySet()) {
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse(entry.getValue()));
            // with separator prefix
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse("/" + entry.getValue()));
            // windows path
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse("C:\\" + entry.getValue().replace('/', '\\')));
        }
    }

    private static final Map<Class<?>, String> DESCRIPTOR = ImmutableMap.of(
            String.class, "Ljava/lang/String;",
            Map.class, "Ljava/util/Map;"
    );

    @Test
    public void testParseDescriptor() {
        for (Map.Entry<Class<?>, String> entry : DESCRIPTOR.entrySet()) {
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse(entry.getValue()));
        }
    }

    private static final Map<Class<?>, String> READABLE = ImmutableMap.of(
            byte.class, "byte",
            byte[].class, "byte[]",
            byte[][].class, "byte[][]",
            String.class, "java.lang.String",
            String[].class, "java.lang.String[]",
            String[][].class, "java.lang.String[][]"
    );

    @Test
    public void testParseReadable() {
        for (Map.Entry<Class<?>, String> entry : READABLE.entrySet()) {
            assertEquals(entry.getKey(), ClassParser.INSTANCE.parse(entry.getValue()));
        }
    }
}
