package com.saicone.types;

import org.jetbrains.annotations.NotNull;

import java.util.HashSet;
import java.util.Set;

public class AnySet<E> extends AnyCollection<E> {

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
