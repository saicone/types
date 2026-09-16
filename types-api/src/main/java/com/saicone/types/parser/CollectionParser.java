package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.Collection;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a parser that convert objects into a collection.
 *
 * @param <T> the type result of collection.
 * @param <E> the type of elements in the collection.
 */
public abstract class CollectionParser<T extends Collection<E>, E> extends AllocParser<T> {

    private final TypeParser<E> elementParser;

    /**
     * Construct a collection parser with defined supplier and element parser.
     *
     * @param supplier      the supplier that generate a collection.
     * @param elementParser the type parser to parse elements.
     */
    public CollectionParser(@NotNull Supplier<T> supplier, @NotNull TypeParser<E> elementParser) {
        this(initialCapacity -> supplier.get(), elementParser);
    }

    /**
     * Construct a collection parser with defined supplier and element parser.
     *
     * @param supplier      the function that generate a collection with allocated size.
     * @param elementParser the type parser to parse elements.
     */
    public CollectionParser(@NotNull Function<Integer, T> supplier, @NotNull TypeParser<E> elementParser) {
        super(supplier);
        this.elementParser = elementParser;
    }

    @Override
    public abstract @NotNull Type getType();

    /**
     * Get the type associated with the elements that hold the object of this parser.
     *
     * @return a type object if present, null otherwise.
     */
    @Nullable
    public Type getElementType() {
        return elementParser.getType();
    }

    /**
     * Get the type parser to parse elements.
     *
     * @return a type parser.
     */
    @NotNull
    public TypeParser<E> getElementParser() {
        return elementParser;
    }

    @Override
    public boolean isInstance(@Nullable Object object) {
        if (super.isInstance(object)) {
            if (((Collection<?>) object).isEmpty()) {
                return true;
            }
            // Test first non-null value type
            for (Object element : ((Collection<?>) object)) {
                if (element == null) {
                    continue;
                }
                return isElementInstance(element);
            }
        }
        return false;
    }

    /**
     * Check if the given object is instance of element parser type.
     *
     * @param object the object to check.
     * @return       true if the object is an instance of element parser type.
     */
    public boolean isElementInstance(@Nullable Object object) {
        return elementParser.isInstance(object);
    }

    @Override
    public @NotNull T parse(@Nullable Object object) {
        return parseEach(object, (element, parser) -> parser.parse(element));
    }

    /**
     * Parse the provided object into required type by converting each applicable element using defined function.
     *
     * @param object   the object to parse.
     * @param function the function that convert an element and the parser for its object to required element type.
     * @return         a converted value type, empty by default.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public T parseEach(@Nullable Object object, @NotNull BiFunction<Object, TypeParser<E>, E> function) {
        if (object == null) {
            return create();
        } else if (isInstance(object)) {
            return (T) object;
        }
        final AnyIterable<Object> iterable = AnyIterable.of(object);
        final T collection = create(iterable.size());

        for (Object element : iterable) {
            collection.add(function.apply(element, elementParser));
        }
        return collection;
    }
}
