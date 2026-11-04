package com.saicone.types;

import com.saicone.types.parser.ArrayParser;
import com.saicone.types.parser.BitSetParser;
import com.saicone.types.parser.BooleanParser;
import com.saicone.types.parser.CharacterParser;
import com.saicone.types.parser.ClassParser;
import com.saicone.types.parser.ColorParser;
import com.saicone.types.parser.DurationParser;
import com.saicone.types.parser.FileParser;
import com.saicone.types.parser.ListParser;
import com.saicone.types.parser.MapParser;
import com.saicone.types.parser.NumberParser;
import com.saicone.types.parser.PathParser;
import com.saicone.types.parser.PatternParser;
import com.saicone.types.parser.SetParser;
import com.saicone.types.parser.StringParser;
import com.saicone.types.parser.TemporalParser;
import com.saicone.types.parser.UriParser;
import com.saicone.types.parser.UrlParser;
import com.saicone.types.parser.UuidParser;
import com.saicone.types.parser.VoidParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.jetbrains.annotations.UnknownNullability;

import java.awt.Color;
import java.io.File;
import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.BitSet;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.regex.Pattern;

/**
 * Utility class to collect common and registrable type parsers.
 *
 * @author Rubenicos
 */
public class Types {

    private static final Map<Type, TypeParser<?>> PARSER_MAP = new HashMap<>();

    /**
     * Void type parser.
     *
     * @see VoidParser
     */
    public static final TypeParser<Void> VOID = VoidParser.INSTANCE;
    /**
     * Type parser to return any object itself.
     */
    public static final TypeParser<Object> OBJECT = TypeParser.of(Object.class, (object) -> object);
    /**
     * String type parser.
     *
     * @see StringParser
     */
    public static final TypeParser<String> STRING = StringParser.INSTANCE;
    /**
     * Character type parser.
     *
     * @see CharacterParser
     */
    public static final TypeParser<Character> CHAR = CharacterParser.INSTANCE;
    /**
     * Boolean type parser.
     *
     * @see BooleanParser
     */
    public static final TypeParser<Boolean> BOOLEAN = BooleanParser.INSTANCE;
    /**
     * Number type parser.
     *
     * @see NumberParser
     */
    public static final TypeParser<Number> NUMBER = NumberParser.NUMBER;
    /**
     * Byte type parser.
     *
     * @see NumberParser
     */
    public static final TypeParser<Byte> BYTE = NumberParser.BYTE;
    /**
     * Short type parser.
     *
     * @see NumberParser
     */
    public static final TypeParser<Short> SHORT = NumberParser.SHORT;
    /**
     * Integer type parser.
     *
     * @see NumberParser
     */
    public static final TypeParser<Integer> INTEGER = NumberParser.INTEGER;
    /**
     * Long type parser.
     *
     * @see NumberParser
     */
    public static final TypeParser<Long> LONG = NumberParser.LONG;
    /**
     * Float type parser.
     *
     * @see NumberParser
     */
    public static final TypeParser<Float> FLOAT = NumberParser.FLOAT;
    /**
     * Double type parser.
     *
     * @see NumberParser
     */
    public static final TypeParser<Double> DOUBLE = NumberParser.DOUBLE;
    /**
     * Class type parser.
     *
     * @see ClassParser
     */
    public static final TypeParser<Class<?>> CLASS = ClassParser.INSTANCE;
    /**
     * BigInteger type parser.
     *
     * @see NumberParser
     */
    public static final TypeParser<BigInteger> BIG_INTEGER = NumberParser.BIG_INTEGER;
    /**
     * BigDecimal type parser.
     *
     * @see NumberParser
     */
    public static final TypeParser<BigDecimal> BIG_DECIMAL = NumberParser.BIG_DECIMAL;
    /**
     * Unique ID type parser.
     *
     * @see UuidParser
     */
    public static final TypeParser<java.util.UUID> UUID = UuidParser.INSTANCE;
    /**
     * Pattern type parser.
     *
     * @see UuidParser
     */
    public static final TypeParser<Pattern> PATTERN = PatternParser.instance();
    /**
     * BitSet type parser.
     *
     * @see BitSetParser
     */
    public static final TypeParser<BitSet> BIT_SET = BitSetParser.INSTANCE;
    /**
     * URI type parser.
     *
     * @see UriParser
     */
    public static final TypeParser<java.net.URI> URI = UriParser.INSTANCE;
    /**
     * URL type parser.
     *
     * @see UrlParser
     */
    public static final TypeParser<java.net.URL> URL = UrlParser.INSTANCE;
    /**
     * File type parser.
     *
     * @see FileParser
     */
    public static final TypeParser<File> FILE = FileParser.INSTANCE;
    /**
     * Path type parser.
     *
     * @see PathParser
     */
    public static final TypeParser<Path> PATH = PathParser.INSTANCE;
    /**
     * Duration type parser.
     *
     * @see DurationParser
     */
    public static final TypeParser<Duration> DURATION = DurationParser.INSTANCE;
    /**
     * LocalDate type parser.
     *
     * @see TemporalParser#LOCAL_DATE
     */
    public static final TypeParser<LocalDate> LOCAL_DATE = TemporalParser.LOCAL_DATE;
    /**
     * LocalTime parser type.
     *
     * @see TemporalParser#LOCAL_TIME
     */
    public static TypeParser<LocalTime> LOCAL_TIME = TemporalParser.LOCAL_TIME;
    /**
     * LocalDateTime parser type.
     *
     * @see TemporalParser#LOCAL_DATE_TIME
     */
    public static final TypeParser<LocalDateTime> LOCAL_DATE_TIME = TemporalParser.LOCAL_DATE_TIME;
    /**
     * Color type parser.
     *
     * @see ColorParser
     */
    public static final TypeParser<Color> COLOR = ColorParser.INSTANCE;
    /**
     * Array of objects type parser.
     *
     * @see ArrayParser
     */
    public static final TypeParser<Object[]> ARRAY = ArrayParser.of(OBJECT);
    /**
     * List of objects type parser.
     *
     * @see ListParser
     */
    public static final TypeParser<List<Object>> LIST = ListParser.of(OBJECT);
    /**
     * Set of objects type parser.
     *
     * @see SetParser
     */
    public static final TypeParser<Set<Object>> SET = SetParser.of(OBJECT);
    /**
     * Map of objects type parser.<br>
     * This is the most typical Map format to save data.
     *
     * @see MapParser#of(TypeParser, TypeParser)
     */
    public static final TypeParser<Map<Object, Object>> MAP = MapParser.of(OBJECT, OBJECT);

