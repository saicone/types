package com.saicone.types.parser;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class FileParserTest {

    private static final File FILE_A = new File(new File(new File("some"), "file"), "path");
    private static final File FILE_B = new File(new File(new File("other"), "path"), "for-file");
    private static final Path PATH_A = Paths.get("some", "file", "path");
    private static final Path PATH_B = Paths.get("other", "path", "for-file");

    @Test
    public void testParseLiteral() {
        assertEquals(FILE_A, FileParser.INSTANCE.parse(FILE_A));
        assertEquals(FILE_B, FileParser.INSTANCE.parse(FILE_B));
    }

    @Test
    public void testParsePath() {
        assertEquals(FILE_A, FileParser.INSTANCE.parse(PATH_A));
        assertEquals(FILE_B, FileParser.INSTANCE.parse(PATH_B));
    }

    @Test
    public void testParseString() {
        assertEquals(FILE_A, FileParser.INSTANCE.parse("some/file/path"));
        assertEquals(FILE_B, FileParser.INSTANCE.parse("other/path/for-file"));
    }

    @Test
    public void testParseStringArray() {
        assertEquals(FILE_A, FileParser.INSTANCE.parse(new String[] { "some", "file", "path" }));
        assertEquals(FILE_B, FileParser.INSTANCE.parse(new String[] { "other", "path", "for-file" }));
    }
}
