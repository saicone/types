package com.saicone.types.util;

import com.saicone.types.TypeWrapper;
import org.jetbrains.annotations.NotNull;

import java.util.Deque;
import java.util.Iterator;

/**
 * Represents a {@link Deque} of type A as a {@link Deque} of type B.
 *
 * @author Rubenicos
 *
 * @param <A> the base type of object.
 * @param <B> the type of object to represent A.
 */
public class WrappedDeque<A, B> extends WrappedQueue<A, B> implements Deque<B> {

    /**
     * Constructs a deque of type A with its types represented as B.
     *
     * @param delegated the delegated deque that will be wrapped in the instance.
     * @param wrapper   the type wrapper to represent delegated deque values as B and convert it back to A.
     */
    public WrappedDeque(@NotNull Deque<A> delegated, @NotNull TypeWrapper<A, B> wrapper) {
        super(delegated, wrapper);
    }

    @Override
    public @NotNull Deque<A> getDelegated() {
        return (Deque<A>) super.getDelegated();
    }

    @Override
    public void addFirst(B b) {
        getDelegated().addFirst(unwrap(b));
    }

    @Override
    public void addLast(B b) {
        getDelegated().addLast(unwrap(b));
    }

    public void addAnyFirst(Object o) {
        getDelegated().addFirst(unwrap(o));
    }

    public void addAnyLast(Object o) {
        getDelegated().addLast(unwrap(o));
    }

    @Override
    public boolean offerFirst(B b) {
        return getDelegated().offerFirst(unwrap(b));
    }

    @Override
    public boolean offerLast(B b) {
        return getDelegated().offerLast(unwrap(b));
    }

    public boolean offerAnyFirst(Object o) {
        return getDelegated().offerFirst(unwrap(o));
    }

    public boolean offerAnyLast(Object o) {
        return getDelegated().offerLast(unwrap(o));
    }

    @Override
    public B removeFirst() {
        return wrap(getDelegated().removeFirst());
    }

    @Override
    public B removeLast() {
        return wrap(getDelegated().removeLast());
    }

    @Override
    public B pollFirst() {
        return wrap(getDelegated().pollFirst());
    }

    @Override
    public B pollLast() {
        return wrap(getDelegated().pollLast());
    }

    @Override
    public B getFirst() {
        return wrap(getDelegated().getFirst());
    }

    @Override
    public B getLast() {
        return wrap(getDelegated().getLast());
    }

    @Override
    public B peekFirst() {
        return wrap(getDelegated().peekFirst());
    }

    @Override
    public B peekLast() {
        return wrap(getDelegated().peekLast());
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return getDelegated().removeFirstOccurrence(unwrap(o));
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return getDelegated().removeLastOccurrence(unwrap(o));
    }

    @Override
    public void push(B b) {
        getDelegated().push(unwrap(b));
    }

    public void pushAny(Object o) {
        getDelegated().push(unwrap(o));
    }

    @Override
    public B pop() {
        return wrap(getDelegated().pop());
    }

    @Override
    public @NotNull Iterator<B> descendingIterator() {
        return new WrappedIterator<>(getDelegated().descendingIterator(), getWrapper());
    }
}
