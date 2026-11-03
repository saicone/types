package com.saicone.types.parser;

import com.saicone.types.AnyIterable;
import com.saicone.types.TypeParser;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.Color;
import java.util.Map;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * Represents a parser that try to convert any String representation of Color.<br>
 * Using RGBA and HEX as primary format, but also supports type-specific formats like OKLCH.
 *
 * @author Rubenicos
 */
public class ColorParser implements TypeParser<Color> {

    /**
     * {@link ColorParser} public instance that uses RGBA as primary format.
     */
    public static final ColorParser INSTANCE = new ColorParser();

    private final boolean rgba;

    /**
     * Creates a new {@link ColorParser} instance that uses RGBA as primary format.
     */
    public ColorParser() {
        this(true);
    }

    /**
     * Creates a new {@link ColorParser} instance.
     *
     * @param rgba whether to use RGBA as primary format
     */
    public ColorParser(boolean rgba) {
        this.rgba = rgba;
    }

    /**
     * Returns whether this parser uses RGBA as primary format.
     *
     * @return true if RGBA is used as primary format, false otherwise
     */
    public boolean isRGBA() {
        return rgba;
    }


    @Override
    public @NotNull Color parse(@NotNull Object object) throws Throwable {
        final Object first = AnyIterable.first(object);
        Objects.requireNonNull(first, "Cannot get effective first element");

        if (first instanceof Color) {
            return (Color) first;
        } else if (first instanceof String) {
            return fromString((String) first, this.isRGBA());
        } else if (first instanceof Map) {
            return fromMapRGB((Map<?, ?>) first);
        } else {
            throw new IllegalArgumentException("Cannot parse color from object: " + object);
        }
    }

    /**
     * Converts a string representation of a color to a {@link Color} object.
     *
     * @param color the string representation of the color
     * @return      the corresponding {@link Color} object
     * @throws IllegalArgumentException if the string cannot be parsed as a color
     */
    @NotNull
    public static Color fromString(@NotNull String color) throws IllegalArgumentException {
        return fromString(color, true);
    }

    /**
     * Converts a string representation of a color to a {@link Color} object.
     *
     * @param color the string representation of the color
     * @param rgba  whether to use RGBA as primary format
     * @return      the corresponding {@link Color} object
     * @throws IllegalArgumentException if the string cannot be parsed as a color
     */
    @NotNull
    public static Color fromString(@NotNull String color, boolean rgba) throws IllegalArgumentException {
        color = color.trim();

        if (color.startsWith("#") || color.length() == 6 || color.length() == 8) {
            return fromStringHEX(color, rgba);
        }

        final String type;
        final String[] args;

        final int index = color.indexOf('(');
        if (index > 0 && color.endsWith(")")) {
            type = color.substring(0, index).trim();

            final String str = color.substring(index + 1, color.length() - 1).trim();
            if (str.contains(",")) {
                args = str.split("\\s*,\\s*");
            } else {
                args = str.split("\\s+");
            }
        } else {
            if (color.contains(",")) {
                args = color.split("\\s*,\\s*");
            } else {
                args = color.split("\\s+");
            }

            if (args.length < 3) {
                throw new IllegalArgumentException("Unknown color format: " + color);
            }

            if (args[0].contains(".") || args[1].contains(".") || args[2].contains(".")) {
                type = "oklch";
            } else if (args.length == 4) {
                type = rgba ? "rgba" : "argb";
            } else if (args.length == 3) {
                type = "rgb";
            } else {
                throw new IllegalArgumentException("Unknown color format: " + color);
            }
        }

        switch (type.toLowerCase()) {
            case "hex":
                return fromStringHEX(args[0], rgba);
            case "rgb":
                return fromStringRGB(args[0], args[1], args[2]);
            case "rgba":
                return fromStringRGBA(args[0], args[1], args[2], args[3]);
            case "argb":
                return fromStringRGBA(args[1], args[2], args[3], args[0]);
            case "oklch":
                return fromStringOKLCH(args[0], args[1], args[2]);
            default:
                throw new IllegalArgumentException("Unknown color type: " + type);
        }
    }

