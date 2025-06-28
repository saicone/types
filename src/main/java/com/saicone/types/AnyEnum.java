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
     * @param type the type of enum to convert.
     * @return     a type enum representation.
     * @param <E>  the type of provided enum class.
     */
    default <E extends Enum<E>> E as(@NotNull Class<E> type) {
        return EnumParser.of(type).parse(this);
    }
}
