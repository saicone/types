package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;

/**
 * Represents a two-dimensional Object with its respective left and right type A that can be converted to left and right type B.
 *
 * @author Rubenicos
 *
 * @param <LeftA> the base type of left object.
 * @param <LeftB> the type of object to represent left A.
 * @param <RightA> the base type of right object.
 * @param <RightB> the type of object to represent right A.
 */
public class WrappedPair<LeftA, LeftB, RightA, RightB> {

    private final Object delegated;
    private final TypeWrapper<LeftA, LeftB> leftWrapper;
    private final TypeWrapper<RightA, RightB> rightWrapper;

    /**
     * Constructs a wrapped two-dimensional object with provided delegated object and type wrappers.
     *
     * @param delegated    the delegated object that will be wrapped in the instance.
     * @param leftWrapper  the type wrapper to represent delegated left B type and convert it back to left A type.
     * @param rightWrapper the type wrapper to represent delegated right B type and convert it back to right A type.
     */
    public WrappedPair(@NotNull Object delegated, @NotNull TypeWrapper<LeftA, LeftB> leftWrapper, @NotNull TypeWrapper<RightA, RightB> rightWrapper) {
        this.delegated = delegated;
        this.leftWrapper = leftWrapper;
        this.rightWrapper = rightWrapper;
    }

    /**
     * Check if the provided wrapped object is similar to current wrapped object.
     *
     * @param wrapped the wrapped object to check.
     * @return        true if both wrappers are similar.
     */
    public boolean isSimilar(@NotNull WrappedPair<?, ?, ?, ?> wrapped) {
        return wrapped.getLeftWrapper() == getLeftWrapper() && wrapped.getRightWrapper() == getRightWrapper();
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
     * Get the current type wrapper that is used to represent delegated left B type and convert it back to left A type.
     *
     * @return a type wrapper.
     */
    @NotNull
    protected TypeWrapper<LeftA, LeftB> getLeftWrapper() {
        return leftWrapper;
    }

    /**
     * Get the current type wrapper that is used to represent delegated right B type and convert it back to right A type.
     *
     * @return a type wrapper.
     */
    @NotNull
    protected TypeWrapper<RightA, RightB> getRightWrapper() {
        return rightWrapper;
    }

    /**
     * Wrap the provided object as left B type, mostly known as a conversion to represent left A type as left B type.
     *
     * @param object the object to wrap.
     * @return       a wrapped left object.
     */
    protected LeftB wrapLeft(Object object) {
        return leftWrapper.wrap(object);
    }

    /**
     * Wrap the provided object as right B type, mostly known as a conversion to represent right A type as right B type.
     *
     * @param object the object to wrap.
     * @return       a wrapped right object.
     */
    protected RightB wrapRight(Object object) {
        return rightWrapper.wrap(object);
    }

    /**
     * Unwrap the provided object as left A type, mostly known as a conversion to bring back left B type as left A type.
     *
     * @param object the object to unwrap.
     * @return       an unwrapped object as base left type used in this instance.
     */
    protected LeftA unwrapLeft(Object object) {
        return leftWrapper.unwrap(object);
    }

    /**
     * Unwrap the provided object as right A type, mostly known as a conversion to bring back right B type as right A type.
     *
     * @param object the object to unwrap.
     * @return       an unwrapped object as base right type used in this instance.
     */
    protected RightA unwrapRight(Object object) {
        return rightWrapper.unwrap(object);
    }
}
