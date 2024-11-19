package com.saicone.types.parser;

import com.saicone.types.IterableType;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.lang.reflect.Type;
import java.math.BigDecimal;
import java.math.BigInteger;
import java.util.function.BiFunction;
import java.util.function.Function;

/**
 * Represents a function that try to parse any type of object to number type.<br>
 * This parser is also compatible with boolean and some popular string representation
 * of numbers like binary ({@code 0[bB][0-1]}), hex ({@code 0[xX#][0-9A-Fa-f]}), octal ({@code 0[0-7]}),
 * leading sings ({@code + -}), unsigned suffix ({@code u}) and number suffixes ({@code b B s S i I f F l L d D}).<br>
 * Including safe-checks for min and max values, and safe conversion with big numbers.
 *
 * @author Rubenicos
 *
 * @param <T> the type result of the function.
 */
@FunctionalInterface
public interface NumberParser<T extends Number> extends TypeParser<T> {

    /**
     * Number type parser.
     */
    NumberParser<Number> NUMBER = new NumberParser<Number>() {
        @Override
        public @NotNull Type getType() {
            return Number.class;
        }

        @Override
        public boolean isBigHolder() {
            return true;
        }

        @Override
        public boolean isDecimalHolder() {
            return true;
        }

        @Override
        public @NotNull Number parseNumber(@NotNull String s) throws NumberFormatException {
            return parseNumber(s, (parser, str) -> parser.parseNumber(s));
        }

        @Override
        public @NotNull Number parseNumber(@NotNull String s, int radix) throws NumberFormatException {
            return parseNumber(s, (parser, str) -> parser.parseNumber(s, radix));
        }

        @Override
        public @NotNull Number parseUnsignedNumber(@NotNull String s) throws NumberFormatException {
            return parseNumber(s, (parser, str) -> parser.parseUnsignedNumber(s));
        }

        @Override
        public @NotNull Number parseUnsignedNumber(@NotNull String s, int radix) throws NumberFormatException {
            return parseNumber(s, (parser, str) -> parser.parseUnsignedNumber(s, radix));
        }

        @NotNull
        private Number parseNumber(@NotNull String s, @NotNull BiFunction<NumberParser<? extends Number>, String, Number> function) {
            final char last = s.charAt(s.length() - 1);
            if (Character.isDigit(last)) {
                final int point = s.indexOf('.');
                final char first = s.charAt(0);
                final int start = first == '+' || first == '-' ? 1 : 0;

                if (point > 0) {
                    if (point - start > 309 || s.length() - point + 1 > 17) {
                        return function.apply(BIG_DECIMAL, s);
                    }
                    return function.apply(DOUBLE, s);
                } else {
                    if (s.length() - start > 19) {
                        return function.apply(BIG_INTEGER, s);
                    }
                    return function.apply(LONG, s);
                }
            } else {
                s = s.substring(0, s.length() - 1);
            }

            switch (last) {
                case 'b':
                case 'B':
                    return function.apply(BYTE, s);
                case 's':
                case 'S':
                    return function.apply(SHORT, s);
                case 'i':
                case 'I':
                    return function.apply(INTEGER, s);
                case 'f':
                case 'F':
                    return function.apply(FLOAT, s);
                case 'l':
                case 'L':
                    return function.apply(LONG, s);
                case 'd':
                case 'D':
                    return function.apply(DOUBLE, s);
                default:
                    throw new NumberFormatException("For input string: \"" + s + "\"");
            }
        }
    };

    /**
     * Byte type parser.
     */
    NumberParser<Byte> BYTE = new NumberParser<Byte>() {
        @Override
        public @NotNull Type getType() {
            return Byte.class;
        }

        @Override
        public char getSuffix() {
            return 'b';
        }

        @Override
        public @NotNull Byte getMinValue() {
            return Byte.MIN_VALUE;
        }

        @Override
        public @NotNull Byte getMaxValue() {
            return Byte.MAX_VALUE;
        }

        @Override
        public @NotNull Byte cast(@NotNull Number number) {
            return number.byteValue();
        }

        @Override
        public @NotNull Byte parseNumber(@NotNull String s) throws NumberFormatException {
            return Byte.parseByte(s);
        }

        @Override
        public @NotNull Byte parseNumber(@NotNull String s, int radix) throws NumberFormatException {
            return Byte.parseByte(s, radix);
        }
    };