    /**
     * Converts a hex string representation of a color to a {@link Color} object.
     *
     * @param hex the hex string representation of the color
     * @return    the corresponding {@link Color} object
     * @throws IllegalArgumentException if the hex string cannot be parsed as a color
     */
    @NotNull
    public static Color fromStringHEX(@NotNull String hex) throws IllegalArgumentException {
        return fromStringHEX(hex, true);
    }

    /**
     * Converts a hex string representation of a color to a {@link Color} object.
     *
     * @param hex  the hex string representation of the color
     * @param rgba whether to use RGBA as primary format
     * @return     the corresponding {@link Color} object
     * @throws IllegalArgumentException if the hex string cannot be parsed as a color
     */
    @NotNull
    public static Color fromStringHEX(@NotNull String hex, boolean rgba) throws IllegalArgumentException {
        if (hex.startsWith("#")) {
            hex = hex.substring(1);
        }

        final int color;
        try {
            color = (int) Long.parseLong(hex, 16);
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException("Invalid hex color: " + hex, e);
        }

        if (hex.length() == 6) {
            return new Color(color);
        } else if (rgba) {
            return new Color(
                    (color >> 24) & 0xFF,
                    (color >> 16) & 0xFF,
                    (color >> 8) & 0xFF,
                    color & 0xFF
            );
        } else {
            return new Color(
                    (color >> 16) & 0xFF,
                    (color >> 8) & 0xFF,
                    color & 0xFF,
                    (color >> 24) & 0xFF
            );
        }
    }

    /**
     * Converts RGB string components to a {@link Color} object.
     *
     * @param r the red component as a string
     * @param g the green component as a string
     * @param b the blue component as a string
     * @return  the corresponding {@link Color} object
     * @throws IllegalArgumentException if any of the components cannot be parsed as an integer
     */
    @NotNull
    public static Color fromStringRGB(@NotNull String r, @NotNull String g, @NotNull String b) throws IllegalArgumentException {
        return new Color(
                parseIntComponent(r, () -> "Red value is not a valid integer: " + r),
                parseIntComponent(g, () -> "Green value is not a valid integer: " + g),
                parseIntComponent(b, () -> "Blue value is not a valid integer: "+ b)
        );
    }

    /**
     * Converts RGBA string components to a {@link Color} object.
     *
     * @param r the red component as a string
     * @param g the green component as a string
     * @param b the blue component as a string
     * @param a the alpha component as a string
     * @return  the corresponding {@link Color} object
     * @throws IllegalArgumentException if any of the components cannot be parsed as an integer
     */
    @NotNull
    public static Color fromStringRGBA(@NotNull String r, @NotNull String g, @NotNull String b, @NotNull String a) throws IllegalArgumentException {
        return new Color(
                parseIntComponent(r, () -> "Red value is not a valid integer: " + r),
                parseIntComponent(g, () -> "Green value is not a valid integer: " + g),
                parseIntComponent(b, () -> "Blue value is not a valid integer: "+ b),
                parseIntComponent(a, () -> "Alpha value is not a valid integer: "+ a)
        );
    }

    /**
     * Converts OKLCH string components to a {@link Color} object.
     *
     * @param L the lightness component as a string
     * @param C the chroma component as a string
     * @param H the hue component as a string
     * @return  the corresponding {@link Color} object
     * @throws NumberFormatException if any of the components cannot be parsed as a double
     */
    @NotNull
    public static Color fromStringOKLCH(@NotNull String L, @NotNull String C, @NotNull String H) throws NumberFormatException {
        double Ld = Double.parseDouble(L);
        double Cd = Double.parseDouble(C);
        double Hd = Double.parseDouble(H);
        return oklch(Ld, Cd, Hd);
    }

