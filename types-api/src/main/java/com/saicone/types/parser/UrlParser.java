package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
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

    /**
     * {@link UrlParser} public instance.
     */
    public static final UrlParser INSTANCE = new UrlParser();

    @Override
    public @Nullable Type getType() {
        return URL.class;
    }

    @Override
    public @Nullable URL parse(@NotNull Object object) {
        try {
            if (object instanceof Path) {
                return ((Path) object).toUri().toURL();
            }
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
        final Object first = AnyIterable.of(object).first();
        if (first == null) {
            return null;
        }

        if (first instanceof URL) {
            return (URL) first;
        }

        try {
            if (first instanceof URI) {
                return ((URI) first).toURL();
            } else if (first instanceof File) {
                return ((File) first).toURI().toURL();
            } else if (first instanceof Path) {
                return ((Path) first).toUri().toURL();
            }
            return new URL(String.valueOf(first));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