    /**
     * Short type parser.
     */
    NumberParser<Short> SHORT = new NumberParser<Short>() {
        @Override
        public @NotNull Type getType() {
            return Short.class;
        }

        @Override
        public char getSuffix() {
            return 's';
        }

        @Override
        public @NotNull Short getMinValue() {
            return Short.MIN_VALUE;
        }

        @Override
        public @NotNull Short getMaxValue() {
            return Short.MAX_VALUE;
        }

        @Override
        public @NotNull Short cast(@NotNull Number number) {
            return number.shortValue();
        }

        @Override
        public @NotNull Short parseNumber(@NotNull String s) throws NumberFormatException {
            return Short.parseShort(s);
        }

        @Override
        public @NotNull Short parseNumber(@NotNull String s, int radix) throws NumberFormatException {
            return Short.parseShort(s, radix);
        }
    };

    /**
     * Integer type parser.
     */
    NumberParser<Integer> INTEGER = new NumberParser<Integer>() {
        @Override
        public @NotNull Type getType() {
            return Integer.class;
        }

        @Override
        public char getSuffix() {
            return 'i';
        }

        @Override
        public @NotNull Integer getMinValue() {
            return Integer.MIN_VALUE;
        }

        @Override
        public @NotNull Integer getMaxValue() {
            return Integer.MAX_VALUE;
        }

        @Override
        public @NotNull Integer cast(@NotNull Number number) {
            return number.intValue();
        }

        @Override
        public @NotNull Integer parseNumber(@NotNull String s) throws NumberFormatException {
            return Integer.parseInt(s);
        }

        @Override
        public @NotNull Integer parseNumber(@NotNull String s, int radix) throws NumberFormatException {
            return Integer.parseInt(s, radix);
        }

        @Override
        public @NotNull Integer parseUnsignedNumber(@NotNull String s) throws NumberFormatException {
            return Integer.parseUnsignedInt(s);
        }

        @Override
        public @NotNull Integer parseUnsignedNumber(@NotNull String s, int radix) throws NumberFormatException {
            return Integer.parseUnsignedInt(s, radix);
        }
    };

    /**
     * Float type parser.
     */
    NumberParser<Float> FLOAT = new NumberParser<Float>() {
        @Override
        public @NotNull Type getType() {
            return Float.class;
        }

        @Override
        public char getSuffix() {
            return 'f';
        }

        @Override
        public @NotNull Float getMinValue() {
            return Float.MIN_VALUE;
        }

        @Override
        public @NotNull Float getMaxValue() {
            return Float.MAX_VALUE;
        }

        @Override
        public boolean isDecimalHolder() {
            return true;
        }

        @Override
        public @NotNull Float cast(@NotNull Number number) {
            return number.floatValue();
        }

        @Override
        public @NotNull Float parseNumber(@NotNull String s) throws NumberFormatException {
            return Float.parseFloat(s);
        }

        @Override
        public @NotNull Float parseNumber(@NotNull String s, int radix) throws NumberFormatException {
            if (radix == 10) {
                return parseNumber(s);
            }
            return cast(INTEGER.parseNumber(s, radix));
        }
    };

    /**
     * Long type parser.
     */
    NumberParser<Long> LONG = new NumberParser<Long>() {
        @Override
        public @NotNull Type getType() {
            return Long.class;
        }

        @Override
        public char getSuffix() {
            return 'l';
        }

        @Override
        public @NotNull Long getMinValue() {
            return Long.MIN_VALUE;
        }

        @Override
        public @NotNull Long getMaxValue() {
            return Long.MAX_VALUE;
        }

        @Override
        public @NotNull Long cast(@NotNull Number number) {
            return number.longValue();
        }

        @Override
        public @NotNull Long parseNumber(@NotNull String s) throws NumberFormatException {
            return Long.parseLong(s);
        }

        @Override
        public @NotNull Long parseNumber(@NotNull String s, int radix) throws NumberFormatException {
            return Long.parseLong(s, radix);
        }

        @Override
        public @NotNull Long parseUnsignedNumber(@NotNull String s) throws NumberFormatException {
            return Long.parseUnsignedLong(s);
        }

        @Override
        public @NotNull Long parseUnsignedNumber(@NotNull String s, int radix) throws NumberFormatException {
            return Long.parseUnsignedLong(s, radix);
        }
    };

