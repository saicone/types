package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import com.saicone.types.Types;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigInteger;
import java.util.Objects;

/**
 * Represents a parser that try to convert any object type to Character.<br>
 * This parser extracts the first character from non-empty String value or try
 * to convert a number character representation to char value.
 *
 * @author Rubenicos
 */
public class CharacterParser implements TypeParser<Character> {

    /**
     * {@link CharacterParser} public instance.
     */
    public static final CharacterParser INSTANCE = new CharacterParser();

    @Override
    public @NotNull Character parse(@NotNull Object object) throws Throwable {
        final Object first = AnyIterable.first(object);
        Objects.requireNonNull(first, "Cannot get effective first element");

        if (first instanceof Character) {
            return (Character) first;
        }

        // decimal representation
        if (first instanceof BigInteger || first instanceof Long || first instanceof Integer || first instanceof Short || first instanceof Byte) {
            final Character c = castOrNull(((Number) first).intValue());
            if (c != null) {
                return c;
            }
        }

        final String str = String.valueOf(first);
        if (str.isEmpty()) {
            return '\0';
        } else if (str.startsWith("0x") || str.startsWith("0b")) { // hexadecimal and binary representation
            try {
                final Character c = castOrNull(Types.INTEGER.parse(str));
                if (c != null) {
                    return c;
                }
            } catch (Throwable ignored) { }
        } else if (str.startsWith("&#")) { // HTML decimal representation
            try {
                final Character c = castOrNull(Integer.parseInt(str.substring(2)));
                if (c != null) {
                    return c;
                }
            } catch (Throwable ignored) { }
        }

        return str.charAt(0);
    }

    /**
     * Casts the given integer to a character if it's in the valid range (0-65535).<br>
     * Returns null if the integer is out of range.
     *
     * @param i the integer to cast.
     * @return  a character if the integer is in range, null otherwise.
     */
    @Nullable
    public static Character castOrNull(int i) {
        if (i >= 0 && i <= 65535) {
            return (char) i;
        }
        return null;
    }
}
