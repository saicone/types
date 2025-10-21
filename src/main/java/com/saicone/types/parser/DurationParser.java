package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.time.Duration;
import java.util.concurrent.TimeUnit;

/**
 * Represents a parser that try to convert any object type to Duration.<br>
 * This parser is also compatible with String representations like
 * {@code "<number> <time unit>"} (e.g., {@code "5 SECONDS"}, {@code "10 DAYS"}).
 *
 * @author Rubenicos
 */
public class DurationParser implements TypeParser<Duration> {

    /**
     * {@link DurationParser} public instance.
     */
    public static final DurationParser INSTANCE = new DurationParser();

    @Override
    public @Nullable Duration parse(@NotNull Object object) {
        final Object first = AnyIterable.of(object).first();
        if (first == null) {
            return null;
        }

        if (first instanceof Duration) {
            return (Duration) first;
        }

        final String[] split = String.valueOf(first).split(" ");
        if (split.length < 2) {
            throw new IllegalArgumentException();
        }
        if (!split[1].toUpperCase().endsWith("S")) {
            split[1] = split[1] + "S";
        }

        try {
            final long time = Long.parseLong(split[0]);
            final TimeUnit unit = TimeUnit.valueOf(split[1].toUpperCase());
            return toDuration(time, unit);
        } catch (Throwable t) {
            throw new IllegalArgumentException(t);
        }
    }

    /**
     * Convert given time and unit to Duration instance.
     *
     * @param time the time value.
     * @param unit the time unit.
     * @return     a duration instance.
     */
    @NotNull
    public static Duration toDuration(long time, @NotNull TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS:
                return Duration.ofNanos(time);
            case MICROSECONDS:
                return Duration.ofNanos(TimeUnit.MICROSECONDS.toNanos(time));
            case MILLISECONDS:
                return Duration.ofMillis(time);
            case SECONDS:
                return Duration.ofSeconds(time);
            case MINUTES:
                return Duration.ofMinutes(time);
            case HOURS:
                return Duration.ofHours(time);
            case DAYS:
                return Duration.ofDays(time);
            default:
                throw new IllegalArgumentException();
        }
    }
}
