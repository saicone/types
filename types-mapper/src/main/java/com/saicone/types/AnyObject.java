package com.saicone.types;

import com.saicone.types.parser.ArrayParser;
import com.saicone.types.parser.EnumParser;
import com.saicone.types.parser.ListParser;
import com.saicone.types.parser.MapParser;
import com.saicone.types.parser.SetParser;
import org.jetbrains.annotations.ApiStatus;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Supplier;

/**
 * Represents a value that can be converted into different types of objects.
 *
 * @author Rubenicos
 *
 * @param <T> the value type itself
 */
@FunctionalInterface
@SuppressWarnings("unused")
public interface AnyObject<T> {

    @NotNull
    static TypeParser<AnyObject<Object>> parser() {
        return Registry.PARSER;
    }

    @NotNull
    @SuppressWarnings("unchecked")
    static <T> AnyObject<T> empty() {
        return (AnyObject<T>) Registry.EMPTY;
    }

    /**
     * Create a wrapped value type from given object type.
     *
     * @param value the object type to wrap
     * @return      a value type instance
     * @param <T>   the value type itself
     */
    @NotNull
    @SuppressWarnings("unchecked")
    static <T> AnyObject<T> of(T value) {
        if (value == null) {
            return empty();
        } else if (value instanceof Set) {
            return (AnyObject<T>) AnySet.of((Set<Object>) value);
        } else if (value instanceof List) {
            return (AnyObject<T>) AnyList.of((List<Object>) value);
        } else if (value instanceof Map) {
            return (AnyObject<T>) AnyMap.of((Map<Object, Object>) value);
        }
        return () -> value;
    }

    /**
     * Check if the value is null or empty, depending on the value type.
     *
     * @return true if the value type is null or empty, false otherwise
     */
    default boolean isEmpty() {
        return false;
    }

    /**
     * Get the value type that implements the interface.
     *
     * @return the value type itself
     */
    @UnknownNullability
    T getValue();

    /**
     * Convert this object into given class type.
     *
     * @param type the class type
     * @return     a converted value type
     * @param <E>  the type result
     */
    @NotNull
    default <E> E as(@NotNull Class<E> type) {
        return as(type, TypeParser::parseOrThrow);
    }

    /**
     * Convert this object into given class type.
     *
     * @param type     the class type
     * @param function the function to run with the object and the type parser
     * @return         a converted value type
     * @throws X if the parsing fails
     * @param <E> the type result of the parser
     * @param <R> the type result of the function
     * @param <X> the type of exception that can be thrown
     */
    @UnknownNullability
    default <E, R, X extends Throwable> R as(@NotNull Class<E> type, @NotNull TypeParserFunction<TypeParser<E>, R, X> function) throws X {
        return Types.run(type, getValue(), function);
    }

    /**
     * Convert this object into given class type.
     *
     * @param parser the parser to apply this object into
     * @return       a converted value type
     * @param <E>    the type result of the parser
     */
    @NotNull
    default <E> E as(@NotNull TypeParser<E> parser) {
        return parser.parseOrThrow(getValue());
    }

    /**
     * Convert this object into given class type.
     *
     * @param parser   the parser to apply this object into
     * @param function the function to run with the object and the type parser
     * @return         a converted value type
     * @throws X if the parsing fails
     * @param <E> the type result of the parser
     * @param <R> the type result of the function
     * @param <X> the type of exception that can be thrown
     */
    @UnknownNullability
    default <E, R, X extends Throwable> R as(@NotNull TypeParser<E> parser, @NotNull TypeParserFunction<TypeParser<E>, R, X> function) throws X {
        return function.apply(parser, getValue());
    }

    /**
     * Convert this object into given enum type.
     *
     * @param type the type of enum to convert
     * @return     a type enum representation
     * @param <E> the type of provided enum class
     */
    @NotNull
    default <E extends Enum<E>> E asEnum(@NotNull Class<E> type) {
        return asEnum(type, TypeParser::parseOrThrow);
    }

    /**
     * Convert this object into given enum type.
     *
     * @param type     the type of enum to convert
     * @param function the function to run with the object and the type parser
     * @return         a type enum representation
     * @throws X if the parsing fails
     * @param <E> the type of provided enum class
     * @param <X> the type of exception that can be thrown
     */
    @UnknownNullability
    default <E extends Enum<E>, X extends Throwable> E asEnum(@NotNull Class<E> type, @NotNull TypeParserFunction<EnumParser<E>, E, X> function) throws X {
        return function.apply(EnumParser.of(type), getValue());
    }

