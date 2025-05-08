package com.saicone.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedType;

/**
 * Represents a function that parse any annotated type of object and converts into value type.
 *
 * @author Rubenicos
 *
 * @param <T> the type result of the function.
 */
@FunctionalInterface
public interface AnnotatedTypeParser<T> extends TypeParser<T> {

    @Override
    default @Nullable T parse(@NotNull Object object) {
        return parse(getClass().getAnnotatedSuperclass(), object);
    }

    /**
     * Parse the given object into required type.
     *
     * @param type   the annotated type that object belongs, current parser by default.
     * @param object the object to parse.
     * @return       a converted value type, null otherwise.
     */
    @Nullable
    T parse(@NotNull AnnotatedType type, @NotNull Object object);

    /**
     * Parse the given object into required type with a default return value if parsed value is null.
     *
     * @param type   the annotated type that object belongs.
     * @param object the object to parse.
     * @param def    the type object to return if parse fails.
     * @return       a converted value type, default object otherwise.
     */
    @Nullable
    @Contract("_, _, !null -> !null")
    default T parse(@NotNull AnnotatedType type, @Nullable Object object, @Nullable T def) {
        if (object == null) {
            return def;
        }
        final T obj = parse(type, object);
        return obj != null ? obj : def;
    }

    /**
     * Parse the given object into required type with a default return value
     * if parsed value is null or the conversion generates an exception.
     *
     * @param type   the annotated type that object belongs.
     * @param object the object to parse.
     * @param def    the type object to return if parse fails.
     * @return       a converted value type, default object otherwise.
     */
    @Nullable
    @Contract("_, _, !null -> !null")
    default T parseOrDefault(@NotNull AnnotatedType type, @Nullable Object object, @Nullable T def) {
        try {
            return parse(type, object, def);
        } catch (Throwable t) {
            return def;
        }
    }
}
