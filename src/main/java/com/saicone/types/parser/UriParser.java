package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.nio.file.Path;

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
    public @Nullable URI parse(@NotNull Object object) {
        if (object instanceof Path) {
            return ((Path) object).toUri();
        }
        final Object first = AnyIterable.of(object).first();
        if (first == null) {
            return null;
        }

        if (first instanceof URI) {
            return (URI) first;
        } else if (first instanceof File) {
            return ((File) first).toURI();
        } else if (first instanceof Path) {
            return ((Path) first).toUri();
        }

        try {
            if (first instanceof URL) {
                return ((URL) first).toURI();
            }
            return new URI(String.valueOf(first));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
