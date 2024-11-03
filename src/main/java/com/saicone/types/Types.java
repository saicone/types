package com.saicone.types;

import com.saicone.types.parser.ClassParser;
import com.saicone.types.parser.FileParser;
import com.saicone.types.parser.NumberParser;
import com.saicone.types.parser.PathParser;
import com.saicone.types.parser.TemporalParser;
import com.saicone.types.parser.UriParser;
import com.saicone.types.parser.UrlParser;
import com.saicone.types.parser.UuidParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;
import java.util.StringJoiner;
import java.util.function.Supplier;

/**
 * Utility class to collect common and registrable type parsers.
 *
 * @author Rubenicos
 */
public class Types {

    private static final Map<Object, TypeParser<?>> PARSER_MAP = new HashMap<>();
    /**
     * Type parser to return any object itself.
     */
    public static final TypeParser<Object> OBJECT = TypeParser.of(Object.class, (object) -> object);
    /**
     * String type parser.
     *
     * @see String#valueOf(Object)
     */
    public static final TypeParser<String> STRING = TypeParser.first(String.class, String::valueOf);
    /**
     * Text type parser, instead of {@link Types#STRING} this parser calls {@link Arrays#toString(Object[])}
     * if the provided object is instance of object array.
     */
    public static final TypeParser<String> TEXT = TypeParser.single(String.class, (object) -> {
        if (object instanceof Object[]) {
            return Arrays.toString((Object[]) object);
        } else if (object.getClass().isArray()) {
            final StringJoiner joiner = new StringJoiner(", ", "[", "]");
            for (Object o : IterableType.of(object)) {
                joiner.add(String.valueOf(o));
            }
            return joiner.toString();
        } else {
            return String.valueOf(object);
        }
    });
    /**
     * Character type parser.<br>
     * This parser extracts the first character from non-empty String value.
     */
    public static final TypeParser<Character> CHAR = TypeParser.first(Character.class, (object) -> {
        if (object instanceof Character) {
            return (Character) object;
        }
        if (object instanceof Number) {
            final int num = ((Number) object).intValue();
            if (num >= 0 && num <= 65535) {
                return (char) num;
            }
        }
        final String s = String.valueOf(object);
        return s.isEmpty() ? null : s.charAt(0);
    });
    /**
     * Boolean type parser.<br>
     * This parser accept any String representation of:<br>
     * true | false<br>
     * t | f<br>
     * 1 | 0<br>
     * yes | no<br>
     * on | off<br>
     * y | n
     */
    public static final TypeParser<Boolean> BOOLEAN = TypeParser.first(Boolean.class, (object) -> {
        if (object instanceof Boolean) {
            return (Boolean) object;
        }
        switch (String.valueOf(object instanceof Number ? ((Number) object).intValue() : object).toLowerCase()) {
            case "true":
            case "t":
            case "1":
            case "yes":
            case "on":
            case "y":
                return true;
            case "false":
            case "f":
            case "0":
            case "no":
            case "off":
            case "n":
                return false;
            default:
                return null;
        }
    });
    /**
     * Number type parser.<br>
     * This parser convert any object string representation as any type of number.
     */
    public static final TypeParser<Number> NUMBER = NumberParser.NUMBER;
    /**
     * Byte type parser.
     */
    public static final TypeParser<Byte> BYTE = NumberParser.BYTE;
    /**
     * Short type parser.
     */
    public static final TypeParser<Short> SHORT = NumberParser.SHORT;
    /**
     * Integer type parser.
     */
    public static final TypeParser<Integer> INTEGER = NumberParser.INTEGER;
    /**
     * Float type parser.
     */
    public static final TypeParser<Float> FLOAT = NumberParser.FLOAT;
    /**
     * Long type parser.
     */
    public static final TypeParser<Long> LONG = NumberParser.LONG;
    /**
     * Double type parser.
     */
    public static final TypeParser<Double> DOUBLE = NumberParser.DOUBLE;
    /**
     * BigInteger type parser.<br>
     * This parser can accept any Number or String representation of BigInteger.
     */
    public static final TypeParser<BigInteger> BIG_INTEGER = NumberParser.BIG_INTEGER;
    /**
     * BigDecimal type parser.<br>
     * This parser can accept any Number or string representation of BigDecimal.
     */
    public static final TypeParser<BigDecimal> BIG_DECIMAL = NumberParser.BIG_DECIMAL;
    /**
     * Class type parser.<br>
     * This parser can accept any string representation of Class name.
     */
    public static final TypeParser<Class<?>> CLASS = new ClassParser();
    /**
     * Unique ID type parser.<br>
     * This parser accepts any String representation of unique ID and also 4-length primitive int array.
     */
    public static final TypeParser<java.util.UUID> UUID = new UuidParser();
    /**
     * URI type parser.
     */
    public static final TypeParser<java.net.URI> URI = new UriParser();
    /**
     * URL type parser.
     */
    public static final TypeParser<java.net.URL> URL = new UrlParser();
    /**
     * File type parser.<br>
     * This parser can accept any String separated by {@code /} or String array.
     */
    public static final TypeParser<File> FILE = new FileParser();
    /**
     * Path type parser.<br>
     * This parser can accept any String separated by {@code /} or String array.
     */
    public static final TypeParser<Path> PATH = new PathParser();
    /**
     * LocalDate type parser.<br>
     * This parser accept any ISO-8601 String or Number array with date values.
     *
     * @see LocalDate#parse(CharSequence)
     * @see LocalDate#ofYearDay(int, int)
     * @see LocalDate#of(int, int, int)
     * @see LocalDate#ofEpochDay(long)
     */
    public static final TypeParser<LocalDate> LOCAL_DATE = TemporalParser.LOCAL_DATE;
    /**
     * LocalTime parser type.<br>
     * This parser accept any String with the format {@code hour:minute:second.nanoOfSecond}
     * or Number array with time values.
     *
     * @see LocalTime#parse(CharSequence)
     * @see LocalTime#ofSecondOfDay(long)
     * @see LocalTime#of(int, int)
     * @see LocalTime#of(int, int, int)
     * @see LocalTime#of(int, int, int, int)
     */
    public static TypeParser<LocalTime> LOCAL_TIME = TemporalParser.LOCAL_TIME;
    /**
     * LocalDateTime parser type.<br>
     * This parser accept any ISO-8601 String separated by {@code T} with time format {@code hour:minute:second.nanoOfSecond}
     * or Number array with date time values.
     *
     * @see LocalDateTime#parse(CharSequence)
     * @see LocalDateTime#ofEpochSecond(long, int, ZoneOffset)
     * @see LocalDateTime#of(int, int, int, int, int)
     * @see LocalDateTime#of(int, int, int, int, int, int)
     * @see LocalDateTime#of(int, Month, int, int, int, int, int)
     */
    public static final TypeParser<LocalDateTime> LOCAL_DATE_TIME = TemporalParser.LOCAL_DATE_TIME;
    /**
     * Map of objects type parser.<br>
     * This is the most typical Map format to save data.
     *
     * @see TypeParser#map(TypeParser, TypeParser, Supplier) Custom map type parser.
     */
    public static final TypeParser<Map<String, Object>> MAP = TypeParser.map(STRING, OBJECT, HashMap::new);

