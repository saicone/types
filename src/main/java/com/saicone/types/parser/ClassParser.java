package com.saicone.types.parser;

import com.saicone.types.IterableType;
import com.saicone.types.TypeParser;
import com.saicone.types.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;

/**
 * Represents a parser that try to convert any String representation of Class name.<br>
 * This parser is compatible with qualified name, file path and class descriptor.
 *
 * @author Rubenicos
 */
public class ClassParser implements TypeParser<Class<?>> {

    /**
     * {@link ClassParser} public instance.
     */
    public static final ClassParser INSTANCE = new ClassParser();

    @Override
    public @Nullable Type getType() {
        return Class.class;
    }

    @Override
    public @Nullable Class<?> parse(@NotNull Object object) {
        final Object first = IterableType.of(object).first();
        if (first == null) {
            return null;
        }

        if (first instanceof Class) {
            return (Class<?>) first;
        }

        String className = String.valueOf(first).replace('/', '.');
        if (className.endsWith(";")) {
            if (className.startsWith("[")) {
                for (int i = 0; i < className.length(); i++) {
                    final char c = className.charAt(i);
                    if (c == '[') {
                        continue;
                    } else if (c == 'L') {
                        className = className.substring(0, i) + className.substring(i + 1);
                    }
                    break;
                }
            } else if (className.startsWith("L")) {
                className = className.substring(1);
            }

            className = className.substring(0, className.length() - 1);
        } else if (className.endsWith(".class")) {
            className = className.substring(0, className.length() - 6);
        } else if (className.endsWith(".java")) {
            className = className.substring(0, className.length() - 5);
        }

        try {
            return Class.forName(className, false, Types.class.getClassLoader());
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    }
}