    /**
     * Convert this object into array type with component type, allowing primitive types.
     *
     * @param component the component type of array
     * @return          a type array
     * @param <C> the component type
     * @param <A> the array type
     */
    @NotNull
    default <C, A> A asArray(@NotNull Class<C> component) {
        return asArray(component, TypeParser::parseOrThrow);
    }

    /**
     * Convert this object into array type with component type, allowing primitive types.
     *
     * @param component the component type of array
     * @param function  the function to run with the object and the type parser
     * @return          a type array
     * @throws X if the parsing fails
     * @param <C> the component type
     * @param <A> the array type
     * @param <X> the type of exception that can be thrown
     */
    @UnknownNullability
    default <C, A, X extends Throwable> A asArray(@NotNull Class<C> component, @NotNull TypeParserFunction<ArrayParser<A, C>, A, X> function) throws X {
        return function.apply(ArrayParser.of(component), getValue());
    }

    /**
     * Convert this object into array type with component type.
     *
     * @param parser the parser that objects into component type
     * @return       a type array
     * @param <C> the component type
     * @param <A> the array type
     */
    @NotNull
    default <C, A> A asArray(@NotNull TypeParser<C> parser) {
        return asArray(parser, TypeParser::parseOrThrow);
    }

    /**
     * Convert this object into array type with component type.
     *
     * @param parser   the parser that objects into component type
     * @param function the function to run with the object and the type parser
     * @return         a type array
     * @throws X if the parsing fails
     * @param <C> the component type
     * @param <A> the array type
     * @param <X> the type of exception that can be thrown
     */
    @UnknownNullability
    default <C, A, X extends Throwable> A asArray(@NotNull TypeParser<C> parser, @NotNull TypeParserFunction<ArrayParser<A, C>, A, X> function) throws X {
        return function.apply(ArrayParser.of(parser), getValue());
    }

    /**
     * Convert this object into a List of elements with the given type parser.
     *
     * @param parser the parser to apply this object into
     * @return       a type list containing the parsed values
     * @param <E>    the type by parser
     */
    @NotNull
    default <E> List<E> asList(@NotNull TypeParser<E> parser) {
        return asList(parser, TypeParser::parseOrThrow);
    }

    /**
     * Convert this object into a List of elements with the given type parser.
     *
     * @param parser   the parser to apply this object into
     * @param function the function to run with the object and the type parser
     * @return         a type list containing the parsed values
     * @throws X if the parsing fails
     * @param <E> the type by parser
     * @param <L> the list type result of the function
     * @param <X> the type of exception that can be thrown
     */
    @UnknownNullability
    default <E, L extends List<E>, X extends Throwable> L asList(@NotNull TypeParser<E> parser, @NotNull TypeParserFunction<ListParser<E>, L, X> function) throws X {
        return function.apply(parser.list(), getValue());
    }

    /**
     * Convert this object into a Set of elements with the given type parser.
     *
     * @param parser the parser to apply this object into
     * @return       a type set containing the parsed values
     * @param <E>    the type by parser
     */
    @NotNull
    default <E> Set<E> asSet(@NotNull TypeParser<E> parser) {
        return asSet(parser, TypeParser::parseOrThrow);
    }

    /**
     * Convert this object into a Set of elements with the given type parser.
     *
     * @param parser   the parser to apply this object into
     * @param function the function to run with the object and the type parser
     * @return         a type set containing the parsed values
     * @throws X if the parsing fails
     * @param <E> the type by parser
     * @param <S> the set type result of the function
     * @param <X> the type of exception that can be thrown
     */
    @UnknownNullability
    default <E, S extends Set<E>, X extends Throwable> S asSet(@NotNull TypeParser<E> parser, @NotNull TypeParserFunction<SetParser<E>, S, X> function) throws X {
        return function.apply(parser.set(), getValue());
    }

    /**
     * Convert this object into map type with the given key and value type parsers.
     *
     * @param keyParser   the parser that accept keys
     * @param valueParser the parser that accept values
     * @return            a map type
     * @param <K> the type of keys maintained by map
     * @param <V> the type of mapped values
     * @param <M> the map type result of the function
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default <K, V, M extends Map<K, V>> M asMap(@NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser) {
        return (M) asMap(keyParser, valueParser, TypeParser::parseOrThrow);
    }

    /**
     * Convert this object into map type with the given key and value type parsers.
     *
     * @param keyParser   the parser that accept keys
     * @param valueParser the parser that accept values
     * @param function    the function to run with the object and the type parser
     * @return            a map type
     * @throws X if the parsing fails
     * @param <K> the type of keys maintained by map
     * @param <V> the type of mapped values
     * @param <M> the map type result of the function
     * @param <X> the type of exception that can be thrown
     */
    @UnknownNullability
    default <K, V, M extends Map<K, V>, X extends Throwable> M asMap(@NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser, @NotNull TypeParserFunction<MapParser<K, V>, M, X> function) throws X {
        return function.apply(MapParser.of(keyParser, valueParser), getValue());
    }

