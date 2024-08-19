package com.saicone.types.parser;

import com.saicone.types.IterableType;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;

/**
 * Represents a parser that try to convert any object type to URL.<br>
 * This parser is compatible with File, Path, URI and String object types.
 *
 * @author Rubenicos
 */
public class UrlParser implements TypeParser<URL> {

    @Override
    public @Nullable Type getType() {
        return URL.class;
    }

    @Override
    public @Nullable URL parse(@NotNull Object object) {
        final Object single = IterableType.of(object).single();
        if (single == null) {
            return null;
        }

        if (single instanceof URL) {
            return (URL) single;
        }

        try {
            if (single instanceof URI) {
                return ((URI) single).toURL();
            } else if (single instanceof File) {
                return ((File) single).toURI().toURL();
            } else if (single instanceof Path) {
                return ((Path) single).toUri().toURL();
            }
            return new URL(String.valueOf(single));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
