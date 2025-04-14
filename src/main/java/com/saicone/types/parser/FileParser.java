package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
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

    /**
     * {@link FileParser} public instance.
     */
    public static final FileParser INSTANCE = new FileParser();

    @Override
    public @Nullable Type getType() {
        return File.class;
    }

    @Override
    public @Nullable File parse(@NotNull Object object) {
        final Object single = AnyIterable.of(object).single();
        if (single == null) {
            return null;
        }

        if (single instanceof File) {
            return (File) single;
        } else if (single instanceof Path) {
            return ((Path) single).toFile();
        }

        final String[] array;
        if (single instanceof String[]) {
            array = (String[]) single;
        } else {
            array = String.valueOf(single).split("/");
        }

        File file = null;
        for (String s : array) {
            file = new File(file, s);
        }
        return file;
    }
}
