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

        final String[] parts;
        if (first instanceof String[]) {
            parts = (String[]) first;
        } else {
            parts = String.valueOf(first).split("AND|&&?");
        }

        Duration result = null;
        for (String part : parts) {
            final String[] split = part.trim().split(" ");
            if (split.length < 2) {
                throw new IllegalArgumentException();
            }
            if (!split[1].toUpperCase().endsWith("S")) {
                split[1] = split[1] + "S";
            }

            try {
                final String time = split[0].trim();
                final TimeUnit unit = TimeUnit.valueOf(split[1].trim().toUpperCase());

                final int point = time.indexOf('.');
                final Duration duration;
                if (point >= 0) {
                    duration = toDuration(Double.parseDouble(time), unit);
                } else {
                    duration = toDuration(Long.parseLong(time), unit);
                }

                if (result == null) {
                    result = duration;
                } else {
                    result = result.plus(duration);
                }
            } catch (IllegalArgumentException | NullPointerException e) {
                throw e;
            } catch (Throwable t) {
                throw new IllegalArgumentException(t);
            }
        }
        return result;
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

    /**
     * Convert given time and unit to Duration instance.
     *
     * @param time the time value.
     * @param unit the time unit.
     * @return     a duration instance.
     */
    @NotNull
    public static Duration toDuration(double time, @NotNull TimeUnit unit) {
        switch (unit) {
            case NANOSECONDS:
                return Duration.ofNanos(Math.round(time));
            case MICROSECONDS:
                return Duration.ofNanos(Math.round(time * 1_000));
            case MILLISECONDS:
                return Duration.ofMillis(Math.round(time));
            case SECONDS:
                return Duration.ofSeconds((long) time, Math.round((time - (long) time) * 1_000_000_000));
            case MINUTES:
                return Duration.ofSeconds(Math.round(time * 60));
            case HOURS:
                return Duration.ofSeconds(Math.round(time * 3_600));
            case DAYS:
                return Duration.ofSeconds(Math.round(time * 86_400));
            default:
                throw new IllegalArgumentException();
        }
    }
}