    /**
     * Converts a map representation of a color to a {@link Color} object.
     * The map should contain keys "red", "green", "blue", and optionally "alpha".
     *
     * @param map the map representation of the color
     * @return    the corresponding {@link Color} object
     * @throws IllegalArgumentException if the map does not contain valid color components
     */
    @NotNull
    public static Color fromMapRGB(@NotNull Map<?, ?> map) throws IllegalArgumentException {
        final int red = getInt(map, "red");
        final int green = getInt(map, "green");
        final int blue = getInt(map, "blue");
        final Integer alpha = getIntOrNull(map, "alpha");
        if (alpha != null) {
            return new Color(red, green, blue, alpha);
        } else {
            return new Color(red, green, blue);
        }
    }

    private static int getInt(@NotNull Map<?, ?> map, @NotNull String key) throws IllegalArgumentException {
        Object value = map.get(key);
        if (value == null) {
            throw new IllegalArgumentException("The map does not contain the key: " + key);
        }
        return parseIntComponent(String.valueOf(value), () -> "The value for key '" + key + "' is not a valid integer: " + value);
    }

    @Nullable
    private static Integer getIntOrNull(@NotNull Map<?, ?> map, @NotNull String key) throws IllegalArgumentException {
        Object value = map.get(key);
        if (value == null) {
            return null;
        }
        return parseIntComponent(String.valueOf(value), () -> "The value for key '" + key + "' is not a valid integer: " + value);
    }

    private static int parseIntComponent(@NotNull String s, @NotNull Supplier<String> errorMessage) throws IllegalArgumentException {
        try {
            final int i = Integer.parseInt(s);
            if (i < 0 || i > 255) {
                throw new IllegalArgumentException(errorMessage.get());
            }
            return i;
        } catch (NumberFormatException e) {
            throw new IllegalArgumentException(errorMessage.get(), e);
        }
    }

    /**
     * Converts OKLCH color space values to a {@link Color} object.
     *
     * @param L the lightness component (0.0 to 1.0)
     * @param C the chroma component (0.0 to 1.0)
     * @param H the hue component in degrees (0.0 to 360.0)
     * @return  the corresponding {@link Color} object
     */
    @NotNull
    public static Color oklch(double L, double C, double H) {
        final double h = Math.toRadians(H);

        // OKLCH -> OKLab
        double a = Math.cos(h) * C;
        double b = Math.sin(h) * C;

        // OKLab -> LMS (lineal)
        double l_ = L + 0.3963377774 * a + 0.2158037573 * b;
        double m_ = L - 0.1055613458 * a - 0.0638541728 * b;
        double s_ = L - 0.0894841775 * a - 1.2914855480 * b;

        // LMS -> RGB (lineal)
        double l = Math.pow(l_, 3);
        double m = Math.pow(m_, 3);
        double s = Math.pow(s_, 3);

        // RGB
        double r = +4.0767416621 * l - 3.3077115913 * m + 0.2309699292 * s;
        double g = -1.2684380046 * l + 2.6097574011 * m - 0.3413193965 * s;
        double b1 = -0.0041960863 * l - 0.7034186147 * m + 1.7076147010 * s;

        // Linear RGB -> sRGB
        r = linearToSrgb(r);
        g = linearToSrgb(g);
        b1 = linearToSrgb(b1);

        // Fix range [0,1]
        r = clamp(r, 0, 1);
        g = clamp(g, 0, 1);
        b1 = clamp(b1, 0, 1);

        return new Color((float) r, (float) g, (float) b1);
    }

    private static double linearToSrgb(double value) {
        if (value <= 0.0031308) {
            return value * 12.92;
        }
        return 1.055 * Math.pow(value, 1.0 / 2.4) - 0.055;
    }

    private static double clamp(double v, double min, double max) {
        return Math.max(min, Math.min(max, v));
    }
}
