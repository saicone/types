package com.saicone.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.UnknownNullability;

/**
 * Represents a function that takes an object and a type parser, and returns a result of type R.<br>
 * This is generally to mark a function that can throw an exception during the parsing process.
 *
 * @author Rubenicos
 *
 * @param <T> the type result of the parser
 * @param <R> the type result of the function
 * @param <X> the type of exception that can be thrown
 */
@FunctionalInterface
public interface TypeParserFunction<T extends TypeParser<?>, R, X extends Throwable> {

    /**
     * Applies this function to the given arguments.
     *
     * @param parser the type parser to use for parsing
     * @param object the input object
     * @return the function result
     * @throws X if the parsing fails
     */
    @UnknownNullability
    R apply(@NotNull T parser, Object object) throws X;
}
