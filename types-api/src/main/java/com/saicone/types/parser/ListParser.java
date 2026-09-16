package com.saicone.types.parser;

import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.WeakHashMap;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a parser that convert objects into a list.
 *
 * @author Rubenicos
 *
 * @param <E> the type of elements in the list.
 */
public class ListParser<E> extends CollectionParser<List<E>, E> {

    private static final Function<Integer, List<?>> LIST_SUPPLIER = capacity -> capacity == null ? new ArrayList<>() : new ArrayList<>(capacity);
    private static final Map<Integer, ListParser<?>> PARSERS = new WeakHashMap<>();

    /**
     * Create a list parser using element parser.
     *
     * @param elementParser the type parser to parse elements.
     * @return              a newly generated list parser or a cached one.
     * @param <E> the type of elements in the list.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <E> ListParser<E> of(@NotNull TypeParser<E> elementParser) {
        final int key = elementParser.hashCode();
        ListParser<?> parser = PARSERS.get(key);
        if (parser == null) {
            parser = new ListParser(LIST_SUPPLIER, elementParser);
            PARSERS.put(key, parser);
        }
        return (ListParser<E>) parser;
    }

    /**
     * Construct a list parser with defined supplier and element parser.
     *
     * @param supplier      the supplier that generate a list.
     * @param elementParser the type parser to parse elements.
     */
    public ListParser(@NotNull Supplier<List<E>> supplier, @NotNull TypeParser<E> elementParser) {
        super(supplier, elementParser);
    }

    /**
     * Construct a list parser with defined supplier and element parser.
     *
     * @param supplier      the function that generate a list with allocated size.
     * @param elementParser the type parser to parse elements.
     */
    public ListParser(@NotNull Function<Integer, List<E>> supplier, @NotNull TypeParser<E> elementParser) {
        super(supplier, elementParser);
    }

    @Override
    public @NotNull Type getType() {
        return List.class;
    }
}
