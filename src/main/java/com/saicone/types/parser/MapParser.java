package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;
import java.util.WeakHashMap;
import java.util.function.BiFunction;
import java.util.function.Function;
import java.util.function.Supplier;

/**
 * Represents a parser that convert objects into a map.
 *
 * @author Rubenicos
 *
 * @param <K> the type of keys in the map.
 * @param <V> the type of values in the map.
 */
public class MapParser<K, V> extends AllocParser<Map<K, V>> {

    private static final Function<Integer, Map<?, ?>> MAP_SUPPLIER = capacity -> capacity == null ? new HashMap<>() : new HashMap<>(capacity);
    private static final Map<Integer, MapParser<?, ?>> PARSERS = new WeakHashMap<>();

    /**
     * Create a map parser using key and value parser.
     *
     * @param keyParser   the type parser to parse keys.
     * @param valueParser the type parser to parse values.
     * @return            a newly generated map parser or a cached one.
     * @param <K> the type of keys in the map.
     * @param <V> the type of values in the map.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <K, V> MapParser<K, V> of(@NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser) {
        final int key = Objects.hash(keyParser, valueParser);
        MapParser<?, ?> parser = PARSERS.get(key);
        if (parser == null) {
            parser = new MapParser(MAP_SUPPLIER, keyParser, valueParser);
            PARSERS.put(key, parser);
        }
        return (MapParser<K, V>) parser;
    }

    private final TypeParser<K> keyParser;
    private final TypeParser<V> valueParser;

    /**
     * Constructs a map parser with defined parameters.
     *
     * @param supplier    the supplier that generate a map.
     * @param keyParser   the type parser to parse keys.
     * @param valueParser the type parser to parse values.
     */
    public MapParser(@NotNull Supplier<Map<K, V>> supplier, @NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser) {
        this(capacity -> supplier.get(), keyParser, valueParser);
    }

    /**
     * Constructs a map parser with defined parameters.
     *
     * @param supplier    the function that generate a map with allocated size.
     * @param keyParser   the type parser to parse keys.
     * @param valueParser the type parser to parse values.
     */
    public MapParser(@NotNull Function<Integer, Map<K, V>> supplier, @NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser) {
        super(supplier);
        this.keyParser = keyParser;
        this.valueParser = valueParser;
    }

    @Override
    public @Nullable Type getType() {
        return Map.class;
    }

    /**
     * Get the type associated with the keys that hold the map of this parser.
     *
     * @return a type object if present, null otherwise.
     */
    @Nullable
    public Type getKeyType() {
        return keyParser.getType();
    }

    /**
     * Get the type associated with the values that hold the map of this parser.
     *
     * @return a type object if present, null otherwise.
     */
    @Nullable
    public Type getValueType() {
        return valueParser.getType();
    }

    /**
     * Get the type parser to parse keys.
     *
     * @return a type parser.
     */
    @NotNull
    public TypeParser<K> getKeyParser() {
        return keyParser;
    }

    /**
     * Get the type parser to parse values.
     *
     * @return a type parser.
     */
    @NotNull
    public TypeParser<V> getValueParser() {
        return valueParser;
    }

    @Override
    public boolean isInstance(@Nullable Object object) {
        if (super.isInstance(object)) {
            if (((Map<?, ?>) object).isEmpty()) {
                return true;
            }
            // Test first non-null entry
            for (Map.Entry<?, ?> entry : ((Map<?, ?>) object).entrySet()) {
                if (entry.getKey() == null || entry.getValue() == null) {
                    continue;
                }
                return isKeyInstance(entry.getKey()) && isValueInstance(entry.getValue());
            }
        }
        return false;
    }

    /**
     * Check if the given object is instance of key parser type.
     *
     * @param object the object to check.
     * @return       true if the object is an instance of key parser type.
     */
    public boolean isKeyInstance(@Nullable Object object) {
        return keyParser.isInstance(object);
    }

    /**
     * Check if the given object is instance of value parser type.
     *
     * @param object the object to check.
     * @return       true if the object is an instance of value parser type.
     */
    public boolean isValueInstance(@Nullable Object object) {
        return valueParser.isInstance(object);
    }

    @Override
    public @NotNull Map<K, V> parse(@Nullable Object object) {
        return parseEach(object, (key, parser) -> parser.parse(key), (value, parser) -> parser.parse(value));
    }

    /**
     * Parse the provided object into required type by converting each applicable entry using defined function.
     *
     * @param object        the object to parse.
     * @param keyFunction   the function that convert a key and the parser for its object to required component type.
     * @param valueFunction the function that convert a value and the parser for its object to required component type.
     * @return              a converted value type, empty by default.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public Map<K, V> parseEach(@Nullable Object object, @NotNull BiFunction<Object, TypeParser<K>, K> keyFunction, @NotNull BiFunction<Object, TypeParser<V>, V> valueFunction) {
        if (object == null) {
            return create();
        } else if (isInstance(object)) {
            return (Map<K, V>) object;
        }
        final AnyIterable<Object> iterable = AnyIterable.of(object);
        final Map<K, V> map = create(iterable.size());

        Object key = null;
        for (Object element : iterable) {
            if (key != null) {
                if (element instanceof Map.Entry) {
                    map.put(keyFunction.apply(key, keyParser), null);
                } else {
                    map.put(keyFunction.apply(key, keyParser), valueFunction.apply(element, valueParser));
                    continue;
                }
            }
            if (element instanceof Map.Entry) {
                map.put(keyFunction.apply(((Map.Entry<?, ?>) element).getKey(), keyParser), valueFunction.apply(((Map.Entry<?, ?>) element).getValue(), valueParser));
            } else {
                key = element;
            }
        }
        if (key != null) {
            map.put(keyFunction.apply(key, keyParser), null);
        }
        return map;
    }
}
