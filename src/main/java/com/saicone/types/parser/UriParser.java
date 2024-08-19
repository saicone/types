package com.saicone.types.parser;

import com.saicone.types.IterableType;
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

    @Override
    public @Nullable Type getType() {
        return URI.class;
    }

    @Override
    public @Nullable URI parse(@NotNull Object object) {
        final Object single = IterableType.of(object).single();
        if (single == null) {
            return null;
        }

        if (single instanceof URI) {
            return (URI) single;
        } else if (single instanceof File) {
            return ((File) single).toURI();
        } else if (single instanceof Path) {
            return ((Path) single).toUri();
        }

        try {
            if (single instanceof URL) {
                return ((URL) single).toURI();
            }
            return new URI(String.valueOf(single));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
