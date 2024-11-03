package com.saicone.types.parser;

import com.saicone.types.IterableType;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.regex.Pattern;

/**
 * Represents a parser that try to convert any String representation of Pattern.
 *
 * @author Rubenicos
 */
public class PatternParser implements TypeParser<Pattern> {

    private final int flags;

    /**
     * Create a patter parser with default options.
     */
    public PatternParser() {
        this(0);
    }

    /**
     * Create a pattern parser with given flags to compile patterns.
     *
     * @param flags a bit mask of match flags.
     */
    public PatternParser(int flags) {
        this.flags = flags;
    }

    /**
     * Return the flags used to compile patterns.
     *
     * @return a bit mask of match flags.
     */
    public int getFlags() {
        return flags;
    }

    @Override
    public @Nullable Type getType() {
        return Pattern.class;
    }

    @Override
    public @Nullable Pattern parse(@NotNull Object object) {
        final Object first = IterableType.of(object).first();
        if (first instanceof Pattern) {
            return (Pattern) first;
        }

        return Pattern.compile(String.valueOf(first), 0);
    }
}
