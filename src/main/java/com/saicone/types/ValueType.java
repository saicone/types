package com.saicone.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.UUID;

/**
 * Represents a value that can be converted into different types of objects.
 *
 * @author Rubenicos
 *
 * @param <T> the value type itself.
 */
@FunctionalInterface
public interface ValueType<T> {

    /**
     * Create a wrapped value type from given object type.
     *
     * @param value the object type to wrap.
     * @return      a value type instance.
     * @param <T>   the value type itself.
     */
    @NotNull
    static <T> ValueType<T> of(T value) {
        return () -> value;
    }

    /**
     * Get the value type that implements the interface.
     *
     * @return the value type itself.
     */
    T getValue();

    /**
     * Convert this object into given class type.
     *
     * @see Types#parse(Class, Object)
     *
     * @param type the class type.
     * @return     a converted value type, null otherwise.
     * @param <E>  the type result.
     */
    @Nullable
    default <E> E as(@NotNull Class<E> type) {
        return as(type, null);
    }

    /**
     * Convert this object into given class type.
     *
     * @see Types#parse(Class, Object, Object)
     *
     * @param type the class type.
     * @param def  the type object to return if the conversion fails.
     * @return     a converted value type, default object otherwise.
     * @param <E>  the type result.
     */
    @Nullable
    @Contract("_, !null -> !null")
    default <E> E as(@NotNull Class<E> type, @Nullable E def) {
        return Types.parse(type, getValue(), def);
    }

    /**
     * Convert this object with the given type parser.
     *
     * @see TypeParser#parse(Object)
     *
     * @param parser the parser to apply this object into.
     * @return       a converted value type, null otherwise.
     * @param <E>    the type result.
     */
    @Nullable
    default <E> E as(@NotNull TypeParser<E> parser) {
        return as(parser, null);
    }

    /**
     * Convert this object with the given type parser.
     *
     * @see TypeParser#parse(Object, Object)
     *
     * @param parser the parser to apply this object into.
     * @param def    the type object to return if parse fails.
     * @return       a converted value type, default object otherwise.
     * @param <E>    the type result.
     */
    @Nullable
    @Contract("_, !null -> !null")
    default <E> E as(@NotNull TypeParser<E> parser, @Nullable E def) {
        return parser.parse(getValue(), def);
    }

    /**
     * Convert this object into Optional converted type with type parser.
     *
     * @param parser the parser to apply this object into.
     * @return       a wrapped conversion result as Optional.
     * @param <E>    the type result of parser.
     */
    @NotNull
    default <E> Optional<E> asOptional(@NotNull TypeParser<E> parser) {
        return parser.optional(getValue());
    }

    /**
     * Convert this object into given collection type with type parser.
     *
     * @see TypeParser#collection(Collection, Object)
     *
     * @param parser     the parser to apply this object into.
     * @param collection the collection to add parsed values.
     * @return           a type collection.
     * @param <E>        the type by parser.
     * @param <C>        the collection type.
     */
    @NotNull
    default <E, C extends Collection<E>> C asCollection(@NotNull TypeParser<E> parser, @NotNull C collection) {
        return parser.collection(collection, getValue());
    }

    /**
     * Convert this object into enum type.
     *
     * @param type   the required enum type.
     * @return       an enum type.
     * @param <E>    the enum type result.
     */
    @Nullable
    default <E extends Enum<?>> E asEnum(@NotNull Class<E> type) {
        return TypeParser.enumeration(type).parse(getValue());
    }

    /**
     * Convert this object into enum type with provided enum values.
     *
     * @param type   the required enum type.
     * @param values the enum value array.
     * @return       an enum type.
     * @param <E>    the enum type result.
     */
    @Nullable
    default <E extends Enum<?>> E asEnum(@NotNull Class<E> type, @NotNull E[] values) {
        return TypeParser.enumeration(type, () -> values).parse(getValue());
    }

    /**
     * Convert this object into map type.
     *
     * @param keyParser   the parser that accept keys.
     * @param valueParser the parser that accept values.
     * @param map         the required map type.
     * @return            a map type.
     * @param <K>         the type of keys maintained by map.
     * @param <V>         the type of mapped values.
     * @param <M>         the map type result of the function.
     */
    @NotNull
    @SuppressWarnings("all")
    default <K, V, M extends Map<K, V>> M asMap(@NotNull TypeParser<K> keyParser, @NotNull TypeParser<V> valueParser, @NotNull M map) {
        return TypeParser.map(keyParser, valueParser, () -> map).parse(getValue());
    }

