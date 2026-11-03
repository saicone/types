package com.saicone.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Represents a function that takes an object and a type parser, and returns a result of type T.
 *
 * @author Rubenicos
 *
 * @param <T> the type of the result
 */
@FunctionalInterface
public interface TypeParserFunction<T> {

    /**
     * Applies this function to the given arguments.
     *
     * @param object the input object
     * @param parser the type parser to use for parsing
     * @return       the function result
     * @throws Throwable if the parsing fails
     */
    @UnknownNullability
    T apply(Object object, @NotNull TypeParser<T> parser) throws Throwable;
}