    static {
        put(Object.class, OBJECT);
        put(String.class, STRING);
        put(Character.class, CHAR);
        put(char.class, TypeParser.of(char.class, CHAR));
        put(Boolean.class, BOOLEAN);
        put(boolean.class, TypeParser.of(boolean.class, BOOLEAN));
        put(Number.class, NUMBER);
        put(Byte.class, BYTE);
        put(byte.class, TypeParser.of(byte.class, BYTE));
        put(Short.class, SHORT);
        put(short.class, TypeParser.of(short.class, SHORT));
        put(Integer.class, INTEGER);
        put(int.class, TypeParser.of(int.class, INTEGER));
        put(Float.class, FLOAT);
        put(float.class, TypeParser.of(float.class, FLOAT));
        put(Long.class, LONG);
        put(long.class, TypeParser.of(long.class, LONG));
        put(Double.class, DOUBLE);
        put(double.class, TypeParser.of(double.class, DOUBLE));
        put(BigInteger.class, BIG_INTEGER);
        put(BigDecimal.class, BIG_DECIMAL);
        put(Class.class, CLASS);
        put(java.util.UUID.class, UUID);
        put(Pattern.class, PATTERN);
        put(java.net.URI.class, URI);
        put(java.net.URL.class, URL);
        put(File.class, FILE);
        put(Path.class, PATH);
        put(Duration.class, DURATION);
        put(LocalDate.class, LOCAL_DATE);
        put(LocalTime.class, LOCAL_TIME);
        put(LocalDateTime.class, LOCAL_DATE_TIME);
        put(Color.class, COLOR);
        put(BitSet.class, BIT_SET);
        put(Object[].class, ARRAY);
        put(List.class, LIST);
        put(Set.class, SET);
        put(Map.class, MAP);
    }

