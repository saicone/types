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
    public @NotNull Collection<A> getUnwrapped() {
        return (Collection<A>) super.getUnwrapped();
    }

    @Override
    public int size() {
        return getUnwrapped().size();
    }

    @Override
    public boolean isEmpty() {
        return getUnwrapped().isEmpty();
    }

    @Override
    public boolean contains(Object o) {
        return getUnwrapped().contains(unwrap(o));
    }

    @Override
    public @NotNull Iterator<B> iterator() {
        return new WrappedIterator<>(getUnwrapped().iterator(), getWrapper());
    }

    @Override
    public @NotNull Object @NotNull [] toArray() {
        final Object[] delegated = getUnwrapped().toArray();
        final Object[] array = new Object[delegated.length];
        for (int i = 0; i < delegated.length; i++) {
            array[i] = wrap(delegated[i]);
        }
        return array;
    }

    @Override
    @SuppressWarnings("unchecked")
    public @NotNull <T> T @NotNull [] toArray(@NotNull T @NotNull [] a) {
        final T[] delegated = getUnwrapped().toArray(a);
        final T[] array = (T[]) Array.newInstance(a.getClass(), delegated.length);
        for (int i = 0; i < delegated.length; i++) {
            array[i] = (T) wrap(delegated[i]);
        }
        return array;
    }

    @Override
    public boolean add(B b) {
        return getUnwrapped().add(unwrap(b));
    }

    /**
     * Same as {@link Collection#add(Object)} with any Object compatibility.
     *
     * @param o element whose presence in this collection is to be ensured.
     * @return  {@code true} if this collection changed as a result of the call
     */
    public boolean addAny(Object o) {
        return getUnwrapped().add(unwrap(o));
    }

    @Override
    public boolean remove(Object o) {
        return getUnwrapped().remove(unwrap(o));
    }

    @Override
    public boolean containsAll(@NotNull Collection<?> c) {
        if (c instanceof WrappedCollection && isSimilar((WrappedObject<?, ?>) c)) {
            return getUnwrapped().containsAll(((WrappedCollection<?, ?>) c).getUnwrapped());
        }
        for (Object o : c) {
            if (!getUnwrapped().contains(unwrap(o))) {
                return false;
            }
        }
        return true;
    }

    @Override
    public boolean addAll(@NotNull Collection<? extends B> c) {
        return addAnyAll(c);
    }

    /**
     * Same as {@link Collection#addAll(Collection)} with any Object compatibility.
     *
     * @param c collection containing elements to be added to this collection
     * @return  {@code true} if this collection changed as a result of the call
     */
    @SuppressWarnings("unchecked")
    public boolean addAnyAll(@NotNull Collection<?> c) {
        if (c instanceof WrappedCollection && isSimilar((WrappedObject<?, ?>) c)) {
            return getUnwrapped().addAll((Collection<? extends A>) ((WrappedCollection<?, ?>) c).getUnwrapped());
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
            return getUnwrapped().removeAll(((WrappedCollection<?, ?>) c).getUnwrapped());
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
            return getUnwrapped().retainAll(((WrappedCollection<?, ?>) c).getUnwrapped());
        }
        return this.removeIf(object -> !c.contains(object));
    }

    @Override
    public void clear() {
        getUnwrapped().clear();
    }
}
