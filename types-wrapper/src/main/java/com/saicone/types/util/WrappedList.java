package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Collection;
import java.util.List;
import java.util.ListIterator;

/**
 * Represents a {@link List} of type A as a {@link List} of type B.
 *
 * @author Rubenicos
 *
 * @param <A> the base type of object.
 * @param <B> the type of object to represent A.
 */
public class WrappedList<A, B> extends WrappedCollection<A, B> implements List<B> {

    /**
     * Constructs a list of type A with its types represented as B.
     *
     * @param delegated the delegated list that will be wrapped in the instance.
     * @param wrapper   the type wrapper to represent delegated list values as B and convert it back to A.
     */
    public WrappedList(@NotNull List<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
        super(delegated, wrapper);
    }

    @Override
    public @NotNull List<A> getDelegated() {
        return (List<A>) super.getDelegated();
    }

    @Override
    public boolean addAll(int index, @NotNull Collection<? extends B> c) {
        return addAnyAll(index, c);
    }

    /**
     * Same as {@link List#addAll(int, Collection)} with any Object compatibility.
     *
     * @param index index at which to insert the first element from the specified collection
     * @param c     collection containing elements to be added to this list
     * @return      {@code true} if this list changed as a result of the call
     */
    @SuppressWarnings("unchecked")
    public boolean addAnyAll(int index, @NotNull Collection<?> c) {
        if (c instanceof WrappedCollection && isSimilar((WrappedObject<?, ?>) c)) {
            return getDelegated().addAll(index, (Collection<? extends A>) ((WrappedCollection<?, ?>) c).getDelegated());
        }
        boolean result = false;
        for (Object o : c) {
            addAny(index, o);
            index++;
            result = true;
        }
        return result;
    }

    @Override
    public B get(int index) {
        return wrap(getDelegated().get(index));
    }

    @Override
    public B set(int index, B element) {
        return wrap(getDelegated().set(index, unwrap(element)));
    }

    /**
     * Same as {@link List#set(int, Object)} with any Object compatibility.
     *
     * @param index   index of the element to replace
     * @param element element to be stored at the specified position
     * @return        the element previously at the specified position
     */
    public B setAny(int index, Object element) {
        return wrap(getDelegated().set(index, unwrap(element)));
    }

    @Override
    public void add(int index, B element) {
        getDelegated().add(index, unwrap(element));
    }

    /**
     * Same as {@link List#add(int, Object)} with any Object compatibility.
     *
     * @param index   index at which the specified element is to be inserted
     * @param element element to be inserted
     */
    public void addAny(int index, Object element) {
        getDelegated().add(index, unwrap(element));
    }

    @Override
    public B remove(int index) {
        return wrap(getDelegated().remove(index));
    }

    @Override
    public int indexOf(Object o) {
        return getDelegated().indexOf(unwrap(o));
    }

    @Override
    public int lastIndexOf(Object o) {
        return getDelegated().lastIndexOf(unwrap(o));
    }

    @Override
    public @NotNull ListIterator<B> listIterator() {
        return new Iterator<>(getDelegated().listIterator(), getWrapper());
    }

    @Override
    public @NotNull ListIterator<B> listIterator(int index) {
        return new Iterator<>(getDelegated().listIterator(index), getWrapper());
    }

    @Override
    public @NotNull List<B> subList(int fromIndex, int toIndex) {
        return new WrappedList<>(getDelegated().subList(fromIndex, toIndex), getWrapper());
    }

    /**
     * Represents a {@link ListIterator} of type A as a {@link ListIterator} of type B.
     *
     * @param <A> the base type of object.
     * @param <B> the type of object to represent A.
     */
    public static class Iterator<A, B> extends WrappedIterator<A, B> implements ListIterator<B> {

        /**
         * Constructs a list iterator of type A with its types represented as B.
         *
         * @param delegated the delegated list iterator that will be wrapped in the instance.
         * @param wrapper   the type wrapper to represent delegated list iterator values as B and convert it back to A.
         */
        public Iterator(@NotNull ListIterator<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
            super(delegated, wrapper);
        }

        @Override
        public @NotNull ListIterator<A> getDelegated() {
            return (ListIterator<A>) super.getDelegated();
        }

        @Override
        public boolean hasPrevious() {
            return getDelegated().hasPrevious();
        }

        @Override
        public B previous() {
            return wrap(getDelegated().previous());
        }

        @Override
        public int nextIndex() {
            return getDelegated().nextIndex();
        }

        @Override
        public int previousIndex() {
            return getDelegated().previousIndex();
        }

        @Override
        public void set(B b) {
            getDelegated().set(unwrap(b));
        }

        /**
         * Same as {@link ListIterator#set(Object)} with any Object compatibility.
         *
         * @param o the element with which to replace the last element returned by {@code next} or {@code previous}
         */
        public void setAny(Object o) {
            getDelegated().set(unwrap(o));
        }

        @Override
        public void add(B b) {
            getDelegated().add(unwrap(b));
        }

        /**
         * Same as {@link ListIterator#add(Object)} with any Object compatibility.
         *
         * @param o the element to insert
         */
        public void addAny(Object o) {
            getDelegated().add(unwrap(o));
        }
    }
}
