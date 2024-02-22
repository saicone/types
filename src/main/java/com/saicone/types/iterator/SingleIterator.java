package com.saicone.types.iterator;

import com.saicone.types.TypeIterator;
import org.jetbrains.annotations.NotNull;

import java.util.NoSuchElementException;

/**
 * Abstract class to iterate into single replaceable object.
 *
 * @author Rubenicos
 *
 * @param <T> the object type.
 */
public abstract class SingleIterator<T> extends TypeIterator<T> {

    private boolean consumed = false;

    /**
     * Constructs a single object iterator.
     *
     * @param value the object to iterate.
     */
    public SingleIterator(@NotNull Object value) {
        super(value);
    }

    @Override
    public boolean hasNext() {
        return !consumed && getValue() != null;
    }

    @Override
    @SuppressWarnings("unchecked")
    public T next() {
        if (consumed || getValue() == null) {
            throw new NoSuchElementException();
        }
        consumed = true;
        return (T) getValue();
    }

    @Override
    public void remove() {
        if (consumed) {
            this.value = null;
            setValue(null);
        } else {
            throw new IllegalStateException();
        }
    }
}
