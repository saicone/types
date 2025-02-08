package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Comparator;
import java.util.Iterator;
import java.util.NavigableSet;
import java.util.Set;
import java.util.SortedSet;

/**
 * Represents a {@link Set} of type A as a {@link Set} of type B.
 *
 * @author Rubenicos
 *
 * @param <A> the base type of object.
 * @param <B> the type of object to represent A.
 */
public class WrappedSet<A, B> extends WrappedCollection<A, B> implements Set<B> {

    /**
     * Constructs a set of type A with its types represented as B.
     *
     * @param delegated the delegated set that will be wrapped in the instance.
     * @param wrapper   the type wrapper to represent delegated set values as B and convert it back to A.
     */
    public WrappedSet(@NotNull Set<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
        super(delegated, wrapper);
    }

    @Override
    public @NotNull Set<A> getDelegated() {
        return (Set<A>) super.getDelegated();
    }

    /**
     * Represents a {@link SortedSet} of type A as a {@link SortedSet} of type B.
     *
     * @author Rubenicos
     *
     * @param <A> the base type of object.
     * @param <B> the type of object to represent A.
     */
    public static class Sorted<A, B> extends WrappedSet<A, B> implements SortedSet<B> {

        /**
         * Constructs a sorted set of type A with its types represented as B.
         *
         * @param delegated the delegated sorted set that will be wrapped in the instance.
         * @param wrapper   the type wrapper to represent delegated sorted set values as B and convert it back to A.
         */
        public Sorted(@NotNull SortedSet<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
            super(delegated, wrapper);
        }

        @Override
        public @NotNull SortedSet<A> getDelegated() {
            return (SortedSet<A>) super.getDelegated();
        }

        @Override
        public @Nullable Comparator<? super B> comparator() {
            final Comparator<? super A> comparator = getDelegated().comparator();
            return comparator == null ? null : (b1, b2) -> comparator.compare(unwrap(b1), unwrap(b2));
        }

        @Override
        public @NotNull SortedSet<B> subSet(B fromElement, B toElement) {
            return new Sorted<>(getDelegated().subSet(unwrap(fromElement), unwrap(toElement)), getWrapper());
        }

        @Override
        public @NotNull SortedSet<B> headSet(B toElement) {
            return new Sorted<>(getDelegated().headSet(unwrap(toElement)), getWrapper());
        }

        @Override
        public @NotNull SortedSet<B> tailSet(B fromElement) {
            return new Sorted<>(getDelegated().tailSet(unwrap(fromElement)), getWrapper());
        }

        @Override
        public B first() {
            return wrap(getDelegated().first());
        }

        @Override
        public B last() {
            return wrap(getDelegated().last());
        }
    }

    /**
     * Represents a {@link NavigableSet} of type A as a {@link NavigableSet} of type B.
     *
     * @author Rubenicos
     *
     * @param <A> the base type of object.
     * @param <B> the type of object to represent A.
     */
    public static class Navigable<A, B> extends Sorted<A, B> implements NavigableSet<B> {

        /**
         * Constructs a navigable set of type A with its types represented as B.
         *
         * @param delegated the delegated navigable set that will be wrapped in the instance.
         * @param wrapper   the type wrapper to represent delegated navigable set values as B and convert it back to A.
         */
        public Navigable(@NotNull NavigableSet<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
            super(delegated, wrapper);
        }

        @Override
        public @NotNull NavigableSet<A> getDelegated() {
            return (NavigableSet<A>) super.getDelegated();
        }

        @Override
        public @Nullable B lower(B b) {
            return wrap(getDelegated().lower(unwrap(b)));
        }

        @Override
        public @Nullable B floor(B b) {
            return wrap(getDelegated().floor(unwrap(b)));
        }

        @Override
        public @Nullable B ceiling(B b) {
            return wrap(getDelegated().ceiling(unwrap(b)));
        }

        @Override
        public @Nullable B higher(B b) {
            return wrap(getDelegated().higher(unwrap(b)));
        }

        @Override
        public @Nullable B pollFirst() {
            return wrap(getDelegated().pollFirst());
        }

        @Override
        public @Nullable B pollLast() {
            return wrap(getDelegated().pollLast());
        }

        @Override
        public @NotNull NavigableSet<B> descendingSet() {
            return new Navigable<>(getDelegated().descendingSet(), getWrapper());
        }

        @Override
        public @NotNull Iterator<B> descendingIterator() {
            return new WrappedIterator<>(getDelegated().descendingIterator(), getWrapper());
        }

        @Override
        public @NotNull NavigableSet<B> subSet(B fromElement, boolean fromInclusive, B toElement, boolean toInclusive) {
            return new Navigable<>(getDelegated().subSet(unwrap(fromElement), fromInclusive, unwrap(toElement), toInclusive), getWrapper());
        }

        @Override
        public @NotNull NavigableSet<B> headSet(B toElement, boolean inclusive) {
            return new Navigable<>(getDelegated().headSet(unwrap(toElement), inclusive), getWrapper());
        }

        @Override
        public @NotNull NavigableSet<B> tailSet(B fromElement, boolean inclusive) {
            return new Navigable<>(getDelegated().tailSet(unwrap(fromElement), inclusive), getWrapper());
        }
    }
}
