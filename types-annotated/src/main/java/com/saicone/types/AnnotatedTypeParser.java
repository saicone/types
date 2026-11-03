package com.saicone.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.AnnotatedType;
import java.util.Optional;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.CompletionException;
import java.util.concurrent.Executor;
import java.util.function.Supplier;

/**
 * Represents a function that parse any annotated type of object and converts into value type.
 *
 * @author Rubenicos
 *
 * @param <T> the type result of the function
 */
@FunctionalInterface
public interface AnnotatedTypeParser<T> extends TypeParser<T> {

    @Override
    default @NotNull T parse(@NotNull Object object) throws Throwable {
        return parse(getClass().getAnnotatedSuperclass(), object);
    }

    /**
     * Parse the given object into required type.
     *
     * @param type   the annotated type that object belongs
     * @param object the object to parse
     * @return       a converted value type
     */
    @NotNull
    T parse(@NotNull AnnotatedType type, @NotNull Object object) throws Throwable;

    /**
     * Try to parse the given object into required type, if fails return null.
     *
     * @param type   the annotated type that object belongs
     * @param object the object to parse
     * @return       a converted value type or null if parsing fails
     */
    @Nullable
    default T parseOrNull(@NotNull AnnotatedType type, @Nullable Object object) {
        if (object == null) {
            return null;
        }
        try {
            return parse(type, object);
        } catch (Throwable t) {
            return null;
        }
    }

    /**
     * Try to parse the given object into required type, if fails return default value.
     *
     * @param type   the annotated type that object belongs
     * @param object the object to parse
     * @param def    the default value to return if parsing fails
     * @return       a converted value type or default value if parsing fails
     */
    @Nullable
    @Contract("_, _, !null -> !null")
    default T parseOrElse(@NotNull AnnotatedType type, @Nullable Object object, @Nullable T def) {
        final T t = parseOrNull(type, object);
        return t == null ? def : t;
    }

    /**
     * Try to parse the given object into required type, if fails return value from supplier.
     *
     * @param type     the annotated type that object belongs
     * @param object   the object to parse
     * @param supplier the supplier to get a value if parsing fails
     * @return         a converted value type or value from supplier if parsing fails
     */
    @UnknownNullability
    default T parseOrGet(@NotNull AnnotatedType type, @Nullable Object object, @NotNull Supplier<T> supplier) {
        final T t = parseOrNull(type, object);
        return t == null ? supplier.get() : t;
    }

    /**
     * Try to parse the given object into required type, if fails return an empty optional.
     *
     * @param type   the annotated type that object belongs
     * @param object the object to parse
     * @return       an optional containing a converted value type or empty if parsing fails
     */
    @NotNull
    default Optional<T> parseOrEmpty(@NotNull AnnotatedType type, @Nullable Object object) {
        return Optional.ofNullable(parseOrNull(type, object));
    }

    /**
     * Parse the given object into required type asynchronously.<br>
     * Take in count that the future can complete exceptionally if the parsing fails.
     *
     * @param type   the annotated type that object belongs
     * @param object the object to parse
     * @return       a completable future containing a converted value type
     */
    @NotNull
    default CompletableFuture<T> parseAsync(@NotNull AnnotatedType type, @NotNull Object object) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return parse(type, object);
            } catch (Throwable t) {
                throw new CompletionException(t);
            }
        });
    }

    /**
     * Parse the given object into required type asynchronously.<br>
     * Take in count that the future can complete exceptionally if the parsing fails.
     *
     * @param type     the annotated type that object belongs
     * @param object   the object to parse
     * @param executor the executor to run the parsing task
     * @return         a completable future containing a converted value type
     */
    @NotNull
    default CompletableFuture<T> parseAsync(@NotNull AnnotatedType type, @NotNull Object object, @NotNull Executor executor) {
        return CompletableFuture.supplyAsync(() -> {
            try {
                return parse(type, object);
            } catch (Throwable t) {
                throw new CompletionException(t);
            }
        }, executor);
    }
}