    /**
     * Convert this object into an ArrayList with the given type parser.
     *
     * @see TypeParser#list(Object)
     *
     * @param parser the parser to apply this object into.
     * @return       a type list containing the parsed values.
     * @param <E>    the type by parser.
     */
    @NotNull
    default <E> List<E> asList(@NotNull TypeParser<E> parser) {
        return asCollection(parser, new ArrayList<>());
    }

    /**
     * Convert this object into a HashSet with the given type parser.
     *
     * @see TypeParser#set(Object)
     *
     * @param parser the parser to apply this object into.
     * @return       a type set containing the parsed values.
     * @param <E>    the type by parser.
     */
    @NotNull
    default <E> Set<E> asSet(@NotNull TypeParser<E> parser) {
        return asCollection(parser, new HashSet<>());
    }

    /**
     * Convert this object into array type with parser.
     *
     * @param parser the parser to apply this object into.
     * @return       a type array.
     * @param <A>    the array type.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default <A> A asArray(@NotNull TypeParser<?> parser) {
        return (A) parser.array(getValue());
    }

    /**
     * Convert this object into given array type with parser.
     *
     * @param parser the parser to apply this object into.
     * @param array  the array to add parsed values.
     * @return       a type array.
     * @param <A>    the array type.
     */
    @NotNull
    default <A> A asArray(@NotNull TypeParser<?> parser, @NotNull A array) {
        return parser.parseArray(array, getValue());
    }

    /**
     * Convert this object into given array type with parser.
     *
     * @param parser the parser to apply this object into.
     * @param array  the array to add parsed values.
     * @return       a type array.
     * @param <E>    the type by parser.
     */
    @NotNull
    default <E> E[] asArray(@NotNull TypeParser<E> parser, @NotNull E[] array) {
        return parser.parseArray(array, getValue());
    }

    /**
     * Convert this object into a string.
     *
     * @see Types#STRING
     *
     * @return a string, null if conversion fails.
     */
    @Nullable
    default String asString() {
        return as(Types.STRING);
    }

    /**
     * Convert this object into a string.
     *
     * @see Types#STRING
     *
     * @param def the default string.
     * @return    a string, default string if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default String asString(@Nullable String def) {
        return as(Types.STRING, def);
    }

    /**
     * Convert this object into a character.
     *
     * @see Types#CHAR
     *
     * @return a character, null if conversion fails.
     */
    @Nullable
    default Character asChar() {
        return as(Types.CHAR);
    }

    /**
     * Convert this object into a character.
     *
     * @see Types#CHAR
     *
     * @param def the default character.
     * @return    a character, default character if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default Character asChar(@Nullable Character def) {
        return as(Types.CHAR, def);
    }

    /**
     * Convert this object into a boolean.
     *
     * @see Types#BOOLEAN
     *
     * @return a boolean, null if conversion fails.
     */
    @Nullable
    default Boolean asBoolean() {
        return as(Types.BOOLEAN);
    }

    /**
     * Convert this object into a boolean.
     *
     * @see Types#BOOLEAN
     *
     * @param def the default boolean.
     * @return    a boolean, default boolean if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default Boolean asBoolean(@Nullable Boolean def) {
        return as(Types.BOOLEAN, def);
    }

    /**
     * Convert this object into a number.
     *
     * @see Types#NUMBER
     *
     * @return a number, null if conversion fails.
     */
    @Nullable
    default Number asNumber() {
        return as(Types.NUMBER);
    }

    /**
     * Convert this object into a number.
     *
     * @see Types#NUMBER
     *
     * @param def the default string.
     * @return    a number, default byte if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default Number asNumber(@Nullable Number def) {
        return as(Types.NUMBER, def);
    }

    /**
     * Convert this object into a byte.
     *
     * @see Types#BYTE
     *
     * @return a byte, null if conversion fails.
     */
    @Nullable
    default Byte asByte() {
        return as(Types.BYTE);
    }

