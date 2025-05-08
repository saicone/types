package com.saicone.types;

import com.saicone.types.parser.ArrayParser;
import com.saicone.types.parser.CollectionParser;
import com.saicone.types.parser.ListParser;
import com.saicone.types.parser.SetParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
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
     * Get a view of this type parser as an array parser of its type.
     *
     * @return an array parser of this type.
     * @param <A> the type result of array.
     */
    @NotNull
    default <A> ArrayParser<A, T> array() {
        return ArrayParser.of(this);
    }

    /**
     * Get a view of this type parser as an array parser of its type.
     *
     * @param supplier the function to create an allocated size array.
     * @return         an array parser of this type.
     * @param <A> the type result of array.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default <A> ArrayParser<A, T> array(@NotNull Function<Integer, A> supplier) {
        if (getType() == null) {
            throw new IllegalStateException("The current parser doesn't contains type");
        }
        return new ArrayParser<>(supplier, (Class<T>) getType(), this);
    }

    /**
     * Get a view of this type parser as a collection parser of its type.
     *
     * @param type     the collection type.
     * @param supplier the supplier to create a collection.
     * @return         a collection parser of this type.
     * @param <C> the type result of collection.
     */
    @NotNull
    default <C extends Collection<T>> CollectionParser<C, T> collection(@NotNull Type type, @NotNull Supplier<C> supplier) {
        return collection(type, initialCapacity -> supplier.get());
    }

    /**
     * Get a view of this type parser as a collection parser of its type.
     *
     * @param type     the collection type.
     * @param supplier the function to create an allocated size collection.
     * @return         a collection parser of this type.
     * @param <C> the type result of collection.
     */
    @NotNull
    default <C extends Collection<T>> CollectionParser<C, T> collection(@NotNull Type type, @NotNull Function<Integer, C> supplier) {
        return new CollectionParser<C, T>(supplier, this) {
            @Override
            public @NotNull Type getType() {
                return type;
            }
        };
    }

    /**
     * Get a view of this type parser as a list parser of its type.
     *
     * @return a list parser of this type.
     */
    @NotNull
    default ListParser<T> list() {
        return ListParser.of(this);
    }

    /**
     * Get a view of this type parser as a list parser of its type.
     *
     * @param supplier the supplier to create a list.
     * @return         a list parser of this type.
     */
    @NotNull
    default ListParser<T> list(@NotNull Supplier<List<T>> supplier) {
        return new ListParser<>(supplier, this);
    }

    /**
     * Get a view of this type parser as a list parser of its type.
     *
     * @param supplier the function to create an allocated size list.
     * @return         a list parser of this type.
     */
    @NotNull
    default ListParser<T> list(@NotNull Function<Integer, List<T>> supplier) {
        return new ListParser<>(supplier, this);
    }

    /**
     * Get a view of this type parser as a set parser of its type.
     *
     * @return a set parser of this type.
     */
    @NotNull
    default SetParser<T> set() {
        return SetParser.of(this);
    }

    /**
     * Get a view of this type parser as a set parser of its type.
     *
     * @param supplier the supplier to create a set.
     * @return         a set parser of this type.
     */
    @NotNull
    default SetParser<T> set(@NotNull Supplier<Set<T>> supplier) {
        return new SetParser<>(supplier, this);
    }

    /**
     * Get a view of this type parser as a set parser of its type.
     *
     * @param supplier the function to create an allocated size set.
     * @return         a set parser of this type.
     */
    @NotNull
    default SetParser<T> set(@NotNull Function<Integer, Set<T>> supplier) {
        return new SetParser<>(supplier, this);
    }
}
