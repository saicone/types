package com.saicone.types.parser;

import com.saicone.types.IterableType;
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

    @Override
    public @Nullable Type getType() {
        return Path.class;
    }

    @Override
    public @Nullable Path parse(@NotNull Object object) {
        final Object firstObj = IterableType.of(object).first();
        if (firstObj == null) {
            return null;
        }

        if (firstObj instanceof Path) {
            return (Path) firstObj;
        } else if (firstObj instanceof File) {
            return ((File) firstObj).toPath();
        }

        final String first;
        String[] more;
        if (firstObj instanceof String[]) {
            more = (String[]) firstObj;
        } else {
            more = String.valueOf(firstObj).split("/");
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
