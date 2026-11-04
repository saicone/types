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
    public @NotNull Deque<A> getUnwrapped() {
        return (Deque<A>) super.getUnwrapped();
    }

    @Override
    public void addFirst(B b) {
        getUnwrapped().addFirst(unwrap(b));
    }

    @Override
    public void addLast(B b) {
        getUnwrapped().addLast(unwrap(b));
    }

    /**
     * Same as {@link Deque#addFirst(Object)} with any Object compatibility.
     *
     * @param o the element to add
     */
    public void addAnyFirst(Object o) {
        getUnwrapped().addFirst(unwrap(o));
    }

    /**
     * Same as {@link Deque#addLast(Object)} with any Object compatibility.
     *
     * @param o the element to add
     */
    public void addAnyLast(Object o) {
        getUnwrapped().addLast(unwrap(o));
    }

    @Override
    public boolean offerFirst(B b) {
        return getUnwrapped().offerFirst(unwrap(b));
    }

    @Override
    public boolean offerLast(B b) {
        return getUnwrapped().offerLast(unwrap(b));
    }

    /**
     * Same as {@link Deque#offerFirst(Object)} with any Object compatibility.
     *
     * @param o the element to add
     * @return  {@code true} if the element was added to this deque, else {@code false}
     */
    public boolean offerAnyFirst(Object o) {
        return getUnwrapped().offerFirst(unwrap(o));
    }

    /**
     * Same as {@link Deque#offerLast(Object)} with any Object compatibility.
     *
     * @param o the element to add
     * @return  {@code true} if the element was added to this deque, else {@code false}
     */
    public boolean offerAnyLast(Object o) {
        return getUnwrapped().offerLast(unwrap(o));
    }

    @Override
    public B removeFirst() {
        return wrap(getUnwrapped().removeFirst());
    }

    @Override
    public B removeLast() {
        return wrap(getUnwrapped().removeLast());
    }

    @Override
    public B pollFirst() {
        return wrap(getUnwrapped().pollFirst());
    }

    @Override
    public B pollLast() {
        return wrap(getUnwrapped().pollLast());
    }

    @Override
    public B getFirst() {
        return wrap(getUnwrapped().getFirst());
    }

    @Override
    public B getLast() {
        return wrap(getUnwrapped().getLast());
    }

    @Override
    public B peekFirst() {
        return wrap(getUnwrapped().peekFirst());
    }

    @Override
    public B peekLast() {
        return wrap(getUnwrapped().peekLast());
    }

    @Override
    public boolean removeFirstOccurrence(Object o) {
        return getUnwrapped().removeFirstOccurrence(unwrap(o));
    }

    @Override
    public boolean removeLastOccurrence(Object o) {
        return getUnwrapped().removeLastOccurrence(unwrap(o));
    }

    @Override
    public void push(B b) {
        getUnwrapped().push(unwrap(b));
    }

    /**
     * Same as {@link Deque#push(Object)} with any Object compatibility.
     *
     * @param o the element to push
     */
    public void pushAny(Object o) {
        getUnwrapped().push(unwrap(o));
    }

    @Override
    public B pop() {
        return wrap(getUnwrapped().pop());
    }

    @Override
    public @NotNull Iterator<B> descendingIterator() {
        return new WrappedIterator<>(getUnwrapped().descendingIterator(), getWrapper());
    }
}
