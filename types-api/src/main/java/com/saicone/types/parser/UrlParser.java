package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URL;
import java.nio.file.Path;
import java.util.Objects;

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
    public @NotNull URL parse(@NotNull Object object) throws Throwable {
        if (object instanceof Path) {
            return ((Path) object).toUri().toURL();
        }

        final Object first = AnyIterable.first(object);
        Objects.requireNonNull(first, "Cannot get effective first element");

        if (first instanceof URL) {
            return (URL) first;
        }

        if (first instanceof URI) {
            return ((URI) first).toURL();
        } else if (first instanceof File) {
            return ((File) first).toURI().toURL();
        } else if (first instanceof Path) {
            return ((Path) first).toUri().toURL();
        }
        return new URL(String.valueOf(first));
    }
}
