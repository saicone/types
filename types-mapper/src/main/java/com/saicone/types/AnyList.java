package com.saicone.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.function.Consumer;
import java.util.function.Function;
import java.util.function.UnaryOperator;

public class AnyList<E> extends AnyCollection<E> {

    private static final TypeParser<AnyList<Object>> PARSER = new TypeParser<AnyList<Object>>() {
        @Override
        public @NotNull Type getType() {
            return AnyList.class;
        }

        @Override
        @SuppressWarnings("unchecked")
        public @Nullable AnyList<Object> parse(@NotNull Object object) {
            if (object instanceof List) {
                return of((List<Object>) object);
            }
            return null;
        }
    };

    @NotNull
    public static TypeParser<AnyList<Object>> parser() {
        return PARSER;
    }

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
    public List<E> getValue() {
        return (List<E>) super.getValue();
    }

    // Any List

    @NotNull
    public AnyObject<E> get(int index) {
        return AnyObject.of(getValue().get(index));
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
        return AnyObject.of(getValue().set(index, element));
    }

    @NotNull
    public AnyObject<E> setAny(int index, @NotNull AnyObject<?> element) {
        return set(index, element.isEmpty() ? null : element.as(elementParser()));
    }

    public void add(int index, @Nullable E element) {
        getValue().add(index, element);
    }

    public void addAny(int index, @NotNull AnyObject<?> element) {
        add(index, element.isEmpty() ? null : element.as(elementParser()));
    }

    @NotNull
    public AnyObject<E> removeAt(int index) {
        return AnyObject.of(getValue().remove(index));
    }

    public int indexOf(@NotNull E element) {
        return getValue().indexOf(element);
    }

    public int indexOf(int start, @NotNull E element) {
        for (int index = start; index < getValue().size(); index++) {
            if (Objects.equals(getValue().get(index), element)) {
                return index;
            }
        }
        return -1;
    }

    public int lastIndexOf(@NotNull E element) {
        return getValue().lastIndexOf(element);
    }

    public int lastIndexOf(int before, @NotNull E element) {
        final int last = Math.min(before + 1, getValue().size());
        for (int index = 0; index < last; index++) {
            if (Objects.equals(getValue().get(index), element)) {
                return index;
            }
        }
        return -1;
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyList<E> move(int fromIndex, int toIndex) {
        final E element = getValue().remove(fromIndex);
        getValue().set(toIndex > fromIndex ? toIndex - 1 : toIndex, element);
        return this;
    }

    @NotNull
    @Contract("_, _, _ -> this")
    public AnyList<E> move(int fromIndex, int toIndex, @NotNull UnaryOperator<E> mapper) {
        final E element = mapper.apply(getValue().remove(fromIndex));
        getValue().set(toIndex > fromIndex ? toIndex - 1 : toIndex, element);
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
        final E element = mapper.apply(getValue().get(index));
        getValue().set(index, element);
        return this;
    }

    @NotNull
    @Contract("_, _ -> this")
    public AnyList<E> editAny(int index, @NotNull Function<AnyObject<E>, E> mapper) {
        return edit(index, element -> mapper.apply(AnyObject.of(element)));
    }
}
