package com.saicone.types;

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
}
