package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Comparator;
import java.util.PriorityQueue;
import java.util.Queue;

/**
 * Represents a {@link Queue} of type A as a {@link Queue} of type B.
 *
 * @author Rubenicos
 *
 * @param <A> the base type of object.
 * @param <B> the type of object to represent A.
 */
public class WrappedQueue<A, B> extends WrappedCollection<A, B> implements Queue<B> {

    /**
     * Constructs a queue of type A with its types represented as B.
     *
     * @param delegated the delegated queue that will be wrapped in the instance.
     * @param wrapper   the type wrapper to represent delegated queue values as B and convert it back to A.
     */
    public WrappedQueue(@NotNull Queue<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
        super(delegated, wrapper);
    }

    @Override
    public @NotNull Queue<A> getDelegated() {
        return (Queue<A>) super.getDelegated();
    }

    @Override
    public boolean offer(B b) {
        return getDelegated().offer(unwrap(b));
    }

    /**
     * Same as {@link Queue#offer(Object)} with any Object compatibility.
     *
     * @param o the element to add
     * @return  {@code true} if the element was added to this queue, else {@code false}
     */
    public boolean offerAny(Object o) {
        return getDelegated().offer(unwrap(o));
    }

    @Override
    public B remove() {
        return wrap(getDelegated().remove());
    }

    @Override
    public B poll() {
        return wrap(getDelegated().poll());
    }

    @Override
    public B element() {
        return wrap(getDelegated().element());
    }

    @Override
    public B peek() {
        return wrap(getDelegated().peek());
    }

    /**
     * Represents a {@link PriorityQueue} of type A as a {@link PriorityQueue} of type B.<br>
     * Unlike other wrapped objects, this one should be declared as a {@link Queue}.
     *
     * @author Rubenicos
     *
     * @param <A> the base type of object.
     * @param <B> the type of object to represent A.
     */
    public static class Priority<A, B> extends WrappedQueue<A, B> {

        /**
         * Constructs a priority queue of type A with its types represented as B.
         *
         * @param delegated the delegated priority queue that will be wrapped in the instance.
         * @param wrapper   the type wrapper to represent delegated priority queue values as B and convert it back to A.
         */
        public Priority(@NotNull PriorityQueue<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
            super(delegated, wrapper);
        }

        @Override
        public @NotNull PriorityQueue<A> getDelegated() {
            return (PriorityQueue<A>) super.getDelegated();
        }

        /**
         * Returns the comparator used to order the elements in this
         * queue, or {@code null} if this queue is sorted according to
         * the {@linkplain Comparable natural ordering} of its elements.
         *
         * @return the comparator used to order this queue, or
         *         {@code null} if this queue is sorted according to the
         *         natural ordering of its elements
         */
        @NotNull
        public Comparator<? super B> comparator() {
            final Comparator<? super A> comparator = getDelegated().comparator();
            return (b1, b2) -> comparator.compare(unwrap(b1), unwrap(b2));
        }
    }
}
