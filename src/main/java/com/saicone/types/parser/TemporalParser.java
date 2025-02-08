package com.saicone.types.parser;

import com.saicone.types.IterableType;
import com.saicone.types.TypeParser;
import com.saicone.types.Types;
import com.saicone.types.iterator.ArrayIterator;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.Month;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.time.temporal.Temporal;

/**
 * Represents an interface that try to parse any object type to temporal object instance.<br>
 * This parser is also compatible with Number, formatted String and array representation of
 * constructor arguments for required temporal type object.
 *
 * @author Rubenicos
 *
 * @param <T> the type result.
 */
public interface TemporalParser<T extends Temporal> extends TypeParser<T> {

    /**
     * {@link TemporalParser} for LocalDate values.<br>
     * This parser accept any ISO-8601 String or Number array with date values.
     *
     * @see LocalDate#parse(CharSequence)
     * @see LocalDate#ofYearDay(int, int)
     * @see LocalDate#of(int, int, int)
     * @see LocalDate#ofEpochDay(long)
     */
    TemporalParser<LocalDate> LOCAL_DATE = new TemporalParser<LocalDate>() {
        @Override
        public @NotNull Type getType() {
            return LocalDate.class;
        }

        @Override
        public @NotNull LocalDate parseTemporal(@NotNull Number number) {
            return LocalDate.ofEpochDay(number.longValue());
        }

        @Override
        public @Nullable LocalDate parseTemporal(@NotNull ArrayIterator<Integer> iterator) {
            if (iterator.size() == 2) {
                return LocalDate.ofYearDay(iterator.next(), iterator.next());
            } else if (iterator.size() == 3) {
                return LocalDate.of(iterator.next(), iterator.next(), iterator.next());
            }
            return null;
        }

        @Override
        public @NotNull LocalDate parseTemporal(@NotNull String s) {
            return LocalDate.parse(s);
        }
    };

    /**
     * {@link TemporalParser} for LocalTime values.<br>
     * This parser accept any String with the format {@code hour:minute:second.nanoOfSecond}
     * or Number array with time values.
     *
     * @see LocalTime#parse(CharSequence)
     * @see LocalTime#ofSecondOfDay(long)
     * @see LocalTime#of(int, int)
     * @see LocalTime#of(int, int, int)
     * @see LocalTime#of(int, int, int, int)
     */
    TemporalParser<LocalTime> LOCAL_TIME = new TemporalParser<LocalTime>() {
        @Override
        public @NotNull Type getType() {
            return LocalTime.class;
        }

        @Override
        public @NotNull LocalTime parseTemporal(@NotNull Number number) {
            return LocalTime.ofSecondOfDay(number.longValue());
        }

        @Override
        public @Nullable LocalTime parseTemporal(@NotNull ArrayIterator<Integer> iterator) {
            switch (iterator.size()) {
                case 2:
                    return LocalTime.of(iterator.next(), iterator.next());
                case 3:
                    return LocalTime.of(iterator.next(), iterator.next(), iterator.next());
                case 4:
                    return LocalTime.of(iterator.next(), iterator.next(), iterator.next(), iterator.next());
                default:
                    return null;
            }
        }

        @Override
        public @NotNull LocalTime parseTemporal(@NotNull String s) {
            return LocalTime.parse(s);
        }
    };

    /**
     * {@link TemporalParser} for LocalDateTime values.<br>
     * This parser accept any ISO-8601 String separated by {@code T} with time format {@code hour:minute:second.nanoOfSecond}
     * or Number array with date time values.
     *
     * @see LocalDateTime#parse(CharSequence)
     * @see LocalDateTime#ofEpochSecond(long, int, ZoneOffset)
     * @see LocalDateTime#of(int, int, int, int, int)
     * @see LocalDateTime#of(int, int, int, int, int, int)
     * @see LocalDateTime#of(int, Month, int, int, int, int, int)
     */
    TemporalParser<LocalDateTime> LOCAL_DATE_TIME = new TemporalParser<LocalDateTime>() {
        @Override
        public @NotNull Type getType() {
            return LocalDateTime.class;
        }

        @Override
        public @NotNull LocalDateTime parseTemporal(@NotNull Number number) {
            return LocalDateTime.ofEpochSecond(number.longValue(), 0, ZonedDateTime.now().getOffset());
        }

        @Override
        public @Nullable LocalDateTime parseTemporal(@NotNull ArrayIterator<Integer> iterator) {
            switch (iterator.size()) {
                case 5:
                    return LocalDateTime.of(iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next());
                case 6:
                    return LocalDateTime.of(iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next());
                case 7:
                    return LocalDateTime.of(iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next(), iterator.next());
                default:
                    return null;
            }
        }

        @Override
        public @NotNull LocalDateTime parseTemporal(@NotNull String s) {
            return LocalDateTime.parse(s);
        }
    };

    @Override
    default @Nullable T parse(@NotNull Object object) {
        final Object single = IterableType.of(object).single();
        if (single == null) {
            return null;
        }

        if (single instanceof Number) {
            return parseTemporal((Number) single);
        } else if (single instanceof Object[] || single.getClass().isArray()) {
            return parseTemporal(ArrayIterator.of(single, Types.INTEGER));
        } else {
            return parseTemporal(String.valueOf(single));
        }
    }

    /**
     * Parses the given number argument as required temporal type.
     *
     * @param number the number to parse.
     * @return       a converted temporal type.
     */
    @NotNull
    T parseTemporal(@NotNull Number number);

    /**
     * Parses the given int array iterator as required temporal type.
     *
     * @param iterator the iterator to parse.
     * @return         a converted temporal type if the array is valid, null otherwise.
     */
    @Nullable
    T parseTemporal(@NotNull ArrayIterator<Integer> iterator);

    /**
     * Parses the given formatted string argument as required temporal type.
     *
     * @param s the string to parse.
     * @return  a converted temporal type.
     */
    @NotNull
    T parseTemporal(@NotNull String s);
}
