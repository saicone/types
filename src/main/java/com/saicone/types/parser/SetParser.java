package com.saicone.types.parser;

import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a parser that convert objects into a set.
 *
 * @author Rubenicos
 *
 * @param <E> the type of elements in the set.
 */
public class SetParser<E> extends CollectionParser<Set<E>, E> {

    private static final Function<Integer, Set<?>> LIST_SUPPLIER = capacity -> capacity == null ? new HashSet<>() : new HashSet<>(capacity);
    private static final Map<Integer, SetParser<?>> PARSERS = new WeakHashMap<>();

    /**
     * Create a set parser using element parser.
     *
     * @param elementParser the type parser to parse elements.
     * @return              a newly generated set parser or a cached one.
     * @param <E> the type of elements in the set.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> SetParser<E> of(@NotNull TypeParser<E> elementParser) {
        final int key = elementParser.hashCode();
        SetParser<?> parser = PARSERS.get(key);
        if (parser == null) {
            parser = new SetParser(LIST_SUPPLIER, elementParser);
            PARSERS.put(key, parser);
        }
        return (SetParser<E>) parser;
    }

    /**
     * Construct a set parser with defined supplier and element parser.
     *
     * @param supplier      the supplier that generate a set.
     * @param elementParser the type parser to parse elements.
     */
    public SetParser(@NotNull Supplier<Set<E>> supplier, @NotNull TypeParser<E> elementParser) {
        super(supplier, elementParser);
    }

    /**
     * Construct a set parser with defined supplier and element parser.
     *
     * @param supplier      the function that generate a set with allocated size.
     * @param elementParser the type parser to parse elements.
     */
    public SetParser(@NotNull Function<Integer, Set<E>> supplier, @NotNull TypeParser<E> elementParser) {
        super(supplier, elementParser);
    }

    @Override
    public @NotNull Type getType() {
        return Set.class;
    }
}
