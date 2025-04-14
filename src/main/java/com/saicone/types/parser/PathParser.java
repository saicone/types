package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Arrays;

/**
 * Represents a parser that try to convert any object type to Path.<br>
 * This parser is compatible with File and String separated by {@code /}.
 *
 * @author Rubenicos
 */
public class PathParser implements TypeParser<Path> {

    /**
     * {@link PathParser} public instance.
     */
    public static final PathParser INSTANCE = new PathParser();

    @Override
    public @Nullable Type getType() {
        return Path.class;
    }

    @Override
    public @Nullable Path parse(@NotNull Object object) {
        final Object single = AnyIterable.of(object).single();
        if (single == null) {
            return null;
        }

        if (single instanceof Path) {
            return (Path) single;
        } else if (single instanceof File) {
            return ((File) single).toPath();
        }

        final String first;
        String[] more;
        if (single instanceof String[]) {
            more = (String[]) single;
        } else {
            more = String.valueOf(single).split("/");
        }
        first = more[0];

        if (more.length > 1) {
            more = Arrays.copyOfRange(more, 1, more.length);
        } else {
            more = new String[0];
        }

        return Paths.get(first, more);
    }
}
