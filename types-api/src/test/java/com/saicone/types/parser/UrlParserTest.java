package com.saicone.types.parser;

import org.junit.jupiter.api.Test;

import java.io.File;
import java.net.MalformedURLException;
import java.net.URL;
import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class UrlParserTest {

    private static final File FILE = new File(new File(new File(new File("some"), "kind"), "of"), "path");
    private static final Path PATH = Paths.get("some", "kind", "of", "path");
    private static final URL URL_HTTPS;
    private static final URL URL_FILE;
    private static final URL URL_PATH;

    static {
        try {
            URL_HTTPS = new URL("https://some/kind/of/path");
            URL_FILE = FILE.toURI().toURL();
            URL_PATH = PATH.toUri().toURL();
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void testParseLiteral() {
        assertEquals(URL_HTTPS, UrlParser.INSTANCE.parse(URL_HTTPS));
    }

    @Test
    public void testParseString() {
        assertEquals(URL_HTTPS, UrlParser.INSTANCE.parse("https://some/kind/of/path"));
    }

    @Test
    public void testParseFile() {
        assertEquals(URL_FILE, UrlParser.INSTANCE.parse(FILE));
    }

    @Test
    public void testParsePath() {
        assertEquals(URL_PATH, UrlParser.INSTANCE.parse(PATH));
    }
}
