package com.saicone.types.parser;

import com.saicone.types.AnnotatedTypeParser;
import com.saicone.types.AnyIterable;
import com.saicone.types.annotation.PatternFlags;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.AnnotatedType;
import java.util.regex.Pattern;

/**
 * Represents a parser that try to convert any String representation of Pattern,
 * compatible with any type annotated with {@link PatternFlags}.
 *
 * @author Rubenicos
 */
public class PatternAnnotatedParser extends PatternParser implements AnnotatedTypeParser<Pattern> {

    /**
     * {@link PatternAnnotatedParser} public instance.
     */
    public static final PatternAnnotatedParser INSTANCE = new PatternAnnotatedParser();

    @Override
    public @Nullable Pattern parse(@NotNull AnnotatedType type, @NotNull Object object) {
        final PatternFlags annotation = type.getAnnotation(PatternFlags.class);
        if (annotation == null) {
            return parse(object);
        }

        final Object first = AnyIterable.of(object).first();
        if (first instanceof Pattern) {
            return (Pattern) first;
        }

        int flags = getFlags();
        for (int ordinal : annotation.value()) {
            flags |= ordinal;
        }

        return Pattern.compile(String.valueOf(first), flags);
    }
}
