package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Array;
import java.util.Collection;
import java.util.Iterator;

/**
 * Represents a {@link Collection} of type A as a {@link Collection} of type B.
 *
 * @author Rubenicos
 *
 * @param <A> the base type of object.
 * @param <B> the type of object to represent A.
 */
public class WrappedCollection<A, B> extends WrappedObject<A, B> implements Collection<B> {

    /**
     * Constructs a collection of type A with its types represented as B.
     *
     * @param delegated the delegated collection that will be wrapped in the instance.
     * @param wrapper   the type wrapper to represent delegated collection values as B and convert it back to A.
     */
    public WrappedCollection(@NotNull Collection<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
        super(delegated, wrapper);
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull Collection<A> getDelegated() {
        return (Collection<A>) super.getDelegated();
    }

    @Override
    public int size() {
        return getDelegated().size();
    }

    @Override
    public boolean isEmpty() {
        return getDelegated().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getDelegated().contains(unwrap(o));
    }

    @Override
    public @NotNull Iterator<B> iterator() {
        return new WrappedIterator<>(getDelegated().iterator(), getWrapper());
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        final Object[] delegated = getDelegated().toArray();
        final Object[] array = new Object[delegated.length];
        for (int i = 0; i < delegated.length; i++) {
            array[i] = wrap(delegated[i]);
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        final T[] delegated = getDelegated().toArray(a);
        final T[] array = (T[]) Array.newInstance(a.getClass(), delegated.length);
        for (int i = 0; i < delegated.length; i++) {
            array[i] = (T) wrap(delegated[i]);
        }
        return array;
    }

    @Override
    public boolean add(B b) {
        return getDelegated().add(unwrap(b));
    }

    public boolean addAny(Object o) {
        return getDelegated().add(unwrap(o));
    }

    @Override
    public boolean remove(Object o) {
        return getDelegated().remove(unwrap(o));
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        if (c instanceof WrappedCollection && isSimilar((WrappedObject<?, ?>) c)) {
            return getDelegated().containsAll(((WrappedCollection<?, ?>) c).getDelegated());
        }
        for (Object o : c) {
            if (!getDelegated().contains(unwrap(o))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends B> c) {
        return addAnyAll(c);
    }

    @SuppressWarnings("unchecked")
    public boolean addAnyAll(@NotNull Collection<?> c) {
        if (c instanceof WrappedCollection && isSimilar((WrappedObject<?, ?>) c)) {
            return getDelegated().addAll((Collection<? extends A>) ((WrappedCollection<?, ?>) c).getDelegated());
        }
        boolean result = false;
        for (Object o : c) {
            if (addAny(o)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean removeAll(@NotNull Collection<?> c) {
        if (c instanceof WrappedCollection && isSimilar((WrappedObject<?, ?>) c)) {
            return getDelegated().removeAll(((WrappedCollection<?, ?>) c).getDelegated());
        }
        boolean result = false;
        for (Object o : c) {
            if (remove(o)) {
                result = true;
            }
        }
        return result;
    }

    @Override
    public boolean retainAll(@NotNull Collection<?> c) {
        if (c instanceof WrappedCollection && isSimilar((WrappedObject<?, ?>) c)) {
            return getDelegated().retainAll(((WrappedCollection<?, ?>) c).getDelegated());
        }
        return this.removeIf(object -> !c.contains(object));
    }

    @Override
    public void clear() {
        getDelegated().clear();
    }
}
