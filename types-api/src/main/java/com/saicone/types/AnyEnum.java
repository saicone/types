package com.saicone.types;

import com.saicone.types.parser.EnumParser;
import org.jetbrains.annotations.NotNull;

/**
 * Utility interface made for enums, to allow a flexible compatibility with other types.
 *
 * @author Rubenicos
 */
public interface AnyEnum {

    /**
     * Get the current enum value as other enum type.
     *
     * @param type the type of enum to convert
     * @return     a type enum representation
     * @param <E>  the type of provided enum class
     */
    @NotNull
    default <E extends Enum<E>> E asEnum(@NotNull Class<E> type) {
        return asEnum(type, TypeParser::parseOrThrow);
    }

    /**
     * Get the current enum value as other enum type.
     *
     * @param type     the type of enum to convert
     * @param function the function to run with the object and the type parser
     * @return         a type enum representation
     * @throws X if the parsing fails
     * @param <E> the type of provided enum class
     * @param <X> the type of exception that can be thrown
     */
    @NotNull
    default <E extends Enum<E>, X extends Throwable> E asEnum(@NotNull Class<E> type, @NotNull TypeParserFunction<EnumParser<E>, E, X> function) throws X {
        return function.apply(EnumParser.of(type), this);
    }
}