    static {
        put(Object.class, OBJECT);
        put("text", TEXT);
        put(String.class, STRING);
        put(Character.class, CHAR);
        put(char.class, TypeParser.of(char.class, (object) -> {
            final Character c = CHAR.parse(object);
            return c != null ? c : '\0';
        }));
        put(Boolean.class, BOOLEAN);
        put(boolean.class, TypeParser.of(boolean.class, (object) -> {
            final Boolean bool = BOOLEAN.parse(object);
            return bool != null ? bool : Boolean.FALSE;
        }));
        put(Number.class, NUMBER);
        put(Byte.class, BYTE);
        put(byte.class, TypeParser.of(byte.class, object -> {
            final Byte num = BYTE.parse(object);
            return num != null ? num : Byte.MIN_VALUE;
        }));
        put(Short.class, SHORT);
        put(short.class, TypeParser.of(short.class, object -> {
            final Short num = SHORT.parse(object);
            return num != null ? num : Short.MIN_VALUE;
        }));
        put(Integer.class, INTEGER);
        put(int.class, TypeParser.of(int.class, object -> {
            final Integer num = INTEGER.parse(object);
            return num != null ? num : Integer.MIN_VALUE;
        }));
        put(Float.class, FLOAT);
        put(float.class, TypeParser.of(float.class, object -> {
            final Float num = FLOAT.parse(object);
            return num != null ? num : Float.MIN_VALUE;
        }));
        put(Long.class, LONG);
        put(long.class, TypeParser.of(long.class, object -> {
            final Long num = LONG.parse(object);
            return num != null ? num : Long.MIN_VALUE;
        }));
        put(Double.class, DOUBLE);
        put(double.class, TypeParser.of(double.class, object -> {
            final Double num = DOUBLE.parse(object);
            return num != null ? num : Double.MIN_VALUE;
        }));
        put(BigInteger.class, BIG_INTEGER);
        put(BigDecimal.class, BIG_DECIMAL);
        put(Class.class, CLASS);
        put(java.util.UUID.class, UUID);
        put(java.net.URI.class, URI);
        put(java.net.URL.class, URL);
        put(File.class, FILE);
        put(Path.class, PATH);
        put(LocalDate.class, LOCAL_DATE);
        put(LocalTime.class, LOCAL_TIME);
        put(LocalDateTime.class, LOCAL_DATE_TIME);
        put(Map.class, MAP);
    }

