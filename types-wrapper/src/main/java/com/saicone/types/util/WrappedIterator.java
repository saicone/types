package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Iterator;

/**
 * Represents a {@link Iterator} of type A as a {@link Iterator} of type B.
 *
 * @author Rubenicos
 *
 * @param <A> the base type of object.
 * @param <B> the type of object to represent A.
 */
public class WrappedIterator<A, B> extends WrappedObject<A, B> implements Iterator<B> {

    /**
     * Constructs an iterator of type A with its types represented as B.
     *
     * @param delegated the delegated iterator that will be wrapped in the instance.
     * @param wrapper   the type wrapper to represent delegated iterator values as B and convert it back to A.
     */
    public WrappedIterator(@NotNull Iterator<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
        super(delegated, wrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Iterator<A> getUnwrapped() {
        return (Iterator<A>) super.getUnwrapped();
    }

    @Override
    public boolean hasNext() {
        return getUnwrapped().hasNext();
    }

    @Override
    public B next() {
        return wrap(getUnwrapped().next());
    }

    @Override
    public void remove() {
        getUnwrapped().remove();
    }
}
