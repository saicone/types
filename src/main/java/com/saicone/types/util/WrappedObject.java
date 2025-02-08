package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Represents an Object type A that can be converted to type B.
 *
 * @author Rubenicos
 *
 * @param <A> the base type of object.
 * @param <B> the type of object to represent A.
 */
public class WrappedObject<A, B> {

    private final Object delegated;
    private final TypeWrapper<A, B> wrapper;

    /**
     * Constructs a wrapped object with provided delegated object and type wrapper.
     *
     * @param delegated the delegated object that will be wrapped in the instance.
     * @param wrapper   the type wrapper to represent delegated object as B and convert it back to A.
     */
    public WrappedObject(@NotNull Object delegated, @NotNull TypeWrapper<A, B> wrapper) {
        this.delegated = delegated;
        this.wrapper = wrapper;
    }

    /**
     * Get the delegated object that is wrapped in this instance.
     *
     * @return a delegated object.
     */
    @NotNull
    public Object getDelegated() {
        return delegated;
    }

    /**
     * Get the current type wrapper that is used to represent delegated object as B and convert it back to A.
     *
     * @return a type wrapper.
     */
    @NotNull
    protected TypeWrapper<A, B> getWrapper() {
        return wrapper;
    }

    /**
     * Wrap the provided object as type B, mostly known as a conversion to represent type A as B.
     *
     * @param object the object to wrap.
     * @return       a wrapped object.
     */
    protected B wrap(Object object) {
        if (object == null) {
            return null;
        }
        return wrapper.wrap(object);
    }

    /**
     * Unwrap the provided object as type A, mostly known as a conversion to bring back type B as A.
     *
     * @param object the object to unwrap.
     * @return       an unwrapped object as base type of object used in this instance.
     */
    protected A unwrap(Object object) {
        if (object == null) {
            return null;
        }
        return wrapper.unwrap(object);
    }

    @Override
    public String toString() {
        return getDelegated().toString();
    }

    @Override
    public final boolean equals(Object object) {
        if (this == object) return true;
        if (!(object instanceof WrappedObject)) {
            return getDelegated().equals(object);
        }

        WrappedObject<?, ?> that = (WrappedObject<?, ?>) object;
        return getDelegated().equals(that.getDelegated());
    }

    @Override
    public int hashCode() {
        return getDelegated().hashCode();
    }
}
