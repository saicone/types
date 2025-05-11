package com.saicone.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Set;

public class AnySet<E> extends AnyCollection<E> {

    private static final TypeParser<AnySet<Object>> PARSER = new TypeParser<AnySet<Object>>() {
        @Override
        public @NotNull Type getType() {
            return AnySet.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public @Nullable AnySet<Object> parse(@NotNull Object object) {
            if (object instanceof Set) {
                return of((Set<Object>) object);
            }
            return null;
        }
    };

    @NotNull
    public static TypeParser<AnySet<Object>> parser() {
        return PARSER;
    }

    @NotNull
    public static AnySet<Object> of(@NotNull Set<Object> set) {
        return new AnySet<>(set, Types.OBJECT);
    }

    public AnySet(@NotNull TypeParser<E> elementParser) {
        this(new HashSet<>(), elementParser);
    }

    public AnySet(@NotNull Set<E> value, @NotNull TypeParser<E> elementParser) {
        super(value, elementParser);
    }

    @Override
    public Set<E> value() {
        return (Set<E>) super.value();
    }
}