    /**
     * Double type parser.
     */
    NumberParser<Double> DOUBLE = new NumberParser<Double>() {
        @Override
        public @NotNull Type getType() {
            return Double.class;
        }

        @Override
        public char getSuffix() {
            return 'd';
        }

        @Override
        public @NotNull Double getMinValue() {
            return Double.MIN_VALUE;
        }

        @Override
        public @NotNull Double getMaxValue() {
            return Double.MAX_VALUE;
        }

        @Override
        public boolean isDecimalHolder() {
            return true;
        }

        @Override
        public @NotNull Double cast(@NotNull Number number) {
            return number.doubleValue();
        }

        @Override
        public @NotNull Double parseNumber(@NotNull String s) throws NumberFormatException {
            return Double.parseDouble(s);
        }
    };

    /**
     * BigInteger type parser.
     */
    NumberParser<BigInteger> BIG_INTEGER = new NumberParser<BigInteger>() {
        @Override
        public @NotNull Type getType() {
            return BigInteger.class;
        }

        @Override
        public boolean isBigHolder() {
            return true;
        }

        @Override
        public @NotNull BigInteger cast(@NotNull Number number) {
            if (number instanceof BigInteger) {
                return (BigInteger) number;
            } else if (number instanceof BigDecimal) {
                return ((BigDecimal) number).toBigInteger();
            } else {
                return BigInteger.valueOf(number.longValue());
            }
        }

        @Override
        public @NotNull BigInteger parseNumber(boolean bool) {
            return bool ? BigInteger.ONE : BigInteger.ZERO;
        }

        @Override
        public @NotNull BigInteger parseNumber(@NotNull String s) throws NumberFormatException {
            return new BigInteger(s);
        }

        @Override
        public @NotNull BigInteger parseNumber(@NotNull String s, int radix) throws NumberFormatException {
            return new BigInteger(s, radix);
        }
    };

    /**
     * BigDecimal type parser.
     */
    NumberParser<BigDecimal> BIG_DECIMAL = new NumberParser<BigDecimal>() {
        @Override
        public @NotNull Type getType() {
            return BigDecimal.class;
        }

        @Override
        public boolean isBigHolder() {
            return true;
        }

        @Override
        public boolean isDecimalHolder() {
            return true;
        }

        @Override
        public @NotNull BigDecimal cast(@NotNull Number number) {
            if (number instanceof BigDecimal) {
                return (BigDecimal) number;
            } else if (number instanceof BigInteger) {
                return new BigDecimal((BigInteger) number);
            } else {
                return BigDecimal.valueOf(number.longValue());
            }
        }

        @Override
        public @NotNull BigDecimal parseNumber(boolean bool) {
            return bool ? BigDecimal.ONE : BigDecimal.ZERO;
        }

        @Override
        public @NotNull BigDecimal parseNumber(@NotNull String s) throws NumberFormatException {
            return new BigDecimal(s);
        }

        @Override
        public @NotNull BigDecimal parseNumber(@NotNull String s, int radix) throws NumberFormatException {
            return new BigDecimal(new BigInteger(s, radix));
        }
    };

    /**
     * Crate a number type parser with associated type that return a number type.
     *
     * @param type           the associated type with the parser.
     * @param suffix         the associated char suffix with the parser.
     * @param min            the minimum allowed value.
     * @param max            the maximum allowed value.
     * @param numberFunction the function that cast any number type to required number.
     * @param stringFunction the function that convert any string with associated radix to required number.
     * @return               a type parser that return a number type.
     * @param <T>            the number type result of the function.
     */
    @NotNull
    static <T extends Number> NumberParser<T> of(@Nullable Type type, char suffix, @NotNull T min, @NotNull T max, @NotNull Function<Number, T> numberFunction, @NotNull BiFunction<String, Integer, T> stringFunction) {
        return new NumberParser<T>() {
            @Override
            public @Nullable Type getType() {
                return type;
            }

            @Override
            public char getSuffix() {
                return suffix;
            }

            @Override
            public @NotNull T getMinValue() {
                return min;
            }

            @Override
            public @NotNull T getMaxValue() {
                return max;
            }

            @Override
            public boolean isBigHolder() {
                return min instanceof BigInteger || min instanceof BigDecimal;
            }

            @Override
            public boolean isDecimalHolder() {
                return min instanceof Float || min instanceof Double || min instanceof BigDecimal;
            }

            @Override
            public @NotNull T cast(@NotNull Number number) {
                return numberFunction.apply(number);
            }

            @Override
            public @NotNull T parseNumber(@NotNull String s) throws NumberFormatException {
                return stringFunction.apply(s, 10);
            }

            @Override
            public @NotNull T parseNumber(@NotNull String s, int radix) throws NumberFormatException {
                return stringFunction.apply(s, radix);
            }
        };
    }

