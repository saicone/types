package com.saicone.types.parser;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UriParserTest {

    private static final File FILE = new File(new File(new File(new File("some"), "kind"), "of"), "path");
    private static final Path PATH = Paths.get("some", "kind", "of", "path");
    private static final URI URI_HTTPS;
    private static final URI URI_FILE;
    private static final URI URI_PATH;

    static {
        try {
            URI_HTTPS = new URI("https://some/kind/of/path");
            URI_FILE = FILE.toURI();
            URI_PATH = PATH.toUri();
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseLiteral() {
        assertEquals(URI_HTTPS, UriParser.INSTANCE.parse(URI_HTTPS));
    }

    @Test
    public void testParseString() {
        assertEquals(URI_HTTPS, UriParser.INSTANCE.parse("https://some/kind/of/path"));
    }

    @Test
    public void testParseFile() {
        assertEquals(URI_FILE, UriParser.INSTANCE.parse(FILE));
    }

    @Test
    public void testParsePath() {
        assertEquals(URI_PATH, UriParser.INSTANCE.parse(PATH));
    }
}
