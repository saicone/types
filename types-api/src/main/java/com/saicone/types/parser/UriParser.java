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
 * Represents a parser that try to convert any object type to URI.<br>
 * This parser is compatible with File, Path, URL and String object types.
 *
 * @author Rubenicos
 */
public class UriParser implements TypeParser<URI> {

    /**
     * {@link UriParser} public instance.
     */
    public static final UriParser INSTANCE = new UriParser();

    @Override
    public @Nullable Type getType() {
        return URI.class;
    }

    @Override
    public @NotNull URI parse(@NotNull Object object) throws Throwable {
        if (object instanceof Path) {
            return ((Path) object).toUri();
        }

        final Object first = AnyIterable.first(object);
        Objects.requireNonNull(first, "Cannot get effective first element");

        if (first instanceof URI) {
            return (URI) first;
        } else if (first instanceof File) {
            return ((File) first).toURI();
        } else if (first instanceof Path) {
            return ((Path) first).toUri();
        }

        if (first instanceof URL) {
            return ((URL) first).toURI();
        }
        return new URI(String.valueOf(first));
    }
}