    /**
     * Get the lowercase number suffix represented by this parser.
     *
     * @return a number suffix character.
     */
    default char getSuffix() {
        return '\0';
    }

    /**
     * Get the minimum supported value by this parser.
     *
     * @return a minimum value number, 0 if this parser allows any value.
     */
    @NotNull
    default T getMinValue() {
        return cast(0);
    }

    /**
     * Get the maximum supported value by this parser.
     *
     * @return a maximum value number, 0 if this parser allows any value.
     */
    @NotNull
    default T getMaxValue() {
        return cast(0);
    }

    /**
     * Check if this parser is for big numbers such as BigInteger or BigDecimal.
     *
     * @return true if this parser hold big numbers.
     */
    default boolean isBigHolder() {
        return false;
    }

    /**
     * Check if this parser allows decimal numbers such as BigDecimal, Double or Float.
     *
     * @return true if this parser can hold decimal numbers.
     */
    default boolean isDecimalHolder() {
        return false;
    }

    /**
     * Cast any number to required number type.
     *
     * @param number a number object.
     * @return       a converted number type, can generate an exception.
     */
    @NotNull
    @SuppressWarnings("unchecked")
    default T cast(@NotNull Number number) {
        return (T) number;
    }

    @Override
    default @Nullable T parse(@NotNull Object object) {
        final Object first = IterableType.of(object).first();
        if (first == null) {
            return null;
        }
        if (first instanceof Number) {
            return parseNumber((Number) first);
        } else if (first instanceof Boolean) {
            return parseNumber((Boolean) first);
        } else {
            return parse(String.valueOf(first));
        }
    }

    /**
     * Parses the given string argument as required number type.<br>
     * This method allows various types of number formats.
     *
     * @param s the string to parse.
     * @return  a converted number type.
     * @throws NumberFormatException if the string does not contain a parsable number type
     */
    @Nullable
    default T parse(@NotNull String s) throws NumberFormatException {
        if (s.trim().isEmpty()) {
            throw new IllegalArgumentException("Cannot parse empty string");
        }

        int start = 0;
        int end = s.length();

        char last = s.charAt(end - 1);

        if (getSuffix() != '\0' && last == getSuffix() || last == Character.toUpperCase(getSuffix())) {
            if (--end == start) {
                throw new NumberFormatException("For input string: \"" + s + "\"");
            }
            last = s.charAt(end - 1);
        }

        char first = s.charAt(start);

        final boolean unsigned;
        final boolean negative;
        if (last == 'u') {
            if (--end == 0) {
                throw new NumberFormatException("For input string: \"" + s + "\"");
            }
            if (first == '-') {
                throw new IllegalArgumentException("Cannot parse negative unsigned number: '" + s + "'");
            } else if (first == '+') {
                if (++start == end) {
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                } else {
                    first = s.charAt(start);
                }
            }

            unsigned = true;
            negative = false;
        } else {
            unsigned = false;
            if (first == '-') {
                if (++start == end) {
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                } else {
                    first = s.charAt(start);
                }
                negative = true;
            } else {
                if (first == '+' && ++start == end) {
                    throw new NumberFormatException("For input string: \"" + s + "\"");
                } else {
                    first = s.charAt(start);
                }

                negative = false;
            }
        }

        int radix = 10;

        if (first == '0') {
            if (start + 1 < end) {
                final char second = s.charAt(start + 1);
                switch (second) {
                    case 'x':
                    case 'X':
                        // hex
                        radix = 16;
                        start += 2;
                        break;
                    case 'b':
                    case 'B':
                        // binary
                        radix = 2;
                        start += 2;
                        break;
                    default:
                        // octal
                        boolean octal = true;
                        for (int i = start + 1; i < end; i++) {
                            final char c = s.charAt(i);
                            if (!Character.isDigit(c)) {
                                throw new NumberFormatException("Error at index " + i + " in: " + s);
                            }
                            if (c == '8' || c == '9') {
                                octal = false;
                                break;
                            }
                        }
                        if (octal) {
                            radix = 8;
                            start += 2;
                        }
                        break;
                }
            }
        } else if (first == '#') { // hex
            radix = 16;
            start++;
        }

        if (start == end) {
            throw new NumberFormatException("For input string: \"" + s + "\"" + (radix == 10 ? "" : " under radix " + radix));
        }

        s = (negative ? "-" : "") + s.substring(start, end);

        if (unsigned) {
            return parseUnsignedNumber(s, radix);
        } else {
            return radix == 10 ? parseNumber(s) : parseNumber(s, radix);
        }
    }

