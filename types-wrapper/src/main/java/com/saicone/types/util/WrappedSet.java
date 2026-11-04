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
    public @NotNull Set<A> getUnwrapped() {
        return (Set<A>) super.getUnwrapped();
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
        public @NotNull SortedSet<A> getUnwrapped() {
            return (SortedSet<A>) super.getUnwrapped();
        }

        @Override
        public @Nullable Comparator<? super B> comparator() {
            final Comparator<? super A> comparator = this.getUnwrapped().comparator();
            return comparator == null ? null : (b1, b2) -> comparator.compare(unwrap(b1), unwrap(b2));
        }

        @Override
        public @NotNull SortedSet<B> subSet(B fromElement, B toElement) {
            return new Sorted<>(this.getUnwrapped().subSet(unwrap(fromElement), unwrap(toElement)), getWrapper());
        }

        @Override
        public @NotNull SortedSet<B> headSet(B toElement) {
            return new Sorted<>(this.getUnwrapped().headSet(unwrap(toElement)), getWrapper());
        }

        @Override
        public @NotNull SortedSet<B> tailSet(B fromElement) {
            return new Sorted<>(this.getUnwrapped().tailSet(unwrap(fromElement)), getWrapper());
        }

        @Override
        public B first() {
            return wrap(this.getUnwrapped().first());
        }

        @Override
        public B last() {
            return wrap(this.getUnwrapped().last());
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
        public @NotNull NavigableSet<A> getUnwrapped() {
            return (NavigableSet<A>) super.getUnwrapped();
        }

        @Override
        public @Nullable B lower(B b) {
            return wrap(this.getUnwrapped().lower(unwrap(b)));
        }

        /**
         * Same as {@link NavigableSet#lower(Object)} with any Object compatibility.
         *
         * @param o the value to match
         * @return  the greatest element less than {@code e}, or {@code null} if there is no such element
         */
        @Nullable
        public B lowerAny(Object o) {
            return wrap(this.getUnwrapped().lower(unwrap(o)));
        }

        @Override
        public @Nullable B floor(B b) {
            return wrap(this.getUnwrapped().floor(unwrap(b)));
        }

        /**
         * Same as {@link NavigableSet#floor(Object)} with any Object compatibility.
         *
         * @param o the value to match
         * @return  the greatest element less than or equal to {@code e}, or {@code null} if there is no such element
         */
        @Nullable
        public B floorAny(Object o) {
            return wrap(this.getUnwrapped().floor(unwrap(o)));
        }

        @Override
        public @Nullable B ceiling(B b) {
            return wrap(this.getUnwrapped().ceiling(unwrap(b)));
        }

        /**
         * Same as {@link NavigableSet#ceiling(Object)} with any Object compatibility.
         *
         * @param o the value to match
         * @return  the least element greater than or equal to {@code e}, or {@code null} if there is no such element
         */
        @Nullable
        public B ceilingAny(Object o) {
            return wrap(this.getUnwrapped().ceiling(unwrap(o)));
        }

        @Override
        public @Nullable B higher(B b) {
            return wrap(this.getUnwrapped().higher(unwrap(b)));
        }

        /**
         * Same as {@link NavigableSet#higher(Object)} with any Object compatibility.
         *
         * @param o the value to match
         * @return  the least element greater than {@code e}, or {@code null} if there is no such element
         */
        @Nullable
        public B higherAny(Object o) {
            return wrap(this.getUnwrapped().higher(unwrap(o)));
        }

        @Override
        public @Nullable B pollFirst() {
            return wrap(this.getUnwrapped().pollFirst());
        }

        @Override
        public @Nullable B pollLast() {
            return wrap(this.getUnwrapped().pollLast());
        }

        @Override
        public @NotNull NavigableSet<B> descendingSet() {
            return new Navigable<>(this.getUnwrapped().descendingSet(), getWrapper());
        }

        @Override
        public @NotNull Iterator<B> descendingIterator() {
            return new WrappedIterator<>(this.getUnwrapped().descendingIterator(), getWrapper());
        }

        @Override
        public @NotNull NavigableSet<B> subSet(B fromElement, boolean fromInclusive, B toElement, boolean toInclusive) {
            return new Navigable<>(this.getUnwrapped().subSet(unwrap(fromElement), fromInclusive, unwrap(toElement), toInclusive), getWrapper());
        }

        @Override
        public @NotNull NavigableSet<B> headSet(B toElement, boolean inclusive) {
            return new Navigable<>(this.getUnwrapped().headSet(unwrap(toElement), inclusive), getWrapper());
        }

        @Override
        public @NotNull NavigableSet<B> tailSet(B fromElement, boolean inclusive) {
            return new Navigable<>(this.getUnwrapped().tailSet(unwrap(fromElement), inclusive), getWrapper());
        }
    }
}
