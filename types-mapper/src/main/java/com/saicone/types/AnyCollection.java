package com.saicone.types;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.Collection;
import java.util.Iterator;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.function.BiPredicate;
import java.util.function.Predicate;

public abstract class AnyCollection<E> implements AnyStructure<Collection<E>>, Iterable<E> {

    private final Collection<E> value;
    private final TypeParser<E> elementParser;

    public AnyCollection(@NotNull Collection<E> value, @NotNull TypeParser<E> elementParser) {
        this.value = value;
        this.elementParser = elementParser;
    }

    // Iterable

    @Override
    public @NotNull Iterator<E> iterator() {
        return value.iterator();
    }

    // Any Object

    @Override
    public boolean isEmpty() {
        return value.isEmpty();
    }

    @Override
    public Collection<E> getValue() {
        return value;
    }

    // Any Structure

    @Override
    public int size() {
        return value.size();
    }

    @Override
    public void clear() {
        value.clear();
    }

    // Any Collection

    @NotNull
    public TypeParser<E> elementParser() {
        return elementParser;
    }

    public boolean contains(@NotNull E element) {
        return value.contains(element);
    }

    public boolean contains(@NotNull AnyObject<?> element) {
        return contains(element.isEmpty() ? null : element.as(elementParser));
    }

    public boolean contains(@NotNull Collection<E> collection) {
        return value.containsAll(collection);
    }

    public boolean matches(@NotNull Predicate<E> predicate) {
        for (E e : value) {
            if (predicate.test(e)) {
                return true;
            }
        }
        return false;
    }

    public boolean add(@Nullable E element) {
        return value.add(element);
    }

    public boolean add(@NotNull AnyObject<?> element) {
        return add(element.isEmpty() ? null : element.as(elementParser));
    }

    public boolean addAll(@NotNull Collection<E> collection) {
        return value.addAll(collection);
    }

    public boolean remove(@Nullable E element) {
        return value.remove(element);
    }

    public boolean remove(@NotNull AnyObject<?> element) {
        return remove(element.isEmpty() ? null : element.as(elementParser));
    }

    public boolean removeAll(@NotNull Collection<E> collection) {
        return value.removeAll(collection);
    }

    public boolean removeIf(@NotNull Predicate<E> predicate) {
        return value.removeIf(predicate);
    }

    public boolean removeIf(@NotNull BiPredicate<Integer, E> predicate) {
        final AtomicInteger index = new AtomicInteger(0);
        return value.removeIf(element -> {
            final boolean result = predicate.test(index.get(), element);
            index.incrementAndGet();
            return result;
        });
    }

    public boolean retain(@NotNull Collection<E> collection) {
        return value.retainAll(collection);
    }

    public boolean retainIf(@NotNull Predicate<E> predicate) {
        return value.removeIf(element -> !predicate.test(element));
    }

    public boolean retainIf(@NotNull BiPredicate<Integer, E> predicate) {
        final AtomicInteger index = new AtomicInteger(0);
        return value.removeIf(element -> {
            final boolean result = !predicate.test(index.get(), element);
            index.incrementAndGet();
            return result;
        });
    }

    @Override
    public String toString() {
        return value.toString();
    }

    @Override
    public final boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof AnyCollection)) return false;

        AnyCollection<?> that = (AnyCollection<?>) o;
        return value.equals(that.value);
    }

    @Override
    public int hashCode() {
        return value.hashCode();
    }
}