    Types() {
    }

    /**
     * Check if any type object is registered as parser key.
     *
     * @param type the key object.
     * @return     true if is already registered.
     */
    public static boolean contains(@NotNull Object type) {
        return PARSER_MAP.containsKey(type);
    }

    /**
     * Register provided type parser associated by class type.
     *
     * @param type   the key class type.
     * @param parser the type parser.
     * @return       the previous type parsed associated with provided class.
     * @param <T>    the type result of the parser.
     */
    @Nullable
    public static <T> TypeParser<?> put(@NotNull Class<T> type, @NotNull TypeParser<T> parser) {
        return PARSER_MAP.put(type, parser);
    }

    /**
     * Register provided type parser associated with type object.
     *
     * @param type   the key object.
     * @param parser the type parser.
     * @return       the previous type parsed associated with provided class.
     * @param <T>    the type result of the parser.
     */
    @Nullable
    public static <T> TypeParser<?> put(@NotNull Object type, @NotNull TypeParser<T> parser) {
        return PARSER_MAP.put(type instanceof String ? ((String) type).toLowerCase() : type, parser);
    }

    /**
     * Remove any registered type parsed associated by object.
     *
     * @param type the object key.
     * @return     the previous type parsed associated with provided class.
     */
    @Nullable
    public static TypeParser<?> remove(@NotNull Object type) {
        return PARSER_MAP.remove(type instanceof String ? ((String) type).toLowerCase() : type);
    }

    /**
     * Get the previously registered type parser from class type.
     *
     * @param type the class type.
     * @return     the registered type parser if found, parser by cast object otherwise.
     * @param <T>  the type result of the parser.
     */
    @NotNull
    public static <T> TypeParser<T> of(@NotNull Class<T> type) {
        return of((Object) type);
    }

    /**
     * Get the previously registered type parser from object type.
     *
     * @param type the key object.
     * @return     the registered type parser if found, parser by cast object otherwise.
     * @param <T>  the type result of the parser.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> TypeParser<T> of(@NotNull Object type) {
        final TypeParser<?> parser = PARSER_MAP.get(type);
        if (parser == null) {
            return (object) -> (T) object;
        }
        return (TypeParser<T>) parser;
    }

    /**
     * Parse any object by providing a type class to find previously registered type parser.<br>
     * This method can also parse into primitive class objects and instead of null the failed
     * value to return will be MIN value for number types, FALSE for boolean and empty for char.
     *
     * @param type   the class type.
     * @param object the object to parse.
     * @return       a converted value type, null otherwise.
     * @param <T>    the type result.
     */
    @Nullable
    public static <T> T parse(@NotNull Class<T> type, @Nullable Object object) {
        return parse((Object) type, object, null);
    }

    /**
     * Parse any object by providing a type class to find previously registered type parser.
     *
     * @param type   the key object.
     * @param object the object to parse.
     * @return       a converted value type, null otherwise.
     * @param <T>    the type result.
     */
    @Nullable
    public static <T> T parse(@NotNull Object type, @Nullable Object object) {
        return parse(type, object, null);
    }

    /**
     * Parse any object by providing a type class to find previously registered type parser.<br>
     * This method can also parse into primitive class objects and instead of default object the failed
     * value to return will be MIN value for number types, FALSE for boolean and empty for char.
     *
     * @param type   the class type.
     * @param object the object to parse.
     * @param def    the type object to return if parser fails or doesn't exist.
     * @return       a converted value type, default object otherwise.
     * @param <T>    the type result.
     */
    @Nullable
    @Contract("_, _, !null -> !null")
    public static <T> T parse(@NotNull Class<T> type, @Nullable Object object, @Nullable T def) {
        return parse((Object) type, object, def);
    }

    /**
     * Parse any object by providing a type object to find previously registered type parser.
     *
     * @param type   the key object.
     * @param object the object to parse.
     * @param def    the type object to return if parser fails or doesn't exist.
     * @return       a converted value type, default object otherwise.
     * @param <T>    the type result.
     */
    @Nullable
    @Contract("_, _, !null -> !null")
    @SuppressWarnings("unchecked")
    public static <T> T parse(@NotNull Object type, @Nullable Object object, @Nullable T def) {
        final TypeParser<?> parser = PARSER_MAP.get(type);
        if (parser == null) {
            return def;
        }
        return ((TypeParser<T>) parser).parse(object, def);
    }
}
