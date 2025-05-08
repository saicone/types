package com.saicone.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class AnyList<E> extends AnyCollection<E> {

    @NotNull
    public static AnyList<Object> of(@NotNull List<Object> list) {
        return new AnyList<>(list, Types.OBJECT);
    }

    public AnyList(@NotNull TypeParser<E> elementParser) {
        super(new ArrayList<>(), elementParser);
    }

    public AnyList(@NotNull List<E> value, @NotNull TypeParser<E> elementParser) {
        super(value, elementParser);
    }

    // Any Collection

    @Override
    public List<E> value() {
        return (List<E>) super.value();
    }

    // Any List

    @NotNull
    public AnyObject<E> get(int index) {
        return AnyObject.of(value().get(index));
    }

    @NotNull
    public AnyObject<E> getFirst() {
        return isEmpty() ? AnyObject.empty() : get(0);
    }

    @NotNull
    public AnyObject<E> getLast() {
        return isEmpty() ? AnyObject.empty() : get(size() - 1);
    }

    @NotNull
    public AnyObject<E> set(int index, @Nullable E element) {
        return AnyObject.of(value().set(index, element));
    }

    @NotNull
    public AnyObject<E> set(int index, @NotNull AnyObject<?> element) {
        return set(index, element.isEmpty() ? null : element.as(elementParser()));
    }

    public void add(int index, @Nullable E element) {
        value().add(index, element);
    }

    public void add(int index, @NotNull AnyObject<?> element) {
        add(index, element.isEmpty() ? null : element.as(elementParser()));
    }

    @NotNull
    public AnyObject<E> removeAt(int index) {
        return AnyObject.of(value().remove(index));
    }

    public int indexOf(@NotNull E element) {
        return value().indexOf(element);
    }

    public int indexOf(int start, @NotNull E element) {
        for (int index = start; index < value().size(); index++) {
            if (Objects.equals(value().get(index), element)) {
                return index;
            }
        }
        return -1;
    }

    public int lastIndexOf(@NotNull E element) {
        return value().lastIndexOf(element);
    }

    public int lastIndexOf(int before, @NotNull E element) {
        final int last = Math.min(before + 1, value().size());
        for (int index = 0; index < last; index++) {
            if (Objects.equals(value().get(index), element)) {
                return index;
            }
        }
        return -1;
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyList<E> move(int fromIndex, int toIndex) {
        final E element = value().remove(fromIndex);
        value().set(toIndex > fromIndex ? toIndex - 1 : toIndex, element);
        return this;
    }

    @NotNull
    @Contract("_, _, _ -> this")
    public AnyList<E> move(int fromIndex, int toIndex, @NotNull UnaryOperator<E> mapper) {
        final E element = mapper.apply(value().remove(fromIndex));
        value().set(toIndex > fromIndex ? toIndex - 1 : toIndex, element);
        return this;
    }

    @NotNull
    @Contract("_, _, _ -> this")
    public AnyList<E> moveAny(int fromIndex, int toIndex, @NotNull Function<AnyObject<E>, E> mapper) {
        return move(fromIndex, toIndex, element -> mapper.apply(AnyObject.of(element)));
    }

    @NotNull
    @Contract("_ -> this")
    public AnyList<E> edit(@NotNull Consumer<AnyList<E>> consumer) {
        consumer.accept(this);
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyList<E> edit(int index, @NotNull UnaryOperator<E> mapper) {
        final E element = mapper.apply(value().get(index));
        value().set(index, element);
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyList<E> editAny(int index, @NotNull Function<AnyObject<E>, E> mapper) {
        return edit(index, element -> mapper.apply(AnyObject.of(element)));
    }
}
