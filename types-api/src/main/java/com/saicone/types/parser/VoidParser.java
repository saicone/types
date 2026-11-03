package com.saicone.types.parser;

import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;

/**
 * Represents a parser of java type void.<br>
 * This parser is only for registry purposes, and will always throw an exception
 * when trying to parse any object.
 *
 * @author Rubenicos
 */
public final class VoidParser implements TypeParser<Void> {

    /**
     * {@link VoidParser} public instance.
     */
    public static final VoidParser INSTANCE = new VoidParser();

    private VoidParser() {
    }

    @Override
    public @NotNull Type getType() {
        return Void.class;
    }

    @Override
    public @NotNull Void parse(@NotNull Object object) throws Throwable {
        throw new UnsupportedOperationException("Void type parser cannot parse any object");
    }
}
