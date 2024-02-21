package com.saicone.types;

import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Abstract class to iterate into replaceable value.
 *
 * @author Rubenicos
 *
 * @param <T> the object type.
 */
public abstract class TypeIterator<T> implements Iterator<T> {

    private final Object value;

    /**
     * Constructs a type iterator.
     *
     * @param value the value to iterate.
     */
    public TypeIterator(@NotNull Object value) {
        this.value = value;
    }

    /**
     * Get the object to iterate.
     *
     * @return an object.
     */
    public Object getValue() {
        return value;
    }

    /**
     * Replace the current object.
     *
     * @param value an object.
     */
    public abstract void setValue(Object value);
}