    /**
     * Parses the given number argument as required number type.<br>
     * This method check any out if range number.
     *
     * @param number the number to parse.
     * @return       a converted number type.
     */
    @NotNull
    default T parseNumber(@NotNull Number number) {
        if (!isBigHolder() && getMinValue().getClass() != number.getClass()) {
            if (number instanceof BigInteger) {
                if (((BigInteger) number).compareTo(BigInteger.valueOf(getMinValue().longValue())) < 0 || ((BigInteger) number).compareTo(BigInteger.valueOf(getMaxValue().longValue())) > 0) {
                    throw new IllegalArgumentException("The number " + number + " cannot be cast to " + getType() + " (out of range: [" + getMinValue()  + ", " + getMaxValue() + "])");
                }
            } else if (number instanceof BigDecimal) {
                if (((BigDecimal) number).compareTo(BigDecimal.valueOf(getMinValue().doubleValue())) < 0 || ((BigDecimal) number).compareTo(BigDecimal.valueOf(getMaxValue().doubleValue())) > 0) {
                    throw new IllegalArgumentException("The number " + number + " cannot be cast to " + getType() + " (out of range: [" + getMinValue()  + ", " + getMaxValue() + "])");
                }
            } else if (!(getMinValue() instanceof Double)) {
                final double min = getMinValue().doubleValue();
                final double max = getMaxValue().doubleValue();
                final double d = number.doubleValue();
                if (d < min || d > max) {
                    throw new IllegalArgumentException("The number " + number + " cannot be cast to " + getType() + " (out of range: [" + getMinValue()  + ", " + getMaxValue() + "])");
                }
                if (!isDecimalHolder() && (number instanceof Float || number instanceof Double) && (d == min || d == max)) {
                    if (d - Math.floor(d) > Float.MIN_NORMAL) {
                        throw new IllegalArgumentException("The number " + number + " cannot be cast to " + getType() + " (the decimal part make it out of range: [" + getMinValue()  + ", " + getMaxValue() + "])");
                    }
                }
            }
        }
        return cast(number);
    }

    /**
     * Parses the given boolean argument as required number type.
     *
     * @param bool the boolean to parse.
     * @return     a converted number type.
     */
    @NotNull
    default T parseNumber(boolean bool) {
        return cast(bool ? (byte) 1 : (byte) 0);
    }

    /**
     * Parses the given string argument as required number type.
     *
     * @param s the string to parse.
     * @return  a converted number type.
     * @throws NumberFormatException if the string does not contain a parsable number type
     */
    @NotNull
    T parseNumber(@NotNull String s) throws NumberFormatException;

    /**
     * Parses the given string argument as required number type in the radix specified by the second argument.
     *
     * @param s     the string to parse.
     * @param radix the radix to be used while parsing the string.
     * @return      a converted number type.
     * @throws NumberFormatException if the string does not contain a parsable number type
     */
    @NotNull
    default T parseNumber(@NotNull String s, int radix) throws NumberFormatException {
        if (radix == 10) {
            return parseNumber(s);
        }
        return cast(LONG.parseNumber(s, radix));
    }

    /**
     * Parses the given string argument as an unsigned required number type.
     *
     * @param s the string to parse.
     * @return  a converted number type.
     * @throws NumberFormatException if the string does not contain a parsable number type
     */
    @NotNull
    default T parseUnsignedNumber(@NotNull String s) throws NumberFormatException {
        if (!s.isEmpty() && s.charAt(0) == '-') {
            throw new NumberFormatException("Illegal leading minus sign on unsigned string " + s);
        }
        return parseNumber(s);
    }

    /**
     * Parses the given string argument as an unsigned required number type in the radix specified by the second argument.
     *
     * @param s     the string to parse.
     * @param radix the radix to be used while parsing the string.
     * @return      a converted number type.
     * @throws NumberFormatException if the string does not contain a parsable number type
     */
    @NotNull
    default T parseUnsignedNumber(@NotNull String s, int radix) throws NumberFormatException {
        if (!s.isEmpty() && s.charAt(0) == '-') {
            throw new NumberFormatException("Illegal leading minus sign on unsigned string " + s);
        }
        return parseNumber(s, radix);
    }
}