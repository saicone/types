package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
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

    /**
     * {@link PatternParser} public instance.
     */
    public static final PatternParser INSTANCE = new PatternParser();

    /**
     * {@link PatternParser} public instance.
     *
     * @return a pattern parser instance.
     */
    @NotNull
    public static PatternParser instance() {
        try {
            final Class<? extends PatternParser> annotated = Class.forName("com.saicone.types.parser.PatternAnnotatedParser").asSubclass(PatternParser.class);
            return (PatternParser) annotated.getDeclaredField("INSTANCE").get(null);
        } catch (Throwable t) {
            return INSTANCE;
        }
    }

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
        final Object first = AnyIterable.of(object).first();
        if (first instanceof Pattern) {
            return (Pattern) first;
        }

        if (getFlags() == 0) {
            return Pattern.compile(String.valueOf(first));
        } else {
            return Pattern.compile(String.valueOf(first), getFlags());
        }
    }
}
