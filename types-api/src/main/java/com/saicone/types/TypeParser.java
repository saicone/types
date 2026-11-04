package com.saicone.types;

import com.saicone.types.parser.ArrayParser;
import com.saicone.types.parser.CollectionParser;
import com.saicone.types.parser.ListParser;
import com.saicone.types.parser.SetParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a function that parse any type of object and converts into value type.
 *
 * @author Rubenicos
 *
 * @param <T> the type result of the function
 */
@FunctionalInterface
public interface TypeParser<T> extends Function<Object, T> {

    /**
     * Create a type parser with associated type.
     *
     * @param type   the associated type with the parser
     * @param parser the delegate parser that process any object
     * @return       a type parser
     * @param <T>    the type result of the function
     */
    @NotNull
    static <T> TypeParser<T> of(@Nullable Type type, @NotNull TypeParser<T> parser) {
        return new TypeParser<T>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @NotNull T parse(@NotNull Object object) throws Throwable {
                return parser.parse(object);
            }
        };
    }

    /**
     * Create a type parser that accepts a {@link AnyIterable#first(Object)}.
     *
     * @param parser the delegate parser that process any first object
     * @return       a type parser that accepts the first object
     * @param <T>    the type result of the function
     */
    @NotNull
    static <T> TypeParser<T> first(@NotNull TypeParser<T> parser) {
        return first(null, parser);
    }

    /**
     * Create a type parser with associated type that accepts a {@link AnyIterable#first(Object)}.
     *
     * @param type   the associated type with the parser
     * @param parser the delegate parser that process any first object
     * @return       a type parser that accepts the first object
     * @param <T>    the type result of the function
     */
    @NotNull
    static <T> TypeParser<T> first(@Nullable Type type, @NotNull TypeParser<T> parser) {
        return new TypeParser<T>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @NotNull T parse(@NotNull Object object) throws Throwable {
                final Object first = AnyIterable.first(object);
                if (first == null) {
                    throw new IllegalArgumentException("The given object has not a first element");
                }
                return parser.parse(first);
            }
        };
    }

    /**
     * Create a type parser that accepts a {@link AnyIterable#single(Object)}.
     *
     * @param parser the delegate parser that process any single object
     * @return       a type parser that accepts a single object
     * @param <T>    the type result of the function
     */
    @NotNull
    static <T> TypeParser<T> single(@NotNull TypeParser<T> parser) {
        return single(null, parser);
    }

    /**
     * Create a type parser with associated type that accepts a {@link AnyIterable#single(Object)}.
     *
     * @param type   the associated type with the parser
     * @param parser the delegate parser that process any single object
     * @return       a type parser that accepts a single object
     * @param <T>    the type result of the function
     */
    @NotNull
    static <T> TypeParser<T> single(@Nullable Type type, @NotNull TypeParser<T> parser) {
        return new TypeParser<T>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @NotNull T parse(@NotNull Object object) throws Throwable {
                final Object single = AnyIterable.single(object);
                if (single == null) {
                    throw new IllegalArgumentException("The given object is not a single element");
                }
                return parser.parse(single);
            }
        };
    }

    /**
     * Get the associated type object with this parser.
     *
     * @return a type object if present, null otherwise
     */
    @Nullable
    default Type getType() {
        return null;
    }

    /**
     * Check if the given object is instance of parser type.
     *
     * @param object the object to check
     * @return       true if the object is an instance of this parser type
     */
    default boolean isInstance(@Nullable Object object) {
        final Type type = getType();
        if (type instanceof Class) {
            return ((Class<?>) type).isInstance(object);
        }
        throw new RuntimeException("The current type parser doesn't support instance check");
    }

    @Override
    @Contract("null -> null")
    default @UnknownNullability T apply(@Nullable Object object) {
        return parseOrNull(object);
    }

    /**
     * Parse the given object into required type.
     *
     * @param object the object to parse
     * @return       a converted value type
     * @throws Throwable if the parsing fails
     */
    @NotNull
    T parse(@NotNull Object object) throws Throwable;

    /**
     * Parse the given object into required type, if fails throw a runtime exception.
     *
     * @param object the object to parse
     * @return       a converted value type
     * @throws RuntimeException if the parsing fails
     */
    @NotNull
    default T parseOrThrow(@NotNull Object object) throws RuntimeException {
        return parseOrThrow(object, t -> new RuntimeException("Failed to parse object: " + object, t));
    }

    /**
     * Parse the given object into required type, if fails throw a custom exception.
     *
     * @param object    the object to parse
     * @param exception the supplier to create a custom exception
     * @return          a converted value type
     * @throws X if the parsing fails
     * @param <X> the type of exception to throw
     */
    @NotNull
    default <X extends Throwable> T parseOrThrow(@NotNull Object object, @NotNull Function<Throwable, ? extends X> exception) throws X {
        try {
            return parse(object);
        } catch (Throwable t) {
            throw exception.apply(t);
        }
    }

    /**
     * Try to parse the given object into required type, if fails return null.
     *
     * @param object the object to parse
     * @return       a converted value type or null if parsing fails
     */
    @Nullable
    default T parseOrNull(@Nullable Object object) {
        if (object == null) {
            return null;
        }
        try {
            return parse(object);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Try to parse the given object into required type, if fails return default value.
     *
     * @param object the object to parse
     * @param def    the default value to return if parsing fails
     * @return       a converted value type or default value if parsing fails
     */
    @Nullable
    @Contract("_, !null -> !null")
    default T parseOrElse(@Nullable Object object, @Nullable T def) {
        final T t = parseOrNull(object);
        return t == null ? def : t;
    }

    /**
     * Try to parse the given object into required type, if fails return value from supplier.
     *
     * @param object   the object to parse
     * @param supplier the supplier to get a value if parsing fails
     * @return         a converted value type or value from supplier if parsing fails
     */
    @UnknownNullability
    default T parseOrGet(@Nullable Object object, @NotNull Supplier<T> supplier) {
        final T t = parseOrNull(object);
        return t == null ? supplier.get() : t;
    }

    /**
     * Try to parse the given object into required type, if fails return an empty optional.
     *
     * @param object the object to parse
     * @return       an optional containing a converted value type or empty if parsing fails
     */
    @NotNull
    default Optional<T> parseOrEmpty(@Nullable Object object) {
        return Optional.ofNullable(parseOrNull(object));
    }

    /**
     * Parse the given object into required type asynchronously.<br>
     * Take in count that the future can complete exceptionally if the parsing fails.
     *
     * @param object the object to parse
     * @return       a completable future containing a converted value type
     */
    @NotNull
    default CompletableFuture<T> parseAsync(@NotNull Object object) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return parse(object);
            } catch (Throwable t) {
                throw new CompletionException(t);
            }
        });
    }

    /**
     * Parse the given object into required type asynchronously.<br>
     * Take in count that the future can complete exceptionally if the parsing fails.
     *
     * @param object   the object to parse
     * @param executor the executor to run the parsing task
     * @return         a completable future containing a converted value type
     */
    @NotNull
    default CompletableFuture<T> parseAsync(@NotNull Object object, @NotNull Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return parse(object);
            } catch (Throwable t) {
                throw new CompletionException(t);
            }
        }, executor);
    }

    /**
     * Create a type parsed that get the result of current parser, and then parse
     * any non-null result with the provided parser.
     *
     * @param parser the parser to apply after non-null value is return by this parser
     * @return       a type parser that first parse any object and then apply the provided parser
     * @param <E>    the type result of the function
     */
    @NotNull
    default <E> TypeParser<E> andThen(@NotNull TypeParser<E> parser) {
        return new TypeParser<E>() {
            @Override
            public @Nullable Type getType() {
                return parser.getType();
            }

            @Override
            public @NotNull E parse(@NotNull Object object) throws Throwable {
                return parser.parse(TypeParser.this.parse(object));
            }
        };
    }

    /**
     * Create a type-checked parser that get the result of current parse, and then apply
     * any non-null result with the provided function.
     *
     * @param type     the associated type with the parser
     * @param function the function to apply non-null value is return by this parser
     * @return         a type parser that first parse any object and then apply the provided function
     * @param <E>      the type result of the function
     */
    @NotNull
    default <E> TypeParser<E> andThen(@Nullable Type type, @NotNull Function<T, E> function) {
        return new TypeParser<E>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public @NotNull E parse(@NotNull Object object) throws Throwable {
                return function.apply(TypeParser.this.parse(object));
            }
        };
    }

    /**
     * Get a view of this type parser as an array parser of its type.
     *
     * @return an array parser of this type
     * @param <A> the type result of array
     */
    @NotNull
    default <A> ArrayParser<A, T> array() {
        return ArrayParser.of(this);
    }

    /**
     * Get a view of this type parser as an array parser of its type.
     *
     * @param supplier the function to create an allocated size array
     * @return         an array parser of this type
     * @param <A> the type result of array
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
     * @param type     the collection type
     * @param supplier the supplier to create a collection
     * @return         a collection parser of this type
     * @param <C> the type result of collection
     */
    @NotNull
    default <C extends Collection<T>> CollectionParser<C, T> collection(@NotNull Type type, @NotNull Supplier<C> supplier) {
        return collection(type, initialCapacity -> supplier.get());
    }

    /**
     * Get a view of this type parser as a collection parser of its type.
     *
     * @param type     the collection type
     * @param supplier the function to create an allocated size collection
     * @return         a collection parser of this type
     * @param <C> the type result of collection
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
     * @return a list parser of this type
     */
    @NotNull
    default ListParser<T> list() {
        return ListParser.of(this);
    }

    /**
     * Get a view of this type parser as a list parser of its type.
     *
     * @param supplier the supplier to create a list
     * @return         a list parser of this type
     */
    @NotNull
    default ListParser<T> list(@NotNull Supplier<List<T>> supplier) {
        return new ListParser<>(supplier, this);
    }

    /**
     * Get a view of this type parser as a list parser of its type.
     *
     * @param supplier the function to create an allocated size list
     * @return         a list parser of this type
     */
    @NotNull
    default ListParser<T> list(@NotNull Function<Integer, List<T>> supplier) {
        return new ListParser<>(supplier, this);
    }

    /**
     * Get a view of this type parser as a set parser of its type.
     *
     * @return a set parser of this type
     */
    @NotNull
    default SetParser<T> set() {
        return SetParser.of(this);
    }

    /**
     * Get a view of this type parser as a set parser of its type.
     *
     * @param supplier the supplier to create a set
     * @return         a set parser of this type
     */
    @NotNull
    default SetParser<T> set(@NotNull Supplier<Set<T>> supplier) {
        return new SetParser<>(supplier, this);
    }

    /**
     * Get a view of this type parser as a set parser of its type.
     *
     * @param supplier the function to create an allocated size set
     * @return         a set parser of this type
     */
    @NotNull
    default SetParser<T> set(@NotNull Function<Integer, Set<T>> supplier) {
        return new SetParser<>(supplier, this);
    }
}
