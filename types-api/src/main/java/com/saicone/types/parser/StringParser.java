package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

import java.lang.reflect.Type;
import java.text.DecimalFormat;
import java.util.Arrays;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.StringJoiner;
import java.util.function.Function;
import java.util.function.Predicate;

/**
 * Represents a parser that try to convert any object type to String.<br>
 * An additional parser is provided to compose multiple formatters for specific object types.
 *
 * @author Rubenicos
 */
public class StringParser implements TypeParser<String> {

    /**
     * {@link StringParser} public instance.
     */
    public static final StringParser INSTANCE = new StringParser();

    /**
     * Create a new composed {@link StringParser} instance.
     *
     * @return a newly generated composed string parser instance
     */
    @NotNull
    public static Composed composed() {
        return new Composed();
    }

    @Override
    public @NotNull Type getType() {
        return String.class;
    }

    @Override
    public @NotNull String parse(@NotNull Object object) throws Throwable {
        final Object first = AnyIterable.first(object);
        Objects.requireNonNull(first, "Cannot get effective first element");

        return first.toString();
    }

    /**
     * Represents a composed parser that try to convert any object type to String using multiple formatters.
     */
    public static class Composed extends StringParser {

        private LinkedHashMap<Predicate<Object>, Function<Object, String>> formatters;

        /**
         * Add a formatter for the given predicate.<br>
         * The predicate will be used to check if the formatter is applicable for the given object.
         *
         * @param predicate the predicate to check if the formatter is applicable
         * @param formatter the formatter to use for the given object
         * @return          this composed parser instance
         */
        @NotNull
        @Contract("_, _ -> this")
        public Composed format(@NotNull Predicate<Object> predicate, @NotNull Function<Object, String> formatter) {
            if (formatters == null) {
                formatters = new LinkedHashMap<>();
            }
            formatters.put(predicate, formatter);
            return this;
        }

        /**
         * Add default String formatters for arrays.<br>
         * Compatible with both Object arrays and primitive arrays.
         *
         * @return this composed parser instance
         */
        @NotNull
        @Contract("-> this")
        public Composed formatArrays() {
            format(object -> object instanceof Object[], object -> Arrays.toString((Object[]) object));
            format(object -> object.getClass().isArray(), object -> {
                final StringJoiner joiner = new StringJoiner(", ", "[", "]");
                for (Object o : AnyIterable.of(object)) {
                    joiner.add(String.valueOf(o));
                }
                return joiner.toString();
            });
            return this;
        }

        /**
         * Add a formatter for decimal numbers using the given pattern.<br>
         * The pattern is used to create a {@link DecimalFormat} instance.
         *
         * @param pattern the pattern to use for decimal formatting
         * @return        this composed parser instance
         */
        @NotNull
        @Contract("_ -> this")
        public Composed formatDecimal(@NotNull String pattern) {
            final DecimalFormat format = new DecimalFormat(pattern);
            format(object -> object instanceof Number, format::format);
            return this;
        }

        @Override
        public @NotNull String parse(@NotNull Object object) throws Throwable {
            if (formatters != null) {
                for (Map.Entry<Predicate<Object>, Function<Object, String>> entry : formatters.entrySet()) {
                    if (entry.getKey().test(object)) {
                        return entry.getValue().apply(object);
                    }
                }
            }
            return super.parse(object);
        }
    }
}
