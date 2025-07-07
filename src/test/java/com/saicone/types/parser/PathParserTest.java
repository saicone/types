package com.saicone.types.parser;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class PathParserTest {

    private static final Path PATH_A = Paths.get("some", "file", "path");
    private static final Path PATH_B = Paths.get("other", "path", "for-file");
    private static final File FILE_A = new File(new File(new File("some"), "file"), "path");
    private static final File FILE_B = new File(new File(new File("other"), "path"), "for-file");

    @Test
    public void testParseLiteral() {
        assertEquals(PATH_A, PathParser.INSTANCE.parse(PATH_A));
        assertEquals(PATH_B, PathParser.INSTANCE.parse(PATH_B));
    }

    @Test
    public void testParseFile() {
        assertEquals(PATH_A, PathParser.INSTANCE.parse(FILE_A));
        assertEquals(PATH_B, PathParser.INSTANCE.parse(FILE_B));
    }

    @Test
    public void testParseString() {
        assertEquals(PATH_A, PathParser.INSTANCE.parse("some/file/path"));
        assertEquals(PATH_B, PathParser.INSTANCE.parse("other/path/for-file"));
    }

    @Test
    public void testParseStringArray() {
        assertEquals(PATH_A, PathParser.INSTANCE.parse(new String[] { "some", "file", "path" }));
        assertEquals(PATH_B, PathParser.INSTANCE.parse(new String[] { "other", "path", "for-file" }));
    }
}