    Types() {
    }

    /**
     * Check if any type object is registered as parser key.
     *
     * @param type the key object
     * @return     true if is already registered
     */
    public static boolean contains(@NotNull Type type) {
        return PARSER_MAP.containsKey(type);
    }

    /**
     * Register provided type parser associated by class type.
     *
     * @param type   the key class type
     * @param parser the type parser
     * @return       the previous type parsed associated with provided class
     * @param <T> the type result of the parser
     */
    @Nullable
    public static <T> TypeParser<?> put(@NotNull Class<T> type, @NotNull TypeParser<T> parser) {
        return PARSER_MAP.put(type, parser);
    }

    /**
     * Register provided type parser associated with type object.
     *
     * @param type   the key object
     * @param parser the type parser
     * @return       the previous type parsed associated with provided class
     * @param <T> the type result of the parser
     */
    @Nullable
    public static <T> TypeParser<?> put(@NotNull Type type, @NotNull TypeParser<T> parser) {
        return PARSER_MAP.put(type, parser);
    }

    /**
     * Remove any registered type parsed associated by object.
     *
     * @param type the object key
     * @return     the previous type parsed associated with provided class
     */
    @Nullable
    public static TypeParser<?> remove(@NotNull Type type) {
        return PARSER_MAP.remove(type);
    }

    /**
     * Get the previously registered type parser from object type or a parser that return the object itself.
     *
     * @param type the class type
     * @return     the registered type parser if found, parser by cast object otherwise
     * @param <T> the type result of the parser
     */
    @NotNull
    public static <T> TypeParser<T> getOrDefault(@NotNull Class<T> type) {
        return getOrDefault((Type) type);
    }

    /**
     * Get the previously registered type parser from object type or a parser that return the object itself.
     *
     * @param type the key object
     * @return     the registered type parser if found, parser by cast object otherwise
     * @param <T> the type result of the parser
     */
    @NotNull
    @SuppressWarnings("unchecked")
    public static <T> TypeParser<T> getOrDefault(@NotNull Type type) {
        TypeParser<?> parser = PARSER_MAP.get(type);
        if (parser == null) {
            parser = PARSER_MAP.get(type.getClass());
        }
        if (parser == null) {
            return (object) -> (T) object;
        }
        return (TypeParser<T>) parser;
    }

    /**
     * Get the previously registered type parser from object type or null if not found.
     *
     * @param type the class type
     * @return     the registered type parser if found, null otherwise
     * @param <T> the type result of the parser
     */
    @Nullable
    public static <T> TypeParser<T> getOrNull(@NotNull Class<T> type) {
        return getOrNull((Type) type);
    }

    /**
     * Get the previously registered type parser from object type or null if not found.
     *
     * @param type the key object
     * @return     the registered type parser if found, null otherwise
     * @param <T> the type result of the parser
     */
    @Nullable
    @SuppressWarnings("unchecked")
    public static <T> TypeParser<T> getOrNull(@NotNull Type type) {
        return (TypeParser<T>) PARSER_MAP.get(type);
    }

    /**
     * Run a function with the provided object and the type parser associated with the provided type.
     *
     * @param type     the key class type
     * @param object   the object to parse
     * @param function the function to run with the object and the type parser
     * @return         the result of the function
     * @throws X if the parsing fails
     * @param <T> the type result of the parser
     * @param <R> the type result of the function
     * @param <X> the type of exception that can be thrown
     */
    @Nullable
    public static <T, R, X extends Throwable> R run(@NotNull Class<T> type, @Nullable Object object, @NotNull TypeParserFunction<T, R, X> function) throws X {
        return run((Type) type, object, function);
    }

    /**
     * Run a function with the provided object and the type parser associated with the provided type.
     *
     * @param type     the key object
     * @param object   the object to parse
     * @param function the function to run with the object and the type parser
     * @return         the result of the function
     * @throws X if the parsing fails
     * @param <R> the type result of the function
     * @param <X> the type of exception that can be thrown
     */
    @UnknownNullability
    public static <R, X extends Throwable> R run(@NotNull Type type, @Nullable Object object, @NotNull TypeParserFunction<?, R, X> function) throws X {
        return function.apply(getOrDefault(type), object);
    }
}