    /**
     * Convert this object into a string.
     *
     * @return a string
     */
    @NotNull
    default String asString() {
        return as(Types.STRING);
    }

    /**
     * Convert this object into a string if possible, otherwise return null.
     *
     * @return a string, null if conversion fails
     */
    @Nullable
    default String asStringOrNull() {
        return as(Types.STRING, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a string if possible, otherwise return an empty optional.
     *
     * @return a string, empty optional if conversion fails
     */
    @NotNull
    default Optional<String> asStringOrEmpty() {
        return as(Types.STRING, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a string if possible, otherwise return the default string.
     *
     * @param def the default string
     * @return    a string, default string if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default String asStringOrElse(@Nullable String def) {
        return as(Types.STRING, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a string if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default string
     * @return         a string, default string if conversion fails
     */
    @UnknownNullability
    default String asStringOrGet(@NotNull Supplier<String> supplier) {
        return as(Types.STRING, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a character.
     *
     * @return a character
     */
    @NotNull
    default Character asChar() {
        return as(Types.CHAR);
    }

    /**
     * Convert this object into a character if possible, otherwise return null.
     *
     * @return a character, null if conversion fails
     */
    @Nullable
    default Character asCharOrNull() {
        return as(Types.CHAR, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a character if possible, otherwise return an empty optional.
     *
     * @return a character, empty optional if conversion fails
     */
    @NotNull
    default Optional<Character> asCharOrEmpty() {
        return as(Types.CHAR, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a character if possible, otherwise return the default character.
     *
     * @param def the default character
     * @return    a character, default character if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default Character asCharOrElse(@Nullable Character def) {
        return as(Types.CHAR, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a character if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default character
     * @return         a character, default character if conversion fails
     */
    @UnknownNullability
    default Character asCharOrGet(@NotNull Supplier<Character> supplier) {
        return as(Types.CHAR, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a boolean.
     *
     * @return a boolean
     */
    @NotNull
    default Boolean asBoolean() {
        return as(Types.BOOLEAN);
    }

    /**
     * Convert this object into a boolean if possible, otherwise return null.
     *
     * @return a boolean, null if conversion fails
     */
    @Nullable
    default Boolean asBooleanOrNull() {
        return as(Types.BOOLEAN, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a boolean if possible, otherwise return an empty optional.
     *
     * @return a boolean, empty optional if conversion fails
     */
    @NotNull
    default Optional<Boolean> asBooleanOrEmpty() {
        return as(Types.BOOLEAN, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a boolean if possible, otherwise return the default boolean.
     *
     * @param def the default boolean
     * @return    a boolean, default boolean if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default Boolean asBooleanOrElse(@Nullable Boolean def) {
        return as(Types.BOOLEAN, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a boolean if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default boolean
     * @return         a boolean, default boolean if conversion fails
     */
    @UnknownNullability
    default Boolean asBooleanOrGet(@NotNull Supplier<Boolean> supplier) {
        return as(Types.BOOLEAN, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a number.
     *
     * @return a number
     */
    @NotNull
    default Number asNumber() {
        return as(Types.NUMBER);
    }

    /**
     * Convert this object into a number if possible, otherwise return null.
     *
     * @return a number, null if conversion fails
     */
    @Nullable
    default Number asNumberOrNull() {
        return as(Types.NUMBER, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a number if possible, otherwise return an empty optional.
     *
     * @return a number, empty optional if conversion fails
     */
    @NotNull
    default Optional<Number> asNumberOrEmpty() {
        return as(Types.NUMBER, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a number if possible, otherwise return the default number.
     *
     * @param def the default number
     * @return    a number, default number if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default Number asNumberOrElse(@Nullable Number def) {
        return as(Types.NUMBER, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a number if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default number
     * @return         a number, default number if conversion fails
     */
    @UnknownNullability
    default Number asNumberOrGet(@NotNull Supplier<Number> supplier) {
        return as(Types.NUMBER, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a byte.
     *
     * @return a byte
     */
    @NotNull
    default Byte asByte() {
        return as(Types.BYTE);
    }

    /**
     * Convert this object into a byte if possible, otherwise return null.
     *
     * @return a byte, null if conversion fails
     */
    @Nullable
    default Byte asByteOrNull() {
        return as(Types.BYTE, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a byte if possible, otherwise return an empty optional.
     *
     * @return a byte, empty optional if conversion fails
     */
    @NotNull
    default Optional<Byte> asByteOrEmpty() {
        return as(Types.BYTE, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a byte if possible, otherwise return the default byte.
     *
     * @param def the default byte
     * @return    a byte, default byte if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default Byte asByteOrElse(@Nullable Byte def) {
        return as(Types.BYTE, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a byte if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default byte
     * @return         a byte, default byte if conversion fails
     */
    @UnknownNullability
    default Byte asByteOrGet(@NotNull Supplier<Byte> supplier) {
        return as(Types.BYTE, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a short.
     *
     * @return a short
     */
    @NotNull
    default Short asShort() {
        return as(Types.SHORT);
    }

    /**
     * Convert this object into a short if possible, otherwise return null.
     *
     * @return a short, null if conversion fails
     */
    @Nullable
    default Short asShortOrNull() {
        return as(Types.SHORT, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a short if possible, otherwise return an empty optional.
     *
     * @return a short, empty optional if conversion fails
     */
    @NotNull
    default Optional<Short> asShortOrEmpty() {
        return as(Types.SHORT, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a short if possible, otherwise return the default short.
     *
     * @param def the default short
     * @return    a short, default short if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default Short asShortOrElse(@Nullable Short def) {
        return as(Types.SHORT, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a short if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default short
     * @return         a short, default short if conversion fails
     */
    @UnknownNullability
    default Short asShortOrGet(@NotNull Supplier<Short> supplier) {
        return as(Types.SHORT, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into an int.
     *
     * @return an int
     */
    @NotNull
    default Integer asInteger() {
        return as(Types.INTEGER);
    }

    /**
     * Convert this object into an int if possible, otherwise return null.
     *
     * @return an int, null if conversion fails
     */
    @Nullable
    default Integer asIntegerOrNull() {
        return as(Types.INTEGER, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into an int if possible, otherwise return an empty optional.
     *
     * @return an int, empty optional if conversion fails
     */
    @NotNull
    default Optional<Integer> asIntegerOrEmpty() {
        return as(Types.INTEGER, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into an int if possible, otherwise return the default int.
     *
     * @param def the default int
     * @return    an int, default int if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default Integer asIntegerOrElse(@Nullable Integer def) {
        return as(Types.INTEGER, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into an int if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default int
     * @return         an int, default int if conversion fails
     */
    @UnknownNullability
    default Integer asIntegerOrGet(@NotNull Supplier<Integer> supplier) {
        return as(Types.INTEGER, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a float.
     *
     * @return a float
     */
    @NotNull
    default Float asFloat() {
        return as(Types.FLOAT);
    }

    /**
     * Convert this object into a float if possible, otherwise return null.
     *
     * @return a float, null if conversion fails
     */
    @Nullable
    default Float asFloatOrNull() {
        return as(Types.FLOAT, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a float if possible, otherwise return an empty optional.
     *
     * @return a float, empty optional if conversion fails
     */
    @NotNull
    default Optional<Float> asFloatOrEmpty() {
        return as(Types.FLOAT, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a float if possible, otherwise return the default float.
     *
     * @param def the default float
     * @return    a float, default float if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default Float asFloatOrElse(@Nullable Float def) {
        return as(Types.FLOAT, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a float if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default float
     * @return         a float, default float if conversion fails
     */
    @UnknownNullability
    default Float asFloatOrGet(@NotNull Supplier<Float> supplier) {
        return as(Types.FLOAT, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a long.
     *
     * @return a long
     */
    @NotNull
    default Long asLong() {
        return as(Types.LONG);
    }

    /**
     * Convert this object into a long if possible, otherwise return null.
     *
     * @return a long, null if conversion fails
     */
    @Nullable
    default Long asLongOrNull() {
        return as(Types.LONG, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a long if possible, otherwise return an empty optional.
     *
     * @return a long, empty optional if conversion fails
     */
    @NotNull
    default Optional<Long> asLongOrEmpty() {
        return as(Types.LONG, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a long if possible, otherwise return the default long.
     *
     * @param def the default long
     * @return    a long, default long if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default Long asLongOrElse(@Nullable Long def) {
        return as(Types.LONG, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a long if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default long
     * @return         a long, default long if conversion fails
     */
    @UnknownNullability
    default Long asLongOrGet(@NotNull Supplier<Long> supplier) {
        return as(Types.LONG, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a double.
     *
     * @return a double
     */
    @NotNull
    default Double asDouble() {
        return as(Types.DOUBLE);
    }

    /**
     * Convert this object into a double if possible, otherwise return null.
     *
     * @return a double, null if conversion fails
     */
    @Nullable
    default Double asDoubleOrNull() {
        return as(Types.DOUBLE, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a double if possible, otherwise return an empty optional.
     *
     * @return a double, empty optional if conversion fails
     */
    @NotNull
    default Optional<Double> asDoubleOrEmpty() {
        return as(Types.DOUBLE, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a double if possible, otherwise return the default double.
     *
     * @param def the default double
     * @return    a double, default double if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default Double asDoubleOrElse(@Nullable Double def) {
        return as(Types.DOUBLE, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a double if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default double
     * @return         a double, default double if conversion fails
     */
    @UnknownNullability
    default Double asDoubleOrGet(@NotNull Supplier<Double> supplier) {
        return as(Types.DOUBLE, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a BigInteger.
     *
     * @return a BigInteger
     */
    @NotNull
    default BigInteger asBigInteger() {
        return as(Types.BIG_INTEGER);
    }

    /**
     * Convert this object into a BigInteger if possible, otherwise return null.
     *
     * @return a BigInteger, null if conversion fails
     */
    @Nullable
    default BigInteger asBigIntegerOrNull() {
        return as(Types.BIG_INTEGER, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a BigInteger if possible, otherwise return an empty optional.
     *
     * @return a BigInteger, empty optional if conversion fails
     */
    @NotNull
    default Optional<BigInteger> asBigIntegerOrEmpty() {
        return as(Types.BIG_INTEGER, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a BigInteger if possible, otherwise return the default BigInteger.
     *
     * @param def the default BigInteger
     * @return    a BigInteger, default BigInteger if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default BigInteger asBigIntegerOrElse(@Nullable BigInteger def) {
        return as(Types.BIG_INTEGER, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a BigInteger if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default BigInteger
     * @return         a BigInteger, default BigInteger if conversion fails
     */
    @UnknownNullability
    default BigInteger asBigIntegerOrGet(@NotNull Supplier<BigInteger> supplier) {
        return as(Types.BIG_INTEGER, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    /**
     * Convert this object into a BigDecimal.
     *
     * @return a BigDecimal
     */
    @NotNull
    default BigDecimal asBigDecimal() {
        return as(Types.BIG_DECIMAL);
    }

    /**
     * Convert this object into a BigDecimal if possible, otherwise return null.
     *
     * @return a BigDecimal, null if conversion fails
     */
    @Nullable
    default BigDecimal asBigDecimalOrNull() {
        return as(Types.BIG_DECIMAL, TypeParser::parseOrNull);
    }

    /**
     * Convert this object into a BigDecimal if possible, otherwise return an empty optional.
     *
     * @return a BigDecimal, empty optional if conversion fails
     */
    @NotNull
    default Optional<BigDecimal> asBigDecimalOrEmpty() {
        return as(Types.BIG_DECIMAL, TypeParser::parseOrEmpty);
    }

    /**
     * Convert this object into a BigDecimal if possible, otherwise return the default BigDecimal.
     *
     * @param def the default BigDecimal
     * @return    a BigDecimal, default BigDecimal if conversion fails
     */
    @Nullable
    @Contract("!null -> !null")
    default BigDecimal asBigDecimalOrElse(@Nullable BigDecimal def) {
        return as(Types.BIG_DECIMAL, (parser, object) -> parser.parseOrElse(object, def));
    }

    /**
     * Convert this object into a BigDecimal if possible, otherwise return the result of the supplier.
     *
     * @param supplier the supplier to get the default BigDecimal
     * @return         a BigDecimal, default BigDecimal if conversion fails
     */
    @UnknownNullability
    default BigDecimal asBigDecimalOrGet(@NotNull Supplier<BigDecimal> supplier) {
        return as(Types.BIG_DECIMAL, (parser, object) -> parser.parseOrGet(object, supplier));
    }

    @ApiStatus.Internal
    final class Registry {

        private static final TypeParser<AnyObject<Object>> PARSER = new TypeParser<AnyObject<Object>>() {
            @Override
            public @NotNull Type getType() {
                return AnyObject.class;
            }

            @Override
            public @NotNull AnyObject<Object> parse(@NotNull Object object) {
                return AnyObject.of(object);
            }
        };

        private static final AnyObject<?> EMPTY = new AnyObject<Object>() {
            @Override
            public boolean isEmpty() {
                return true;
            }

            @Override
            public Object getValue() {
                return null;
            }
        };

        Registry() {
        }
    }
}