    /**
     * Convert this object into a byte.
     *
     * @see Types#BYTE
     *
     * @param def the default string.
     * @return    a byte, default byte if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default Byte asByte(@Nullable Byte def) {
        return as(Types.BYTE, def);
    }

    /**
     * Convert this object into a short.
     *
     * @see Types#SHORT
     *
     * @return a short, null if conversion fails.
     */
    @Nullable
    default Short asShort() {
        return as(Types.SHORT);
    }

    /**
     * Convert this object into a short.
     *
     * @see Types#SHORT
     *
     * @param def the default string.
     * @return    a short, default short if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default Short asShort(@Nullable Short def) {
        return as(Types.SHORT, def);
    }

    /**
     * Convert this object into an integer.
     *
     * @see Types#INTEGER
     *
     * @return an integer, null if conversion fails.
     */
    @Nullable
    default Integer asInt() {
        return as(Types.INTEGER);
    }

    /**
     * Convert this object into an integer.
     *
     * @see Types#INTEGER
     *
     * @param def the default string.
     * @return    an integer, default integer if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default Integer asInt(@Nullable Integer def) {
        return as(Types.INTEGER, def);
    }

    /**
     * Convert this object into a float.
     *
     * @see Types#FLOAT
     *
     * @return a float, null if conversion fails.
     */
    @Nullable
    default Float asFloat() {
        return as(Types.FLOAT);
    }

    /**
     * Convert this object into a float.
     *
     * @see Types#FLOAT
     *
     * @param def the default string.
     * @return    a float, default float if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default Float asFloat(@Nullable Float def) {
        return as(Types.FLOAT, def);
    }

    /**
     * Convert this object into a long.
     *
     * @see Types#LONG
     *
     * @return a long, null if conversion fails.
     */
    @Nullable
    default Long asLong() {
        return as(Types.LONG);
    }

    /**
     * Convert this object into a long.
     *
     * @see Types#LONG
     *
     * @param def the default string.
     * @return    a long, default long if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default Long asLong(@Nullable Long def) {
        return as(Types.LONG, def);
    }

    /**
     * Convert this object into a double.
     *
     * @see Types#DOUBLE
     *
     * @return a double, null if conversion fails.
     */
    @Nullable
    default Double asDouble() {
        return as(Types.DOUBLE);
    }

    /**
     * Convert this object into a double.
     *
     * @see Types#DOUBLE
     *
     * @param def the default string.
     * @return    a double, default double if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default Double asDouble(@Nullable Double def) {
        return as(Types.DOUBLE, def);
    }

    /**
     * Convert this object into a BigInteger.
     *
     * @see Types#BIG_INTEGER
     *
     * @return a BigInteger, null if conversion fails.
     */
    @Nullable
    default BigInteger asBigInteger() {
        return as(Types.BIG_INTEGER);
    }

    /**
     * Convert this object into a BigInteger.
     *
     * @see Types#BIG_INTEGER
     *
     * @param def the default string.
     * @return    a BigInteger, default BigInteger if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default BigInteger asBigInteger(@Nullable BigInteger def) {
        return as(Types.BIG_INTEGER, def);
    }

    /**
     * Convert this object into a BigDecimal.
     *
     * @see Types#BIG_DECIMAL
     *
     * @return a BigDecimal, null if conversion fails.
     */
    @Nullable
    default BigDecimal asBigDecimal() {
        return as(Types.BIG_DECIMAL);
    }

    /**
     * Convert this object into a BigDecimal.
     *
     * @see Types#BIG_DECIMAL
     *
     * @param def the default string.
     * @return    a BigDecimal, default BigDecimal if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default BigDecimal asBigDecimal(@Nullable BigDecimal def) {
        return as(Types.BIG_DECIMAL, def);
    }

    /**
     * Convert this object into a unique ID.
     *
     * @see Types#UUID
     *
     * @return a unique ID, null if conversion fails.
     */
    @Nullable
    default UUID asUniqueId() {
        return as(Types.UUID);
    }

    /**
     * Convert this object into a unique ID.
     *
     * @see Types#UUID
     *
     * @param def the default string.
     * @return    a unique ID, default unique ID if conversion fails.
     */
    @Nullable
    @Contract("!null -> !null")
    default UUID asUniqueId(@Nullable UUID def) {
        return as(Types.UUID, def);
    }
}
