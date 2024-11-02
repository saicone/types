package com.saicone.types.parser;

import com.saicone.types.IterableType;
import com.saicone.types.TypeParser;
import com.saicone.types.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Method;
import java.lang.reflect.Type;

/**
 * Represents a function that try to parse any type of object to enum type.<br>
 * This parser is also compatible with enum ordinal, enum name and even other enum object
 * by extracting ordinal value.
 *
 * @author Rubenicos
 *
 * @param <T> the type result of the function.
 */
@FunctionalInterface
public interface EnumParser<T extends Enum<T>> extends TypeParser<T> {

    /**
     * Create an enum type parser that convert any object into provided enum class type.
     *
     * @param type the associated type with the parser.
     * @return     an enum type parser that return an enum type.
     * @param <T>  the enum type result of the function.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    static <T extends Enum<T>> EnumParser<T> of(@NotNull Class<T> type) {
        try {
            final Method method = type.getDeclaredMethod("values");
            method.setAccessible(true);
            final T[] values = (T[]) method.invoke(null);
            return of(type, values);
        } catch (Throwable t) {
            throw new RuntimeException(t);
        }
    }

    /**
     * Create an enum type parser that convert any object into provided enum class type
     * using provided array of enum values.
     *
     * @param type   the associated type with the parser.
     * @param values the enum array values.
     * @return       an enum type parser that return an enum type.
     * @param <T>    the enum type result of the function.
     */
    @NotNull
    static <T extends Enum<T>> EnumParser<T> of(@NotNull Class<T> type, @NotNull T[] values) {
        return new EnumParser<T>() {
            @Override
            public @NotNull Type getType() {
                return type;
            }

            @Override
            public @NotNull T[] values() {
                return values;
            }
        };
    }

    /**
     * Returns an array containing the constants of the enum type used by this parser.
     *
     * @return the enum array values.
     */
    @NotNull
    T[] values();

    @Override
    @SuppressWarnings("unchecked")
    default @Nullable T parse(@NotNull Object object) {
        final Object single = IterableType.of(object).single();
        if (single == null) {
            return null;
        }

        final Type type = getType();
        if (type instanceof Class && ((Class<?>) type).isInstance(single)) {
            return (T) single;
        } else if (single instanceof Number) {
            // ordinal -> enum
            return parse(((Number) single).intValue());
        } else if (single instanceof Enum) {
            // enum -> enum
            return parse(((Enum<?>) single).ordinal());
        } else {
            try {
                final Integer ordinal = Types.INTEGER.parse(single);
                if (ordinal != null) {
                    return parse(ordinal);
                }
            } catch (NumberFormatException ignored) { }

            final String name = String.valueOf(single);
            for (T value : values()) {
                if (value.name().equalsIgnoreCase(name)) {
                    return value;
                }
            }
            return null;
        }
    }

    /**
     * Parses the given ordinal number as required enum type.
     *
     * @param ordinal the ordinal number that represent an enum value.
     * @return        the parsed enum value if ordinal is valid, null otherwise.
     */
    @Nullable
    default T parse(int ordinal) {
        final T[] values = values();
        if (ordinal >= 0 && ordinal < values.length) {
            return values[ordinal];
        }
        return null;
    }
}
