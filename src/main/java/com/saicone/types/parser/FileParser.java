package com.saicone.types.parser;

import com.saicone.types.IterableType;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Type;
import java.nio.file.Path;

/**
 * Represents a parser that try to convert any object type to File.<br>
 * This parser is compatible with Path and String separated by {@code /}.
 *
 * @author Rubenicos
 */
public class FileParser implements TypeParser<File> {

    @Override
    public @Nullable Type getType() {
        return File.class;
    }

    @Override
    public @Nullable File parse(@NotNull Object object) {
        final Object first = IterableType.of(object).first();
        if (first == null) {
            return null;
        }

        if (first instanceof File) {
            return (File) first;
        } else if (first instanceof Path) {
            return ((Path) first).toFile();
        }

        final String[] array;
        if (first instanceof String[]) {
            array = (String[]) first;
        } else {
            array = String.valueOf(first).split("/");
        }

        File file = null;
        for (String s : array) {
            file = new File(file, s);
        }
        return file;
    }
}
