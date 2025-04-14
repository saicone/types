package com.saicone.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Array;
import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a function that parse any type of object and converts into value type.
 *
 * @author Rubenicos
 *
 * @param <T> the type result of the function.
 */
@FunctionalInterface
public interface TypeParser<T> {

    /**
     * Create a type parser with associated type.
     *
     * @param type   the associated type with the parser.
     * @param parser the delegate parser that process any object.
     * @return       a type parser.
     * @param <T>    the type result of the function.
     */
    @NotNull
    static <T> TypeParser<T> of(@Nullable Type type, @NotNull TypeParser<T> parser) {
        return new TypeParser<T>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @Nullable T parse(@NotNull Object object) {
                return parser.parse(object);
            }
        };
    }

    /**
     * Create a type parser that accepts a {@link AnyIterable#first()}.
     *
     * @param parser the delegate parser that process any first object.
     * @return       a type parser that accepts the first object.
     * @param <T>    the type result of the function.
     */
    @NotNull
    static <T> TypeParser<T> first(@NotNull TypeParser<T> parser) {
        return first(null, parser);
    }

    /**
     * Create a type parser with associated type that accepts a {@link AnyIterable#first()}.
     *
     * @param type   the associated type with the parser.
     * @param parser the delegate parser that process any first object.
     * @return       a type parser that accepts the first object.
     * @param <T>    the type result of the function.
     */
    @NotNull
    static <T> TypeParser<T> first(@Nullable Type type, @NotNull TypeParser<T> parser) {
        return new TypeParser<T>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @Nullable T parse(@NotNull Object object) {
                final Object single = AnyIterable.of(object).first();
                if (single == null) {
                    return null;
                }
                return parser.parse(single);
            }
        };
    }

    /**
     * Create a type parser that accepts a {@link AnyIterable#single()}.
     *
     * @param parser the delegate parser that process any single object.
     * @return       a type parser that accepts a single object.
     * @param <T>    the type result of the function.
     */
    @NotNull
    static <T> TypeParser<T> single(@NotNull TypeParser<T> parser) {
        return single(null, parser);
    }

    /**
     * Create a type parser with associated type that accepts a {@link AnyIterable#single()}.
     *
     * @param type   the associated type with the parser.
     * @param parser the delegate parser that process any single object.
     * @return       a type parser that accepts a single object.
     * @param <T>    the type result of the function.
     */
    @NotNull
    static <T> TypeParser<T> single(@Nullable Type type, @NotNull TypeParser<T> parser) {
        return new TypeParser<T>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @Nullable T parse(@NotNull Object object) {
                final Object single = AnyIterable.of(object).single();
                if (single == null) {
                    return null;
                }
                return parser.parse(single);
            }
        };
    }

    /**
     * Create a type parser that convert any object into required
     * collection type by parsing values with provided type parser.
     *
     * @param elementParser      the parser that accept objects.
     * @param collectionSupplier the collection supplier that creates required collection type.
     * @return                   a type parser that return a collection type.
     * @param <E>                the type of elements in the collection.
     * @param <C>                the collection type result of the function.
     */
    @NotNull
    static <E, C extends Collection<E>> TypeParser<C> collection(@NotNull TypeParser<E> elementParser, @NotNull Supplier<C> collectionSupplier) {
        return collection(null, elementParser, collectionSupplier);
    }

    /**
     * Create a type parser with associated type that convert any object into required
     * collection type by parsing values with provided type parser.
     *
     * @param type               the associated type with the parser.
     * @param elementParser      the parser that accept objects.
     * @param collectionSupplier the collection supplier that creates required collection type.
     * @return                   a type parser that return a collection type.
     * @param <E>                the type of elements in the collection.
     * @param <C>                the collection type result of the function.
     */
    @NotNull
    static <E, C extends Collection<E>> TypeParser<C> collection(@Nullable Type type, @NotNull TypeParser<E> elementParser, @NotNull Supplier<C> collectionSupplier) {
        return new TypeParser<C>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @Nullable C parse(@NotNull Object object) {
                final C collection = collectionSupplier.get();
                for (Object obj : AnyIterable.of(object)) {
                    final E element = elementParser.parseOrDefault(obj, null);
                    if (element != null) {
                        collection.add(element);
                    }
                }
                return collection;
            }
        };
    }

    /**
     * Create a type parser that convert any map into required map type
     * by parsing keys and values with provided type parsers.
     *
     * @param keyParser   the parser that accept keys.
     * @param valueParser the parser that accept values.
     * @param mapSupplier the map supplier that creates required map type.
     * @return            a type parser that return a map type.
     * @param <K>         the type of keys maintained by map.
     * @param <V>         the type of mapped values.
     * @param <M>         the map type result of the function.
     */
    @NotNull
    static <K, V, M extends Map<K, V>> TypeParser<@NotNull M> map(@NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser, @NotNull Supplier<M> mapSupplier) {
        return map(null, keyParser, valueParser, mapSupplier);
    }

    /**
     * Create a type parser with associated type that convert any map into required map type
     * by parsing keys and values with provided type parsers.
     *
     * @param type        the associated type with the parser.
     * @param keyParser   the parser that accept keys.
     * @param valueParser the parser that accept values.
     * @param mapSupplier the map supplier that creates required map type.
     * @return            a type parser that return a map type.
     * @param <K>         the type of keys maintained by map.
     * @param <V>         the type of mapped values.
     * @param <M>         the map type result of the function.
     */
    @NotNull
    static <K, V, M extends Map<K, V>> TypeParser<@NotNull M> map(@Nullable Type type, @NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser, @NotNull Supplier<M> mapSupplier) {
        return new TypeParser<M>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @Nullable M parse(@NotNull Object object) {
                final M map = mapSupplier.get();
                if (object instanceof Map) {
                    for (Map.Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
                        final K key = keyParser.parseOrDefault(entry.getKey(), null);
                        if (key == null) continue;
                        final V value = valueParser.parseOrDefault(entry.getValue(), null);
                        if (value == null) continue;
                        map.put(key, value);
                    }
                }
                return map;
            }
        };
    }

    /**
     * Get the associated type object with this parser.
     *
     * @return a type object if present, null otherwise.
     */
    @Nullable
    default Type getType() {
        return null;
    }

    /**
     * Check if the given object is instance of parser type.
     *
     * @param object the object to check.
     * @return       true if the object is an instance of this parser type.
     */
    default boolean isInstance(@Nullable Object object) {
        final Type type = getType();
        if (type instanceof Class) {
            return ((Class<?>) type).isInstance(object);
        }
        throw new RuntimeException("The current type parser doesn't support instance check");
    }

    /**
     * Parse the given object into required type.
     *
     * @param object the object to parse.
     * @return       a converted value type, null otherwise.
     */
    @Nullable
    T parse(@NotNull Object object);

    /**
     * Parse the given object into required type with a default return value if parsed value is null.
     *
     * @param object the object to parse.
     * @param def    the type object to return if parse fails.
     * @return       a converted value type, default object otherwise.
     */
    @Nullable
    @Contract("_, !null -> !null")
    default T parse(@Nullable Object object, @Nullable T def) {
        if (object == null) {
            return def;
        }
        if (object instanceof AnyObject) {
            return parse(((AnyObject<?>) object).getValue());
        }
        final T obj = parse(object);
        return obj != null ? obj : def;
    }

    /**
     * Parse the given object into required type with a default return value if parsed value is null.<br>
     * This method also checks if the object is instance of given class type.
     *
     * @param type   the class type.
     * @param object the object to parse.
     * @param def    the type object to return if parse fails.
     * @return       a converted value type, default object otherwise.
     */
    @Nullable
    @Contract("_, _, !null -> !null")
    @SuppressWarnings("unchecked")
    default T parse(@NotNull Class<T> type, @Nullable Object object, @Nullable T def) {
        if (type.isInstance(object)) {
            return (T) object;
        }
        return parse(object, def);
    }

    /**
     * Parse the given object into required type with a default return value
     * if parsed value is null or the conversion generates an exception.
     *
     * @param object the object to parse.
     * @param def    the type object to return if parse fails.
     * @return       a converted value type, default object otherwise.
     */
    @Nullable
    @Contract("_, !null -> !null")
    default T parseOrDefault(@Nullable Object object, @Nullable T def) {
        try {
            return parse(object, def);
        } catch (Throwable t) {
            return def;
        }
    }

    /**
     * Parse the given object into required type with a default return value
     * if parsed value is null or the conversion generates an exception.<br>
     * This method also checks if the object is instance of given class type.
     *
     * @param type   the class type.
     * @param object the object to parse.
     * @param def    the type object to return if parse fails.
     * @return       a converted value type, default object otherwise.
     */
    @Nullable
    @Contract("_, _, !null -> !null")
    @SuppressWarnings("unchecked")
    default T parseOrDefault(@NotNull Class<T> type, @Nullable Object object, @Nullable T def) {
        if (type.isInstance(object)) {
            return (T) object;
        }
        return parseOrDefault(object, def);
    }

    /**
     * Parse the given object into provided array type value.
     *
     * @param array  the array to add values.
     * @param object the object to parse.
     * @return       a type array.
     * @param <A>    the array type.
     */
    @NotNull
    default <A> A parseArray(@NotNull A array, @Nullable Object object) {
        return parseArray(array, object, null);
    }

    /**
     * Parse the given object into provided array type value.
     *
     * @param array  the array to add values.
     * @param object the object to parse.
     * @param def    the type object to fill failed parsed values.
     * @return       a type array.
     * @param <A>    the array type.
     */
    @NotNull
    @SuppressWarnings("all")
    default <A> A parseArray(@NotNull A array, @Nullable Object object, @Nullable T def) {
        if (object == null) {
            return array;
        }
        final Class<?> component = array.getClass().getComponentType();
        Object finalArray = array;
        final int size = Array.getLength(finalArray);
        int index = 0;
        for (Object obj : AnyIterable.of(object)) {
            final Object o = parseOrDefault(obj, def);
            if (o != null) {
                if (index >= size) {
                    Object arrayCopy = Array.newInstance(component, index + 1);
                    System.arraycopy(finalArray, 0, arrayCopy, 0, index);
                    finalArray = arrayCopy;
                }
                Array.set(finalArray, index, o);
                index++;
            }
        }
        return (A) finalArray;
    }

    /**
     * Create a type parsed that get the result of current parser, and then parse
     * any non-null result with the provided parser.
     *
     * @param parser the parser to apply after non-null value is return by this parser.
     * @return       a type parser that first parse any object and then apply the provided parser.
     * @param <E>    the type result of the function.
     */
    @NotNull
    default <E> TypeParser<E> andThen(@NotNull TypeParser<E> parser) {
        return new TypeParser<E>() {
            @Override
            public @Nullable Type getType() {
                return parser.getType();
            }

            @Override
            public @Nullable E parse(@NotNull Object object) {
                final T t = TypeParser.this.parse(object);
                return t == null ? null : parser.parse(t);
            }
        };
    }

    /**
     * Create a type-checked parser that get the result of current parse, and then apply
     * any non-null result with the provided function.
     *
     * @param type     the associated type with the parser.
     * @param function the function to apply non-null value is return by this parser.
     * @return         a type parser that first parse any object and then apply the provided function.
     * @param <E>      the type result of the function.
     */
    @NotNull
    default <E> TypeParser<E> andThen(@Nullable Type type, @NotNull Function<T, E> function) {
        return new TypeParser<E>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @Nullable E parse(@NotNull Object object) {
                final T t = TypeParser.this.parse(object);
                return t == null ? null : function.apply(t);
            }
        };
    }

    /**
     * Parse the given object into required type and save into Optional object,
     * an empty optional is return when parse fails or throw exception.
     *
     * @param object the object to parse.
     * @return       an Optional with a present value if the parsed value is non-null, otherwise an empty Optional.
     */
    @NotNull
    default Optional<T> optional(@Nullable Object object) {
        return Optional.ofNullable(object == null ? null : parseOrDefault(object, null));
    }

    /**
     * Parse the given object into required type and save into Optional object,
     * an empty optional is return when parse fails or throw exception.<br>
     * This method also checks if the object is instance of given class type.
     *
     * @param type   the class type.
     * @param object the object to parse.
     * @return       an Optional with a present value if the parsed value is non-null, otherwise an empty Optional.
     */
    @NotNull
    default Optional<T> optional(@NotNull Class<T> type, @Nullable Object object) {
        return Optional.ofNullable(object == null ? null : parseOrDefault(type, object, null));
    }

    /**
     * Convert parse function into a CompletableFuture with the given object to parse.<br>
     * The return object also can throw NullPointerException if parser fails.
     *
     * @param object the object to parse.
     * @return       the CompletableFuture that return a non-null parsed object.
     */
    @NotNull
    default CompletableFuture<T> completableFuture(@Nullable Object object) {
        return CompletableFuture.completedFuture(object).thenApply(o -> {
           final T t = parse(o, null);
            if (t != null) {
                return t;
            }
            throw new NullPointerException("Cannot parse provided object into " + getType());
        });
    }

    /**
     * Convert parse function into a CompletableFuture that is asynchronously completed with the given object to parse.<br>
     * The return object also can throw NullPointerException if parser fails.
     *
     * @see CompletableFuture#supplyAsync(Supplier)
     *
     * @param object the object to parse.
     * @return       the CompletableFuture that return a non-null parsed object.
     */
    @NotNull
    default CompletableFuture<T> supplyAsync(@Nullable Object object) {
        return CompletableFuture.supplyAsync(() -> {
            final T t = parse(object, null);
            if (t != null) {
                return t;
            }
            throw new NullPointerException("Cannot parse provided object into " + getType());
        });
    }

    /**
     * Convert parse function into a CompletableFuture that is asynchronously completed with the given object to parse.<br>
     * The return object also can throw NullPointerException if parser fails.
     *
     * @see CompletableFuture#supplyAsync(Supplier, Executor)
     *
     * @param object   the object to parse.
     * @param executor the executor to use for asynchronous execution.
     * @return         the CompletableFuture that return a non-null parsed object.
     */
    @NotNull
    default CompletableFuture<T> supplyAsync(@Nullable Object object, @NotNull Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            final T t = parse(object, null);
            if (t != null) {
                return t;
            }
            throw new NullPointerException("Cannot parse provided object into " + getType());
        }, executor);
    }

    /**
     * Parse the given object into collection parameter.<br>
     * This method iterates into any type of object to add parsed values into collection.
     *
     * @param collection the collection to add parsed values.
     * @param object     the object to parse.
     * @return           a type collection.
     * @param <C>        the collection type to return.
     */
    @NotNull
    default <C extends Collection<T>> C collection(@NotNull C collection, @Nullable Object object) {
        return collection(collection, object, null);
    }

    /**
     * Parse the given object into collection parameter.<br>
     * This method iterate into any type of object to add parsed values into collection.
     *
     * @param collection the collection to add parsed values.
     * @param object     the object to parse.
     * @param def        the type object to fill failed parsed values.
     * @return           a type collection.
     * @param <C>        the collection type to return.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default <C extends Collection<T>> C collection(@NotNull C collection, @Nullable Object object, @Nullable T def) {
        if (object == null) {
            return collection;
        }

        // Check if the current value is the same type of collection
        if (collection.getClass().isInstance(object)) {
            if (((Collection<?>) object).isEmpty()) {
                return collection;
            }
            // Test first non-null value type
            for (Object obj : ((Collection<?>) object)) {
                if (obj == null) {
                    continue;
                }
                if (obj.equals(parseOrDefault(obj, def))) {
                    try {
                        return (C) object;
                    } catch (ClassCastException ignored) { }
                }
                break;
            }
        }
        for (Object obj : AnyIterable.of(object)) {
            final T result = parseOrDefault(obj, def);
            if (result != null) {
                collection.add(result);
            }
        }
        return collection;
    }

    /**
     * Parse the given object into list of type value.
     *
     * @param object the object to parse.
     * @return       a type list.
     */
    @NotNull
    default List<T> list(@Nullable Object object) {
        return list(object, null);
    }

    /**
     * Parse the given object into list of type value.
     *
     * @param object the object to parse.
     * @param def    the type object to fill failed parsed values.
     * @return       a type list.
     */
    @NotNull
    default List<T> list(@Nullable Object object, @Nullable T def) {
        return collection(new ArrayList<>(), object, def);
    }

    /**
     * Parse the given object into set of type value.
     *
     * @param object the object to parse.
     * @return       a type set.
     */
    @NotNull
    default Set<T> set(@Nullable Object object) {
        return set(object, null);
    }

    /**
     * Parse the given object into set of type value.
     *
     * @param object the object to parse.
     * @param def    the type object to fill failed parsed values.
     * @return       a type set.
     */
    @NotNull
    default Set<T> set(@Nullable Object object, @Nullable T def) {
        return collection(new HashSet<>(), object, def);
    }

    /**
     * Parse the given object into array type value.
     *
     * @param object the object to parse.
     * @return       a type array.
     * @param <A>    the array type.
     */
    @NotNull
    default <A> A array(@Nullable Object object) {
        return array(object, null);
    }

    /**
     * Parse the given object into array type value.
     *
     * @param object the object to parse.
     * @param def    the type object to fill failed parsed values.
     * @return       a type array.
     * @param <A>    the array type.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default <A> A array(@Nullable Object object, @Nullable T def) {
        final Type type = getType();
        if (type instanceof Class) {
            return (A) parseArray(Array.newInstance((Class<?>) type, 0), object, def);
        }
        throw new RuntimeException("The current type parser doesn't support array creation");
    }
}
