package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a two-dimensional Object with its respective type A1 and A2 that can be converted to type B1 and B2.<br>
 * <br>
 * The name of this class is a convention in this library to allow an easy understand about wrapped objects with two type parameters.
 *
 * @author Rubenicos
 *
 * @param <OneA> the base type of A1 object.
 * @param <OneB> the type of object to represent A1.
 * @param <TwoA> the base type of A2.
 * @param <TwoB> the type of object to represent A2.
 */
public class WrappedObject2<OneA, OneB, TwoA, TwoB> {

    private final Object delegated;
    private final TypeWrapper<OneA, OneB> wrapperOne;
    private final TypeWrapper<TwoA, TwoB> wrapperTwo;

    /**
     * Constructs a wrapped two-dimensional object with provided delegated object and type wrappers.
     *
     * @param delegated  the delegated object that will be wrapped in the instance.
     * @param wrapperOne the type wrapper to represent delegated B1 type and convert it back to A1 type.
     * @param wrapperTwo the type wrapper to represent delegated B2 type and convert it back to A2 type.
     */
    public WrappedObject2(@NotNull Object delegated, @NotNull TypeWrapper<OneA, OneB> wrapperOne, @NotNull TypeWrapper<TwoA, TwoB> wrapperTwo) {
        this.delegated = delegated;
        this.wrapperOne = wrapperOne;
        this.wrapperTwo = wrapperTwo;
    }

    /**
     * Check if the provided wrapped object is similar to current wrapped object.
     *
     * @param wrapped the wrapped object to check.
     * @return        true if both wrappers are similar.
     */
    public boolean isSimilar(@NotNull WrappedObject2<?, ?, ?, ?> wrapped) {
        return wrapped.getWrapperOne() == getWrapperOne() && wrapped.getWrapperTwo() == getWrapperTwo();
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
     * Get the current type wrapper that is used to represent delegated B1 type and convert it back to A1 type.
     *
     * @return a type wrapper.
     */
    @NotNull
    protected TypeWrapper<OneA, OneB> getWrapperOne() {
        return wrapperOne;
    }

    /**
     * Get the current type wrapper that is used to represent delegated B2 type and convert it back to A2 type.
     *
     * @return a type wrapper.
     */
    @NotNull
    protected TypeWrapper<TwoA, TwoB> getWrapperTwo() {
        return wrapperTwo;
    }

    /**
     * Wrap the provided object as B1 type, mostly known as a conversion to represent A1 type as B1 type.
     *
     * @param object the object to wrap.
     * @return       a wrapped B1 object.
     */
    protected OneB wrapOne(Object object) {
        return wrapperOne.wrap(object);
    }

    /**
     * Wrap the provided object as B2 type, mostly known as a conversion to represent A2 type as B2 type.
     *
     * @param object the object to wrap.
     * @return       a wrapped B2 object.
     */
    protected TwoB wrapTwo(Object object) {
        return wrapperTwo.wrap(object);
    }

    /**
     * Unwrap the provided object as A1 type, mostly known as a conversion to bring back B1 type as A1 type.
     *
     * @param object the object to unwrap.
     * @return       an unwrapped object as base A1 type used in this instance.
     */
    protected OneA unwrapOne(Object object) {
        return wrapperOne.unwrap(object);
    }

    /**
     * Unwrap the provided object as A2 type, mostly known as a conversion to bring back B2 type as A2 type.
     *
     * @param object the object to unwrap.
     * @return       an unwrapped object as base A2 type used in this instance.
     */
    protected TwoA unwrapTwo(Object object) {
        return wrapperTwo.unwrap(object);
    }
}
