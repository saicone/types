package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.util.Objects;

/**
 * Represents a parser that try to convert any object type to Boolean.<br>
 * This parser is also compatible with any boolean representation from {@link Object#toString()}
 * and also interpret decimal values between ZERO and ONE as {@code true} following elasticity
 * check from mathematical functions.
 *
 * @author Rubenicos
 */
public class BooleanParser implements TypeParser<Boolean> {

    /**
     * {@link BooleanParser} public instance.
     */
    public static final BooleanParser INSTANCE = new BooleanParser();

    /**
     * Check if the provided string is a valid boolean representation.<br>
     * This method doesn't check for number representations.
     *
     * @param s the string to check
     * @return  true if the provided string is a valid boolean, false otherwise
     */
    public static boolean isValid(@NotNull String s) {
        switch (s.trim().toLowerCase()) {
            case "true":
            case "t":
            case "yes":
            case "on":
            case "y":
            case "false":
            case "f":
            case "no":
            case "off":
            case "n":
                return true;
            default:
                return false;
        }
    }

    @Override
    public @Nullable Type getType() {
        return Boolean.class;
    }

    @Override
    public @NotNull Boolean parse(@NotNull Object object) throws Throwable {
        final Object first = AnyIterable.first(object);
        Objects.requireNonNull(first, "Cannot get effective first element");

        if (first instanceof Boolean) {
            return (Boolean) first;
        } else if (first instanceof Number) {
            return parseBoolean((Number) first);
        } else {
            return parseBoolean(String.valueOf(first));
        }
    }

    /**
     * Parse the given Number as Boolean using ZERO as {@code false} and ONE as {@code true}.<br>
     * This method also interpret decimal values between ZERO and ONE as {@code true}
     * following elasticity check from mathematical functions.
     *
     * @param number the number to parse
     * @return       a converted Boolean value if Number is valid
     * @throws IllegalArgumentException if the Number is not a valid representation
     */
    public static boolean parseBoolean(@NotNull Number number) throws IllegalArgumentException {
        if (number instanceof BigDecimal) {
            final BigDecimal decimal = (BigDecimal) number;
            if (decimal.equals(BigDecimal.ZERO)) {
                return false;
            } else if (decimal.compareTo(BigDecimal.ZERO) > 0 && decimal.compareTo(BigDecimal.ONE) <= 0) {
                return true;
            }
        } else if (number instanceof Double) {
            if ((Double) number == 0d) {
                return false;
            } else if ((Double) number > 0d && (Double) number <= 1d) {
                return true;
            }
        } else if (number instanceof Float) {
            if ((Float) number == 0f) {
                return false;
            } else if ((Float) number > 0f && (Float) number <= 1f) {
                return true;
            }
        } else {
            final long value = number.longValue();
            if (value == 0) {
                return false;
            } else if (value == 1) {
                return true;
            }
        }
        throw new IllegalArgumentException("Invalid boolean representation: " + number);
    }

    /**
     * Parse the given String as Number.<br>
     * This method accept any String representation of:<br>
     * {@code true | false}<br>
     * {@code t | f}<br>
     * {@code 1 | 0}<br>
     * {@code yes | no}<br>
     * {@code on | off}<br>
     * {@code y | n}
     *
     * @param s the string to parse
     * @return  a converted Boolean value if String is a valid representations
     * @throws IllegalArgumentException if the String is not a valid representation
     */
    public static boolean parseBoolean(@NotNull String s) throws IllegalArgumentException {
        switch (s.trim().toLowerCase()) {
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
                throw new IllegalArgumentException("Invalid boolean representation: " + s);
        }
    }
}
