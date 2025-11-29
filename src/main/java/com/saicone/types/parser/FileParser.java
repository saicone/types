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
        if (object instanceof Path) {
            return ((Path) object).toFile();
        }
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

        return getFile(array);
    }

    /**
     * Converts a string path into a {@link File}.<br>
     * This method aims to avoid the usage of Strings with OS-driven separators.
     *
     * @param path a string path, not compatible with file separators.
     * @return     the resulting {@link File}.
     */
    @NotNull
    public static File getFile(@NotNull String... path) {
        File file = null;
        for (String part : path) {
            if (part.isEmpty()) {
                continue;
            }
            file = file == null ? new File(part) : new File(file, part);
        }

        if (file == null) {
            file = new File(".");
        }
        return file;
    }
}
