package com.saicone.types;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.lang.reflect.Array;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.net.MalformedURLException;
import java.net.URISyntaxException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.text.NumberFormat;
import java.text.ParseException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.util.*;
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
    public static final TypeParser<String> STRING = TypeParser.single(String.class, String::valueOf);
    /**
     * Text type parser, instead of {@link Types#STRING} this parser calls {@link Arrays#toString(Object[])}
     * if the provided object is instance of object array.
     */
    public static final TypeParser<String> TEXT = TypeParser.single(String.class, (object) -> {
        if (object instanceof Object[]) {
            return Arrays.toString((Object[]) object);
        } else {
            return String.valueOf(object);
        }
    });
    /**
     * Character type parser.<br>
     * This parser extracts the first character from non-empty String value.
     */
    public static final TypeParser<Character> CHAR = TypeParser.single(Character.class, (object) -> {
        if (object instanceof Character) {
            return (Character) object;
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
    public static final TypeParser<Boolean> BOOLEAN = TypeParser.single(Boolean.class, (object) -> {
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
    public static final TypeParser<Number> NUMBER = TypeParser.number(Number.class, (object) -> {
       if (object instanceof Number) {
           return (Number) object;
       }
        try {
            return NumberFormat.getInstance().parse(String.valueOf(object));
        } catch (ParseException e) {
            throw new IllegalArgumentException(e);
        }
    });
    /**
     * Byte type parser.
     */
    public static final TypeParser<Byte> BYTE = TypeParser.number(Byte.class, (object) -> object instanceof Number ? ((Number) object).byteValue() : Byte.parseByte(String.valueOf(object)));
    /**
     * Short type parser.
     */
    public static final TypeParser<Short> SHORT = TypeParser.number(Short.class, (object) -> object instanceof Number ? ((Number) object).shortValue() : Short.parseShort(String.valueOf(object)));
    /**
     * Integer type parser.
     */
    public static final TypeParser<Integer> INT = TypeParser.number(Integer.class, (object) -> object instanceof Number ? ((Number) object).intValue() : Integer.parseInt(String.valueOf(object)));
    /**
     * Float type parser.
     */
    public static final TypeParser<Float> FLOAT = TypeParser.number(Float.class, (object) -> object instanceof Number ? ((Number) object).floatValue() : Float.parseFloat(String.valueOf(object)));
    /**
     * Long type parser.
     */
    public static final TypeParser<Long> LONG = TypeParser.number(Long.class, (object) -> object instanceof Number ? ((Number) object).longValue() : Long.parseLong(String.valueOf(object)));
    /**
     * Double type parser.
     */
    public static final TypeParser<Double> DOUBLE = TypeParser.number(Double.class, (object) -> object instanceof Number ? ((Number) object).doubleValue() : Double.parseDouble(String.valueOf(object)));
    /**
     * BigInteger type parser.<br>
     * This parser can accept any Number or String representation of BigInteger.
     */
    public static final TypeParser<BigInteger> BIG_INTEGER = TypeParser.number(BigInteger.class, (object) -> {
        if (object instanceof BigInteger) {
            return (BigInteger) object;
        } else if (object instanceof Number) {
            return BigInteger.valueOf(((Number) object).longValue());
        } else {
            return new BigInteger(String.valueOf(object));
        }
    });
    /**
     * BigDecimal type parser.<br>
     * This parser can accept any Number or string representation of BigDecimal.
     */
    public static final TypeParser<BigDecimal> BIG_DECIMAL = TypeParser.number(BigDecimal.class, (object) -> {
        if (object instanceof BigDecimal) {
            return (BigDecimal) object;
        } else if (object instanceof Number) {
            return BigDecimal.valueOf(((Number) object).doubleValue());
        } else {
            return new BigDecimal(String.valueOf(object));
        }
    });
    /**
     * Class type parser.<br>
     * This parser can accept any string representation of Class name.
     */
    public static final TypeParser<Class<?>> CLASS = TypeParser.single(Class.class, (object) -> {
        if (object instanceof Class) {
            return (Class<?>) object;
        }
        try {
            return Class.forName(String.valueOf(object));
        } catch (ClassNotFoundException e) {
            throw new IllegalArgumentException(e);
        }
    });
    /**
     * Unique ID type parser.<br>
     * This parser accepts any String representation of unique ID and also 4-length primitive int array.
     */
    public static final TypeParser<java.util.UUID> UUID = TypeParser.first(java.util.UUID.class, (object) -> {
        if (object instanceof java.util.UUID) {
            return (java.util.UUID) object;
        } else if (object instanceof int[]) {
            final int[] array = (int[]) object;
            if (array.length == 4) {
                StringBuilder builder = new StringBuilder();
                for (int i : array) {
                    String hex = Integer.toHexString(i);
                    builder.append(new String(new char[8 - hex.length()]).replace('\0', '0')).append(hex);
                }
                if (builder.length() == 32) {
                    builder.insert(20, '-').insert(16, '-').insert(12, '-').insert(8, '-');
                    return java.util.UUID.fromString(builder.toString());
                } else {
                    throw new IllegalArgumentException("The final converted UUID '" + builder + "' isn't a 32-length string");
                }
            }
        } else if (object instanceof String) {
            return java.util.UUID.fromString((String) object);
        }
        return null;
    });
    /**
     * URI type parser.
     */
    public static final TypeParser<java.net.URI> URI = TypeParser.single(java.net.URI.class, (object) -> {
        if (object instanceof java.net.URI) {
            return (java.net.URI) object;
        } else if (object instanceof File) {
            return ((File) object).toURI();
        } else if (object instanceof Path) {
            return ((Path) object).toUri();
        }
        try {
            if (object instanceof java.net.URL) {
                return ((java.net.URL) object).toURI();
            }
            return new java.net.URI(String.valueOf(object));
        } catch (URISyntaxException e) {
            throw new IllegalArgumentException(e);
        }
    });
    /**
     * URL type parser.
     */
    public static final TypeParser<java.net.URL> URL = TypeParser.single(java.net.URL.class, (object) -> {
        if (object instanceof java.net.URL) {
            return (java.net.URL) object;
        }
        try {
            if (object instanceof java.net.URI) {
                return ((java.net.URI) object).toURL();
            } else if (object instanceof File) {
                return ((File) object).toURI().toURL();
            } else if (object instanceof Path) {
                return ((Path) object).toUri().toURL();
            }
            return new java.net.URL(String.valueOf(object));
        } catch (MalformedURLException e) {
            throw new IllegalArgumentException(e);
        }
    });
    /**
     * File type parser.<br>
     * This parser can accept any String separated by {@code /} or String array.
     */
    public static final TypeParser<File> FILE = TypeParser.first(File.class, (object) -> {
        if (object instanceof File) {
            return (File) object;
        } else if (object instanceof Path) {
            return ((Path) object).toFile();
        }
        final String[] array;
        if (object instanceof String[]) {
            array = (String[]) object;
        } else {
            array = String.valueOf(object).split("/");
        }
        File file = null;
        for (String s : array) {
            file = new File(file, s);
        }
        return file;
    });
    /**
     * Path type parser.<br>
     * This parser can accept any String separated by {@code /} or String array.
     */
    public static final TypeParser<Path> PATH = TypeParser.first(Path.class, (object) -> {
        if (object instanceof Path) {
            return (Path) object;
        } else if (object instanceof File) {
            return ((File) object).toPath();
        }
        final String first;
        String[] more;
        if (object instanceof String[]) {
            more = (String[]) object;
        } else {
            more = String.valueOf(object).split("/");
        }
        first = more[0];
        if (more.length > 1) {
            more = Arrays.copyOfRange(more, 1, more.length);
        } else {
            more = new String[0];
        }
        return Paths.get(first, more);
    });
    /**
     * LocalDate type parser.<br>
     * This parser accept any ISO-8601 String or Number array with date values.
     *
     * @see LocalDate#parse(CharSequence)
     * @see LocalDate#ofYearDay(int, int)
     * @see LocalDate#of(int, int, int)
     * @see LocalDate#ofEpochDay(long)
     */
    public static final TypeParser<LocalDate> LOCAL_DATE = TypeParser.first(LocalDate.class, (object) -> {
        if (object.getClass().isArray()) {
            final int size = Array.getLength(object);
            if (size >= 2) {
                final Object year = Array.get(object, 0);
                if (year instanceof Number) {
                    if (size == 2) {
                        return LocalDate.ofYearDay(((Number) year).intValue(), ((Number) Array.get(object, 1)).intValue());
                    } else {
                        return LocalDate.of(((Number) year).intValue(), ((Number) Array.get(object, 1)).intValue(), ((Number) Array.get(object, 2)).intValue());
                    }
                }
            }
            return null;
        } else if (object instanceof Number) {
            return LocalDate.ofEpochDay(((Number) object).longValue());
        } else {
            return LocalDate.parse(String.valueOf(object));
        }
    });
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
    public static TypeParser<LocalTime> LOCAL_TIME = TypeParser.first(LocalTime.class, (object) -> {
        if (object.getClass().isArray()) {
            final int size = Array.getLength(object);
            if (size >= 2) {
                final Object year = Array.get(object, 0);
                if (year instanceof Number) {
                    if (size == 2) {
                        return LocalTime.of(((Number) year).intValue(), ((Number) Array.get(object, 1)).intValue());
                    } else if (size == 3) {
                        return LocalTime.of(((Number) year).intValue(), ((Number) Array.get(object, 1)).intValue(), ((Number) Array.get(object, 2)).intValue());
                    } else {
                        return LocalTime.of(
                                ((Number) year).intValue(),
                                ((Number) Array.get(object, 1)).intValue(),
                                ((Number) Array.get(object, 2)).intValue(),
                                ((Number) Array.get(object, 3)).intValue());
                    }
                }
            }
            return null;
        } else if (object instanceof Number) {
            return LocalTime.ofSecondOfDay(((Number) object).longValue());
        } else {
            return LocalTime.parse(String.valueOf(object));
        }
    });
    /**
     * LocalDateTime parser type.<br>
     * This parser accept any ISO-8601 String separated by {@code T} with time format {@code hour:minute:second.nanoOfSecond}
     * or Number array with date time values.
     * 
     * @see LocalDateTime#parse(CharSequence)
     * @see LocalDateTime#of(int, int, int, int, int)
     * @see LocalDateTime#of(int, int, int, int, int, int)
     * @see LocalDateTime#of(int, Month, int, int, int, int, int)
     */
    public static final TypeParser<LocalDateTime> LOCAL_DATE_TIME = TypeParser.first(LocalDateTime.class, (object) -> {
        if (object.getClass().isArray()) {
            final int size = Array.getLength(object);
            if (size >= 5) {
                final Object year = Array.get(object, 0);
                if (year instanceof Number) {
                    if (size == 5) {
                        return LocalDateTime.of(((Number) year).intValue(),
                                ((Number) Array.get(object, 1)).intValue(),
                                ((Number) Array.get(object, 2)).intValue(),
                                ((Number) Array.get(object, 3)).intValue(),
                                ((Number) Array.get(object, 4)).intValue());
                    } else if (size == 6) {
                        return LocalDateTime.of(((Number) year).intValue(),
                                ((Number) Array.get(object, 1)).intValue(),
                                ((Number) Array.get(object, 2)).intValue(),
                                ((Number) Array.get(object, 3)).intValue(),
                                ((Number) Array.get(object, 4)).intValue(),
                                ((Number) Array.get(object, 5)).intValue());
                    } else {
                        return LocalDateTime.of(((Number) year).intValue(),
                                ((Number) Array.get(object, 1)).intValue(),
                                ((Number) Array.get(object, 2)).intValue(),
                                ((Number) Array.get(object, 3)).intValue(),
                                ((Number) Array.get(object, 4)).intValue(),
                                ((Number) Array.get(object, 5)).intValue(),
                                ((Number) Array.get(object, 6)).intValue());
                    }
                }
            }
            return null;
        } else {
            return LocalDateTime.parse(String.valueOf(object));
        }
    });
    /**
     * Map of objects type parser.<br>
     * This is the most typical Map format to save data.
     *
     * @see TypeParser#map(TypeParser, TypeParser, Supplier) Custom map type parser.
     */
    public static final TypeParser<Map<String, Object>> MAP = TypeParser.map(STRING, OBJECT, HashMap::new);

    static {
        add(Object.class, OBJECT);
        add("text", TEXT);
        add(String.class, STRING);
        add(Character.class, CHAR);
        add(char.class, TypeParser.of(char.class, (object) -> {
            final Character c = CHAR.parse(object);
            return c != null ? c : '\0';
        }));
        add(Boolean.class, BOOLEAN);
        add(boolean.class, TypeParser.of(boolean.class, (object) -> {
            final Boolean bool = BOOLEAN.parse(object);
            return bool != null ? bool : Boolean.FALSE;
        }));
        add(Number.class, NUMBER);
        add(Byte.class, BYTE);
        add(byte.class, TypeParser.of(byte.class, object -> {
            final Byte num = BYTE.parse(object);
            return num != null ? num : Byte.MIN_VALUE;
        }));
        add(Short.class, SHORT);
        add(short.class, TypeParser.of(short.class, object -> {
            final Short num = SHORT.parse(object);
            return num != null ? num : Short.MIN_VALUE;
        }));
        add(Integer.class, INT);
        add(int.class, TypeParser.of(int.class, object -> {
            final Integer num = INT.parse(object);
            return num != null ? num : Integer.MIN_VALUE;
        }));
        add(Float.class, FLOAT);
        add(float.class, TypeParser.of(float.class, object -> {
            final Float num = FLOAT.parse(object);
            return num != null ? num : Float.MIN_VALUE;
        }));
        add(Long.class, LONG);
        add(long.class, TypeParser.of(long.class, object -> {
            final Long num = LONG.parse(object);
            return num != null ? num : Long.MIN_VALUE;
        }));
        add(Double.class, DOUBLE);
        add(double.class, TypeParser.of(double.class, object -> {
            final Double num = DOUBLE.parse(object);
            return num != null ? num : Double.MIN_VALUE;
        }));
        add(BigInteger.class, BIG_INTEGER);
        add(BigDecimal.class, BIG_DECIMAL);
        add(Class.class, CLASS);
        add(java.util.UUID.class, UUID);
        add(java.net.URI.class, URI);
        add(java.net.URL.class, URL);
        add(File.class, FILE);
        add(Path.class, PATH);
        add(LocalDate.class, LOCAL_DATE);
        add(LocalTime.class, LOCAL_TIME);
        add(LocalDateTime.class, LOCAL_DATE_TIME);
        add(Map.class, MAP);
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
    public static <T> TypeParser<?> add(@NotNull Class<T> type, @NotNull TypeParser<T> parser) {
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
    public static <T> TypeParser<?> add(@NotNull Object type, @NotNull TypeParser<T> parser) {
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
